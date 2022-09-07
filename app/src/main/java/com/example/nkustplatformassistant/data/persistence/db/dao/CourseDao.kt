package com.example.nkustplatformassistant.data.persistence.db.dao

import androidx.room.*
import com.example.nkustplatformassistant.data.DropDownParams
import com.example.nkustplatformassistant.data.persistence.db.converter.DataConverter
import com.example.nkustplatformassistant.data.persistence.db.entity.CourseEntity

@Dao
interface CourseDao {

    @Query("SELECT * FROM course_table WHERE year = :year AND semester = :semester")
    fun getSpecCourseList(year: Int, semester: Int): List<CourseEntity>

    @Query("SELECT DISTINCT year,semester,semDescription FROM course_table")
    fun getDropDownList(): List<DropDownParams>

    @Query("SELECT DISTINCT year,semester,semDescription FROM course_table ORDER BY semester,year LIMIT 1")
    fun getLatestCourseList(): List<DropDownParams>

    @Query("SELECT COUNT(*) FROM course_table")
    fun sizeOfDB(): Int

    @Query("")
    fun isExist(): Boolean {
        val dbSize = sizeOfDB()
        return dbSize > 1
    }

    @Query("SELECT * FROM COURSE_TABLE WHERE courseId = :courseId")
    suspend fun getCourseById(courseId: Int): CourseEntity

    @Query("")
    @TypeConverters(DataConverter::class)
    suspend fun getSpecCourseById(courseId: Int): CourseEntity {
        val course = getCourseById(courseId)
        return course.apply {
            this.courseTime = DataConverter().strToTime(this.classTime)
        }
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCourse(course: CourseEntity)

    @Query("DELETE FROM COURSE_TABLE WHERE courseId = :courseId")
    suspend fun deleteSpecificCourse(courseId: Int)

    @Query("DELETE FROM course_table")
    suspend fun emptyCourseTable()
}