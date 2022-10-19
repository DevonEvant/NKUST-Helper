package com.narui.nkustplatformassistant.data.persistence.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "score_table")
data class ScoreEntity(
//    @PrimaryKey(autoGenerate = true) val id: Int? = 0,
    @ColumnInfo val year: Int,
    @ColumnInfo val semester: Int,
    @ColumnInfo val semDescription: String,
    @PrimaryKey @ColumnInfo val subjectName: String,
    @ColumnInfo val midScore: String,
    @ColumnInfo val finalScore: String,
)
