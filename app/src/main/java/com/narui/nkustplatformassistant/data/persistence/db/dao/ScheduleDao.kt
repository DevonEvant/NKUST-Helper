package com.narui.nkustplatformassistant.data.persistence.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.narui.nkustplatformassistant.data.persistence.db.entity.ScheduleEntity

@Dao
interface ScheduleDao {
    @Query("SELECT * FROM schedule_table")
    suspend fun getAllSchedule(): List<ScheduleEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSchedule(scheduleEntity: ScheduleEntity)

    @Query("")
    suspend fun insertMultiSchedule(scheduleList: List<ScheduleEntity>) {
        scheduleList.forEach { eachSchedule: ScheduleEntity ->
            insertSchedule(eachSchedule)
        }
    }

    @Query("SELECT COUNT(*) FROM schedule_table")
    fun sizeOfDB(): Int

    @Query("")
    fun isExist(): Boolean {
        val dbSize = sizeOfDB()
        return dbSize > 1
    }

    @Query("DELETE FROM schedule_table")
    suspend fun emptyCalenderTable()
}