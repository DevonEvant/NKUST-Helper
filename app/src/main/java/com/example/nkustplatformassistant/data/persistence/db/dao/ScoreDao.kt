package com.example.nkustplatformassistant.data.persistence.db.dao

import androidx.room.*
import com.example.nkustplatformassistant.data.persistence.db.entity.ScoreDropDownParams
import com.example.nkustplatformassistant.data.persistence.db.entity.ScoreEntity

@Dao
interface ScoreDao {

    @Query("SELECT * FROM score_table ORDER BY id ASC")
    fun getScoreList(): List<ScoreEntity>

    @Query("SELECT * FROM score_table WHERE year = :year AND semester = :semester")
    fun getSpecScoreList(year: Int, semester: Int): List<ScoreEntity>

    @Query("SELECT DISTINCT year,semester,semDescription FROM SCORE_TABLE")
    fun getDropDownList(): List<ScoreDropDownParams>

    @Query("SELECT DISTINCT year,semester,semDescription FROM SCORE_TABLE ORDER BY semester,year LIMIT 1")
    fun getLatestScoreList(): ScoreDropDownParams

    @Query("SELECT EXISTS(SELECT * FROM score_table)")
    fun isExist():Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScore(scoreEntity: ScoreEntity)

    @Query("")
    suspend fun insertMultiScore(
        subjectList: List<ScoreEntity>,
    ) {
        for (item in subjectList) {
            insertScore(ScoreEntity(null,
                item.year,
                item.semester,
                item.semDescription,
                item.subjectName,
                item.midScore,
                item.finalScore)
            )
        }
    }

    @Query("DELETE FROM score_table")
    suspend fun emptyScoreTable()
}