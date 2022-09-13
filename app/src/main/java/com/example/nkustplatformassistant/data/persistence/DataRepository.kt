package com.example.nkustplatformassistant.data.persistence

import android.content.Context
import androidx.compose.ui.graphics.ImageBitmap
import com.example.nkustplatformassistant.data.CurriculumTime
import com.example.nkustplatformassistant.data.DropDownParams
import com.example.nkustplatformassistant.data.NkustEvent
import com.example.nkustplatformassistant.data.Score
import com.example.nkustplatformassistant.data.persistence.db.NkustDatabase
import com.example.nkustplatformassistant.data.persistence.db.entity.CourseEntity
import com.example.nkustplatformassistant.data.persistence.db.entity.ScoreEntity
import com.example.nkustplatformassistant.data.remote.NkustAccessor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class DataRepository(context: Context) {
    companion object {
        @Volatile
        private var INSTANCE: DataRepository? = null

        fun getInstance(context: Context): DataRepository {
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: DataRepository(context).also { INSTANCE = it }
            }

            return INSTANCE as DataRepository
        }
    }


    private val nkustAccessor = NkustAccessor()
    private val db = NkustDatabase.getInstance(context)

    // TODO: Check if calender_table is null and add it to companion object (if can)
    suspend fun checkDataIsReady(): Boolean {
        val isDataReady = mutableListOf<Boolean>()
        withContext(Dispatchers.IO) {
            isDataReady.addAll(
                listOf(isScoreExist(), isCourseExist())
            )
        }
        return !isDataReady.contains(false)
    }

    // TODO: Calendar Dao
    suspend fun clearAllDB() {
        db.scoreDao().emptyScoreTable()
        db.courseDao().emptyCourseTable()
    }

    // Login...
    suspend fun userLogin(uid: String, pwd: String, etxtCode: String): Boolean {
        return nkustAccessor.loginWebap(uid, pwd, etxtCode)
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

    /**
     * By using [getLatestScoreParams], you can get latest year, semester, and semester description
     * with a data class of [DropDownParams]
     */
    suspend fun getLatestScoreParams(): DropDownParams {
        lateinit var latestScoreParams: DropDownParams
        withContext(Dispatchers.IO) {
            latestScoreParams = db.scoreDao().getLatestScoreList()
        }
        return latestScoreParams
    }

    suspend fun getSpecScoreDataFromDB(year: Int, semester: Int): List<ScoreEntity> {
        return db.scoreDao().getSpecScoreList(year, semester)
    }


    private suspend fun isScoreExist(): Boolean {
        var exist: Boolean
        withContext(Dispatchers.IO) {
            exist = db.scoreDao().isExist()
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

    internal suspend fun getLatestCourseParams(): DropDownParams {
        return db.courseDao().getLatestCourseParams()
    }

    suspend fun getCurrentCourse(/*time: CurriculumTime*/):CourseEntity {
        val todayCourseList = getLatestCourseParams()
        val result = db.courseDao().getSpecCourseList(
            todayCourseList.year, todayCourseList.semester)
        return result[0]
    }

    // Schedule
    suspend fun getSchedule(
        year: String,
        semester: String,
        refresh: Boolean = false,
    ): List<NkustEvent> {
        throw Error("not complete")
        withContext(Dispatchers.IO) {
        }
    }

}

suspend fun allListToGet(nkustAccessor: NkustAccessor): List<List<String>> {
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
suspend fun getAllScoreToTypedScoreEntity(nkustAccessor: NkustAccessor): List<ScoreEntity> {

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

suspend fun getAllCourseToTypedCourseEntity(nkustAccessor: NkustAccessor): List<CourseEntity> {
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