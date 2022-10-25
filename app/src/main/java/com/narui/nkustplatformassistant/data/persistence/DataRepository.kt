package com.narui.nkustplatformassistant.data.persistence

import android.content.Context
import androidx.compose.ui.graphics.ImageBitmap
import com.narui.nkustplatformassistant.data.DropDownParams
import com.narui.nkustplatformassistant.data.NkustEvent
import com.narui.nkustplatformassistant.data.Score
import com.narui.nkustplatformassistant.data.persistence.db.NkustDatabase
import com.narui.nkustplatformassistant.data.persistence.db.entity.CourseEntity
import com.narui.nkustplatformassistant.data.persistence.db.entity.ScheduleEntity
import com.narui.nkustplatformassistant.data.persistence.db.entity.ScoreEntity
import com.narui.nkustplatformassistant.data.persistence.db.entity.ScoreOtherEntity
import com.narui.nkustplatformassistant.data.remote.NkustAccessor
import com.narui.nkustplatformassistant.data.remote.NkustClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.*
import java.time.LocalDate

class DataRepository(context: Context) {
    companion object {
        @Volatile
        private var INSTANCE: DataRepository? = null
        var loginState = false

        fun getInstance(context: Context): DataRepository {
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: DataRepository(context).also { INSTANCE = it }
            }

            return INSTANCE as DataRepository
        }
    }

    private val nkustClient = NkustClient.getInstance(context)
    private val nkustAccessor = NkustAccessor(nkustClient)
    private val db = NkustDatabase.getInstance(context)

    // TODO: Check if schedule_table is null and add it to companion object (if can)
    suspend fun checkDataIsReady(): Boolean {
        val isDataReady = mutableListOf<Boolean>()
        withContext(Dispatchers.IO) {
            isDataReady.addAll(
                listOf(isScoreExist(), isScoreOtherExist(), isCourseExist(), isScheduleExist())
            )
        }
        return !isDataReady.contains(false)
    }

    // TODO: Schedule Dao
    suspend fun clearAllDB() {
        db.scoreDao().emptyScoreTable()
        db.courseDao().emptyCourseTable()
        db.scoreDao().emptyScoreTable()
        db.scoreOtherDao().emptyScoreOtherTable()
    }

    // Login...
    suspend fun userLogin(uid: String, pwd: String, etxtCode: String): Boolean {
        loginState = nkustAccessor.loginWebap(uid, pwd, etxtCode)
        return loginState
    }

    suspend fun userLogin(
        uid: String,
        pwd: String,
        etxtCode: String,
        refresh: Boolean = false,
    ): Boolean {
        if (refresh)
            throw Error("not complete")

        return nkustAccessor.loginWebap(uid, pwd, etxtCode)
    }

    suspend fun getWebapCaptchaImage(
        refresh: Boolean = false,
    ): ImageBitmap {
        lateinit var imageBitmap: ImageBitmap
        withContext(Dispatchers.IO) {
            if (refresh)
                imageBitmap = nkustAccessor.getWebapEtxtBitmap()
        }
        return imageBitmap
    }

    // Score...
    suspend fun fetchAllScoreToDB() {
        fetchScoreDataToDB()
    }

    private suspend fun fetchScoreDataToDB() {
        val listToInsert = getAllScoreToTypedScoreEntity(nkustAccessor)
        db.scoreDao().insertMultiScore(listToInsert)
    }

    suspend fun fetchAllScoreOtherToDB() {
        val listToInsert = getAllScoreOtherToTypedScoreOtherEntity(nkustAccessor)

        for (item in listToInsert) {
            db.scoreOtherDao().insertScoreOther(item)
        }
    }

    suspend fun getDropDownListFromDB(): List<DropDownParams> {
        val dropDownList = mutableListOf<DropDownParams>()
        withContext(Dispatchers.IO) {
            dropDownList.addAll(
                db.scoreDao().getDropDownList()
            )
        }
        println(dropDownList.size)
        return dropDownList
    }

