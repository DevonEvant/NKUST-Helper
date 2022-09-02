package com.example.nkustplatformassistant.data.persistence

import android.content.Context
import androidx.compose.ui.graphics.ImageBitmap
import com.example.nkustplatformassistant.data.NkustEvent
import com.example.nkustplatformassistant.data.persistence.db.NkustDatabase
import com.example.nkustplatformassistant.data.persistence.db.entity.ScoreDropDownParams
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

    // TODO: Check if score, subject, calender is null
    suspend fun checkDataIsReady(): Boolean {
        val isDataReady = mutableListOf<Boolean>()
        withContext(Dispatchers.IO) {
            isDataReady.add(
                isScoreExist()
            )
        }
        return !isDataReady.contains(false)
    }

    // Login...
    suspend fun userLogin(uid: String, pwd: String, etxtCode: String): Boolean {
        return nkustAccessor.loginWebap(uid, pwd, etxtCode)
    }

    // Score...
    private suspend fun fetchScoreDataToDB() {
        val listToInsert = getAllScoreToTypedScoreEntity()
        db.ScoreDao().insertMultiScore(listToInsert)
    }

    suspend fun getDropDownListFromDB(): List<ScoreDropDownParams> {
        val dropDownList = mutableListOf<ScoreDropDownParams>()
        withContext(Dispatchers.IO) {
            dropDownList.addAll(
                db.ScoreDao().getDropDownList()
            )
        }
        println(dropDownList.size)
        return dropDownList
    }

    /**
     * By using [getLatestScoreParams], you can get latest year, semester, and semester description
     * with a data class of [ScoreDropDownParams]
     */
    suspend fun getLatestScoreParams(): ScoreDropDownParams {
        lateinit var latestScoreParams: ScoreDropDownParams
        withContext(Dispatchers.IO) {
            latestScoreParams = db.ScoreDao().getLatestScoreList()
        }
        return latestScoreParams
    }

    suspend fun getSpecScoreDataFromDB(year: Int, semester: Int): List<ScoreEntity> {
        return db.ScoreDao().getSpecScoreList(year, semester)
    }

    suspend fun getAllScore() {
        fetchScoreDataToDB()
    }

    private suspend fun isScoreExist(): Boolean {
        var scoreExist: Boolean
        withContext(Dispatchers.IO) {
            scoreExist = db.ScoreDao().isExist()
        }
        return scoreExist
    }

    // Subject...
    suspend fun fetchData(context: Context) {
        withContext(Dispatchers.IO) {
            DataRepository(context).fetchScoreDataToDB()
        }
    }

    suspend fun userLogin(
        uid: String,
        pwd: String,
        etxtCode: String,
        reflash: Boolean = false
    ): Boolean {
        if (reflash)
            throw Error("not complete")

        return nkustAccessor.loginWebap(uid, pwd, etxtCode)
    }

    suspend fun getSchedule(
        year: String,
        semester: String,
        refresh: Boolean = false
    ): List<NkustEvent> {
        throw Error("not complete")
        withContext(Dispatchers.IO) {
        }
    }

    suspend fun getAllScores(
        year: String,
        semester: String,
        reflash: Boolean = false
    ): List<ScoreEntity> {
        throw Error("not complete")
        withContext(Dispatchers.IO) {
//            if (reflash)

        }
    }

    suspend fun getWebapCaptchaImage(
        refresh: Boolean = false
    ): ImageBitmap {
        throw Error("not complete")
        withContext(Dispatchers.IO) {
            if (refresh)
                nkustAccessor.getWebapEtxtBitmap()

        }
    }

}

/**
 * By using this function, you'll get a [List]<[ScoreEntity]>
 * including all score from the year you enrolled to the latest (now)
 */
suspend fun getAllScoreToTypedScoreEntity(): List<ScoreEntity> {
    val user = NkustAccessor()

    val listToGet: MutableList<List<String>> = mutableListOf()
    user.getYearsOfDropDownListByMap()
        .forEach { (yearSemester, Description) ->
            yearSemester.split(",").let {
                listToGet.add(listOf(it[0], it[1], Description))
            }
        }


    val scoreEntityList = mutableListOf<ScoreEntity>()
    try {
        listToGet.forEach { (semester, year, semDescription) ->
            user.getYearlyScore(year, semester)
                .forEach { (subjectName, midScore, finalScore) ->
                    scoreEntityList.add(
                        ScoreEntity(
                            null,
                            year.toInt(),
                            semester.toInt(),
                            semDescription,
                            subjectName,
                            midScore,
                            finalScore
                        )
                    )
                }
        }
    } catch (e: IndexOutOfBoundsException) {
        println(
            "Error when getting yearly score: $e\n" +
                    "It maybe no data, but it's okay"
        )
    }

//    println(scoreEntityList.size)

    return scoreEntityList
}
