package com.narui.nkustplatformassistant.data.persistence.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.narui.nkustplatformassistant.data.DropDownParams
import com.narui.nkustplatformassistant.data.persistence.db.entity.ScoreOtherEntity

@Dao
interface ScoreOtherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScoreOther(scoreOtherEntity: ScoreOtherEntity)

    @Query("SELECT DISTINCT year,semester,semDescription FROM SCORE_OTHER_TABLE ORDER BY year DESC, semester DESC")
    suspend fun getDropDownList(): List<DropDownParams>

    @Query("SELECT * FROM SCORE_OTHER_TABLE WHERE year = :year AND semester = :semester")
    suspend fun getScoreOther(year: Int, semester: Int): ScoreOtherEntity

    @Query("SELECT COUNT(*) FROM SCORE_OTHER_TABLE")
    fun sizeOfDB(): Int

    @Query("")
    fun isExist(): Boolean {
        val dbSize = sizeOfDB()
        return dbSize > 1
    }

    @Query("DELETE FROM SCORE_OTHER_TABLE")
    suspend fun emptyScoreOtherTable()
}