//    /**
//     * By using [getLatestScoreParams], you can get latest year, semester, and semester description
//     * with a data class of [DropDownParams]
//     */
//    suspend fun getLatestScoreParams(): DropDownParams {
//        lateinit var latestScoreParams: DropDownParams
//        withContext(Dispatchers.IO) {
//            latestScoreParams = db.scoreDao().getLatestScoreList()
//        }
//        return latestScoreParams
//    }

    /**
     * Get Score's DropDownList by its availability
     * @return List<[DropDownParams]>
     *
     * first element is the latest param
     */
    suspend fun getScoreDropDownList(): List<DropDownParams> {
        lateinit var dropDownList: List<DropDownParams>
        withContext(Dispatchers.IO) {
            dropDownList = db.scoreDao().getDropDownList()
        }
        return dropDownList
    }

    suspend fun getSpecScoreDataFromDB(year: Int, semester: Int): List<ScoreEntity> {
        return db.scoreDao().getSpecScoreList(year, semester)
    }

    suspend fun getSpecScoreOtherDataFromDB(year: Int, semester: Int): ScoreOtherEntity {
        return db.scoreOtherDao().getScoreOther(year, semester)
    }


    private suspend fun isScoreExist(): Boolean {
        var exist: Boolean
        withContext(Dispatchers.IO) {
            exist = db.scoreDao().isExist()
        }
        return exist
    }

    private suspend fun isScoreOtherExist(): Boolean {
        var exist: Boolean
        withContext(Dispatchers.IO) {
            exist = db.scoreOtherDao().isExist()
        }
        return exist
    }

    private suspend fun isCourseExist(): Boolean {
        var exist: Boolean
        withContext(Dispatchers.IO) {
            exist = db.courseDao().isExist()
        }
        return exist
    }

    private suspend fun isScheduleExist(): Boolean {
        var exist: Boolean
        withContext(Dispatchers.IO) {
            exist = db.scheduleDao().isExist()
        }
        return exist
    }

    // Course
    suspend fun fetchCourseDataToDB() {
        db.courseDao().emptyCourseTable()
        val listToInsert = getAllCourseToTypedCourseEntity(nkustAccessor)
        listToInsert.forEach {
            db.courseDao().insertCourse(it)
        }
    }

    private suspend fun getLatestCourseParams(): DropDownParams {
        return db.courseDao().getLatestCourseParams()
    }

    /**
     * Get today's course
     */
    suspend fun getTodayCourse(): List<CourseEntity> {
        val latestCourseList = getLatestCourseParams()
        val courseList = db.courseDao().getSpecCourseList(
            latestCourseList.year, latestCourseList.semester
        )

        val todayWeek = LocalDate.now().dayOfWeek.value

        val listToReturn = mutableListOf<CourseEntity>()

        courseList.forEach { eachCourseEntity ->
            eachCourseEntity.courseTime.forEach { courseTime ->
                if ((courseTime.week!!.ordinal + 1) == todayWeek) {
                    listToReturn.add(eachCourseEntity)
                }
            }
        }

        return listToReturn
    }

    suspend fun getSpecCurriculumCourse(year: Int, semester: Int): List<CourseEntity> {
        return db.courseDao().getSpecCourseList(year, semester)
    }

    // Schedule
    suspend fun fetchAllScheduleToDB(context: Context) {
        db.scheduleDao().insertMultiSchedule(
            getCurrentYearSchedule(nkustAccessor, context)
        )
    }

    suspend fun getScheduleFromDB(): List<ScheduleEntity> {
        return db.scheduleDao().getAllSchedule()
    }

}

private val listToGet: MutableList<List<String>> = mutableListOf()

private suspend fun allListToGet(nkustAccessor: NkustAccessor, dst: String): List<List<String>> {
    if (listToGet.isEmpty()) {
        nkustAccessor.getYearsOfDropDownListByMap(dst)
            .forEach { (yearSemester, Description) ->
                yearSemester.split(",").let {
                    listToGet.add(listOf(it[0], it[1], Description))
                }
            }
    }

    return listToGet
}

