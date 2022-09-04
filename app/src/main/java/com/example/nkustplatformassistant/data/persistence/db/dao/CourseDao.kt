package com.example.nkustplatformassistant.data.persistence.db.dao

import androidx.room.*
import com.example.nkustplatformassistant.data.persistence.db.converter.DataConverter
import com.example.nkustplatformassistant.data.persistence.db.entity.CourseEntity

@Dao
interface CourseDao {

    @Query("SELECT * FROM COURSE_TABLE WHERE courseId = :courseId")
    @TypeConverters(DataConverter::class)
    suspend fun getSpecCourseById(courseId: Int): CourseEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCourse(course: CourseEntity)

    @Query("DELETE FROM COURSE_TABLE WHERE courseId = :courseId")
    suspend fun deleteSpecificCourse(courseId: Int)

    @Query("DELETE FROM calender_table")
    suspend fun emptyCalenderTable()
}