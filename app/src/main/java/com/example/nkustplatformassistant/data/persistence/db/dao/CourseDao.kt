package com.example.nkustplatformassistant.data.persistence.db.dao

import androidx.room.*
import com.example.nkustplatformassistant.data.persistence.db.entity.Calender
import com.example.nkustplatformassistant.data.persistence.db.entity.Course
import kotlinx.coroutines.flow.Flow

@Dao
interface CourseDao {

    @Query("SELECT professor ,courseName, className," +
            " classLocation, weekday, startTime, endTime FROM COURSE_TABLE")
    suspend fun getBasicClassData(courseId: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCourse(vararg course: Course)

    @Query("DELETE FROM COURSE_TABLE WHERE courseId = :courseId")
    suspend fun deleteSpecificCourse(courseId: Int)

    @Query("DELETE FROM calender_table")
    suspend fun emptyCalenderTable()
}