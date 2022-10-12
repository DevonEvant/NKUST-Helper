package com.example.nkustplatformassistant.data.persistence.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "score_other_table")
data class ScoreOtherEntity(
    @ColumnInfo val year: String,
    @ColumnInfo val semester: String,
    @PrimaryKey @ColumnInfo val semDescription: String,
    @ColumnInfo val behaviorScore: String,
    @ColumnInfo val average: String,
    @ColumnInfo val classRanking: Int?,
    @ColumnInfo val classPeople: Int?,
    @ColumnInfo val deptRanking: Int?,
    @ColumnInfo val deptPeople: Int?,
)