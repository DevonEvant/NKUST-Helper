package com.example.nkustplatformassistant.data.persistence.db.dao

import androidx.room.*
import com.example.nkustplatformassistant.data.DropDownParams
import com.example.nkustplatformassistant.data.persistence.db.entity.ScoreEntity

@Dao
interface ScoreDao {

//    @Query("SELECT * FROM score_table ORDER BY id ASC")
//    fun getScoreList(): List<ScoreEntity>

    @Query("SELECT * FROM score_table WHERE year = :year AND semester = :semester")
    fun getSpecScoreList(year: Int, semester: Int): List<ScoreEntity>

    @Query("SELECT DISTINCT year,semester,semDescription FROM SCORE_TABLE ORDER BY YEAR, SEMESTER DESC")
    fun getDropDownList(): List<DropDownParams>

    @Query("SELECT DISTINCT year,semester,semDescription FROM SCORE_TABLE ORDER BY semester,year LIMIT 1")
    fun getLatestScoreList(): DropDownParams

    // SELECT name FROM sqlite_master WHERE type='table' AND name='{table_name}';

    @Query("SELECT COUNT(*) FROM score_table")
    fun sizeOfDB(): Int

    @Query("")
    fun isExist(): Boolean {
        val dbSize = sizeOfDB()
        return dbSize > 1
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScore(scoreEntity: ScoreEntity)

    @Query("")
    suspend fun insertMultiScore(
        subjectList: List<ScoreEntity>,
    ) {
        emptyScoreTable()

        for (item in subjectList) {
            insertScore(ScoreEntity(
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