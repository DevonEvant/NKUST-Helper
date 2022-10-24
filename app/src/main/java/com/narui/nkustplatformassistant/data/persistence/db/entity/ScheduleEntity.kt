package com.narui.nkustplatformassistant.data.persistence.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "schedule_table")
data class ScheduleEntity(
    @ColumnInfo(name = "agency") val agency: String,
    @ColumnInfo(name = "start_date") val startDate: String,
    @ColumnInfo(name = "end_date") val endDate: String?,
    @PrimaryKey @ColumnInfo(name = "description") val description: String,
)