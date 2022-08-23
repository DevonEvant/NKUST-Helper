package com.example.nkustplatformassistant.data.persistence.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.nkustplatformassistant.data.remote.Subject
import kotlinx.coroutines.flow.Flow

@Dao
interface ScoreDao {
    @Query("SELECT * FROM score_table ORDER BY id ASC")
    fun getScoreList(): Flow<List<Subject>>

//        @Query("SELECT * FROM score_table ORDER BY mid_term_exam_score DESC")
//        fun get

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScore(vararg subject: Subject)

    @Query("DELETE FROM score_table")
    suspend fun emptyScoreTable()
}