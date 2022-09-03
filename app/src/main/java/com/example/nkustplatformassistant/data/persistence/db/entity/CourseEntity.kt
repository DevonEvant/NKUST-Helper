package com.example.nkustplatformassistant.data.persistence.db.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "course_table")
data class Course(
    @NonNull @PrimaryKey @ColumnInfo val courseId: Int,
    @ColumnInfo val professor: String,
    @ColumnInfo val courseName: String,
    @ColumnInfo val className: String,
    @ColumnInfo val classLocation: String,
    @ColumnInfo val classTime: String,
    @ColumnInfo val classGroup: String,
    @ColumnInfo val credits: String,
    @ColumnInfo val teachingHours: String,
    @ColumnInfo val importance: Boolean,
)