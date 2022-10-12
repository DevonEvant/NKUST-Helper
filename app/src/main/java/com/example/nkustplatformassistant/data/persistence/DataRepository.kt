package com.example.nkustplatformassistant.data.persistence

import android.content.Context
import androidx.compose.ui.graphics.ImageBitmap
import com.example.nkustplatformassistant.data.*
import com.example.nkustplatformassistant.data.persistence.db.NkustDatabase
import com.example.nkustplatformassistant.data.persistence.db.entity.CourseEntity
import com.example.nkustplatformassistant.data.persistence.db.entity.ScoreEntity
import com.example.nkustplatformassistant.data.persistence.db.entity.ScoreOtherEntity
import com.example.nkustplatformassistant.data.remote.NkustAccessor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.example.nkustplatformassistant.data.remote.NkustClient
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

    // TODO: Check if calender_table is null and add it to companion object (if can)
    suspend fun checkDataIsReady(): Boolean {
        val isDataReady = mutableListOf<Boolean>()
        withContext(Dispatchers.IO) {
            isDataReady.addAll(
                listOf(isScoreExist(), isScoreOtherExist(), isCourseExist())
            )
        }
        return !isDataReady.contains(false)
    }

    // TODO: Calendar Dao
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
    suspend fun getScoreDropDownList(): List<DropDownParams>{
        lateinit var dropDownList: List<DropDownParams>
        withContext(Dispatchers.IO){
            dropDownList = db.scoreDao().getDropDownList()
        }
        return dropDownList
    }

    suspend fun getSpecScoreDataFromDB(year: Int, semester: Int): List<ScoreEntity> {
        return db.scoreDao().getSpecScoreList(year, semester)
    }

    suspend fun getSpecScoreOtherDataFromDB(year: Int, semester: Int): ScoreOtherEntity{
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
            latestCourseList.year, latestCourseList.semester)

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
    suspend fun getScheduleToDB(
        year: String,
        semester: String,
    ): List<NkustEvent> {
        throw Error("not complete")
        withContext(Dispatchers.IO) {
        }
    }

}

// TODO: Reuse allListToGet
// var listToGet: List<List<String>> = listOf()

private suspend fun allListToGet(nkustAccessor: NkustAccessor): List<List<String>> {
    val listToGet: MutableList<List<String>> = mutableListOf()
    nkustAccessor.getYearsOfDropDownListByMap()
        .forEach { (yearSemester, Description) ->
            yearSemester.split(",").let {
                listToGet.add(listOf(it[0], it[1], Description))
            }
        }

    return listToGet
}

/**
 * By using this function, you'll get a [List]<[ScoreEntity]>
 * including all score from the year you enrolled to the latest (now)
 */
private suspend fun getAllScoreToTypedScoreEntity(nkustAccessor: NkustAccessor): List<ScoreEntity> {

    val listToGet = allListToGet(nkustAccessor)

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

private suspend fun getAllScoreOtherToTypedScoreOtherEntity(nkustAccessor: NkustAccessor): List<ScoreOtherEntity> {
    val listToGet = allListToGet(nkustAccessor)

    val scoreOtherList = mutableListOf<ScoreOtherEntity>()

    listToGet.forEach {
        scoreOtherList.add(nkustAccessor.getSemesterScoreOther(it[0], it[1], it[2]))
    }
    return scoreOtherList
}

private suspend fun getAllCourseToTypedCourseEntity(nkustAccessor: NkustAccessor): List<CourseEntity> {
    val listToGet = allListToGet(nkustAccessor)

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