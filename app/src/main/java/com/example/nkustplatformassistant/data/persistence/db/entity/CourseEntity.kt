package com.example.nkustplatformassistant.data.persistence.db.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.example.nkustplatformassistant.data.CourseTime

@Entity(tableName = "course_table")
data class CourseEntity(
    @NonNull @PrimaryKey @ColumnInfo val courseId: Int,
    @NonNull @ColumnInfo val year: Int,
    @NonNull @ColumnInfo val semester: Int,
    @ColumnInfo val professor: String,
    @ColumnInfo val courseName: String,
    @ColumnInfo val className: String,
    @ColumnInfo val classLocation: String,
    @ColumnInfo val classTime: String,
    @ColumnInfo val classGroup: String,
    @ColumnInfo val credits: String,
    @ColumnInfo val teachingHours: String,
    @ColumnInfo val importance: Boolean,
){
    @Ignore val courseTime: List<CourseTime> = listOf()
}