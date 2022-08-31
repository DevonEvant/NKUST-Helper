package com.example.nkustplatformassistant.data.persistence

import android.content.Context
import com.example.nkustplatformassistant.data.persistence.db.NkustDatabase
import com.example.nkustplatformassistant.data.persistence.db.entity.ScoreEntity
import com.example.nkustplatformassistant.data.remote.FetchData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.IndexOutOfBoundsException

val user = FetchData()

class DataRepository(context: Context) {

    private val db = NkustDatabase.getDatabase(context)

    private suspend fun fetchScoreDataToDB() {
        val listToInsert = getAllScoreToTypedScoreEntity()
        db.ScoreDao().insertMultiScore(listToInsert)
    }

    suspend fun getSpecScoreDataFromDB(year: Int, semester: Int) {
        db.ScoreDao().getSpecScoreList(year, semester)
    }

    suspend fun userLogin(uid: String, pwd: String, etxtCode: String): Boolean {
        return user.loginWebap(uid, pwd, etxtCode)
    }

    suspend fun fetchData(context: Context) {
        withContext(Dispatchers.IO) {
            DataRepository(context).fetchScoreDataToDB()
        }
    }
}

/**
 * By using this function, you'll get a [List]<[ScoreEntity]>
 * including all score from the year you enrolled to the latest (now)
 */
suspend fun getAllScoreToTypedScoreEntity(): List<ScoreEntity> {
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
                    scoreEntityList.add(ScoreEntity(null,
                        year.toInt(),
                        semester.toInt(),
                        semDescription,
                        subjectName,
                        midScore,
                        finalScore))
                }
        }
    } catch (e: IndexOutOfBoundsException) {
        println("Error when getting yearly score: $e\n" +
                "It maybe no data, but it's okay")
    }

//    println(scoreEntityList.size)

    return scoreEntityList
}