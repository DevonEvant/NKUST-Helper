package com.narui.nkustplatformassistant.data.persistence.db.entity

import androidx.room.*
import com.narui.nkustplatformassistant.data.CourseTime

@Entity(tableName = "course_table")
data class CourseEntity(
    @PrimaryKey @ColumnInfo val courseId: Int,
    @ColumnInfo val year: Int,
    @ColumnInfo val semester: Int,
    @ColumnInfo val semDescription: String,
    @ColumnInfo val professor: String,
    @ColumnInfo val courseName: String,
    @ColumnInfo val className: String,
    @ColumnInfo val classLocation: String,
    @ColumnInfo val classTime: String,
    @ColumnInfo val classGroup: String,
    @ColumnInfo val credits: String,
    @ColumnInfo val teachingHours: String,
    @ColumnInfo val importance: Boolean,
) {
    @Ignore
    lateinit var courseTime: List<CourseTime>
}