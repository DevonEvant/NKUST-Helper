package com.example.nkustplatformassistant.data.persistence.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.nkustplatformassistant.data.persistence.db.entity.Calender
import com.example.nkustplatformassistant.data.remote.Subject
import kotlinx.coroutines.flow.Flow

@Dao
interface CalenderDao {
    @Query("SELECT * FROM calender_table ORDER BY id ASC")
    fun getCalender(): Flow<List<Calender>>

//        @Query("SELECT * FROM score_table ORDER BY mid_term_exam_score DESC")
//        fun get

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScore(vararg calender: Calender)

    @Query("DELETE FROM calender_table")
    suspend fun emptyCalenderTable()
}