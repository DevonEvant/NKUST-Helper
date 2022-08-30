package com.example.nkustplatformassistant.data.persistence

import android.content.Context
import android.util.Log
import com.example.nkustplatformassistant.data.persistence.db.NkustDatabase
import com.example.nkustplatformassistant.data.persistence.db.entity.ScoreEntity
import com.example.nkustplatformassistant.ui.login.user
import java.lang.IndexOutOfBoundsException


class DataRepository(context: Context) {
    private val db = NkustDatabase.getDatabase(context)

    suspend fun fetchScoreData() {
        val listToInsert = getAllScoreToTypedScoreEntity()
        db.ScoreDao().insertMultiScore(listToInsert)
    }
}

/**
 * By using this function, you'll get a [List]<[ScoreEntity]>
 * including all score from the year you enrolled to the latest (now)
 */
suspend fun getAllScoreToTypedScoreEntity(): List<ScoreEntity> {
    val semYearMap = mutableMapOf<String, String>()
    user.getYearsOfDropDownListByMap()
        .forEach { (yearSemester, _) ->
            yearSemester.split(",").let {
                semYearMap[it[1]] = it[0]
            }
        }

    val scoreEntityList = mutableListOf<ScoreEntity>()
    try {
        semYearMap.forEach { (semester, year) ->
            user.getYearlyScore(year, semester)
                .forEach { (subjectName, midScore, finalScore) ->
                    scoreEntityList.add(ScoreEntity(null,
                        year.toInt(),
                        semester.toInt(),
                        subjectName,
                        midScore,
                        finalScore))
                }
        }
    } catch (e: IndexOutOfBoundsException) {
        println("Error when getting yearly score: ${e.toString()}\n" +
                "It maybe no data, but it's okay")
    }

    println(scoreEntityList.size)

    return scoreEntityList
}