/**
 * By using this function, you'll get a [List]<[ScoreEntity]>
 * including all score from the year you enrolled to the latest (now)
 */
private suspend fun getAllScoreToTypedScoreEntity(
    nkustAccessor: NkustAccessor,
): List<ScoreEntity> {

    val listToGet = allListToGet(nkustAccessor, "AG008")

    val scoreEntityList = mutableListOf<ScoreEntity>()

    listToGet.forEach { (year, semester, semDescription) ->
        try {
            val listToAdd: List<Score> = nkustAccessor.getYearlyScore(year, semester)

            listToAdd.forEach { (subjectName, midScore, finalScore) ->
                scoreEntityList.add(
                    ScoreEntity(
                        year.toInt(),
                        semester.toInt(),
                        semDescription,
                        subjectName,
                        midScore,
                        finalScore
                    )
                )
            }
        } catch (e: NoSuchElementException) {
            // for allCourse.removeFirst()
            println(
                "Error when getting yearly score: $e\n" +
                        "It maybe no data, but it's okay"
            )

        } catch (e: IndexOutOfBoundsException) {
            println(
                "Error when getting yearly score: $e\n" +
                        "It maybe no data, but it's okay"
            )
        }
    }

    return scoreEntityList
}

private suspend fun getAllScoreOtherToTypedScoreOtherEntity(
    nkustAccessor: NkustAccessor,
): List<ScoreOtherEntity> {
    val listToGet = allListToGet(nkustAccessor, "AG008")

    val scoreOtherList = mutableListOf<ScoreOtherEntity>()

    listToGet.forEach {
        scoreOtherList.add(nkustAccessor.getSemesterScoreOther(it[0], it[1], it[2]))
    }
    return scoreOtherList
}

private suspend fun getAllCourseToTypedCourseEntity(
    nkustAccessor: NkustAccessor,
): List<CourseEntity> {
    val listToGet = allListToGet(nkustAccessor, "AG222")

    val courseEntityList = mutableListOf<CourseEntity>()


    listToGet.forEach { (year, semester, semDescription) ->
        try {
            val listToAdd = nkustAccessor.getSpecCurriculum(year, semester, semDescription)
            courseEntityList.addAll(listToAdd)

        } catch (e: NoSuchElementException) {
            // for allCourse.removeFirst()
            println(
                "Error when getting yearly score: $e\n" +
                        "It maybe no data, but it's okay"
            )

        } catch (e: IndexOutOfBoundsException) {
            println(
                "Error when getting yearly score: $e\n" +
                        "It maybe no data, but it's okay"
            )
        }
    }

    return courseEntityList
}

private suspend fun getCurrentYearSchedule(
    nkustAccessor: NkustAccessor,
    context: Context,
): List<ScheduleEntity> {

    val semesterToGet = nkustAccessor.scheduleToGet()
    val schedule = mutableListOf<ScheduleEntity>()

    semesterToGet.forEach { (year, semester) ->
        val pdfFile = File(context.filesDir, "$year-$semester.pdf")

        nkustAccessor.getNkustScheduleCn(
            year = year,
            semester = semester,
            file = pdfFile
        )

        val scheduleTemp = mutableListOf<NkustEvent>()

        scheduleTemp.addAll(
            nkustAccessor.parseNkustSchedule(pdfFile, context.applicationContext)
        )

        scheduleTemp.forEach { eachSchedule: NkustEvent ->
            val dateSet = eachSchedule.time.split("，")[0].split("-", "～")
            schedule.add(
                ScheduleEntity(
                    agency = eachSchedule.agency,
                    startDate = Regex("(\\d*/\\d*)").find(dateSet[0])!!.value,
                    endDate = if (dateSet.size > 1) dateSet[1] else null,
                    description = "${eachSchedule.time}: ${eachSchedule.description}"
                )
            )
        }
    }

    schedule.forEach { eachSchedule: ScheduleEntity ->
        println(
            "${eachSchedule.agency}, ${eachSchedule.startDate}, " +
                    "${eachSchedule.endDate}, ${eachSchedule.description}"
        )
    }

    return schedule
}