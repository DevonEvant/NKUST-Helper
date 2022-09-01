package com.example.nkustplatformassistant.data.persistence.db.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "score_table")
data class ScoreEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = 0,
    @NonNull @ColumnInfo val year: Int,
    @NonNull @ColumnInfo val semester: Int,
    @NonNull @ColumnInfo val semDescription: String,
    @NonNull @ColumnInfo val subjectName: String,
    @ColumnInfo val midScore: String,
    @ColumnInfo val finalScore: String,
)

data class ScoreDropDownParams(
    val year: Int,
    val semester: Int,
    val semDescription: String,
)
