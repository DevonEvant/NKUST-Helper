package com.example.nkustplatformassistant.data.persistence.db.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "score_table")
class Score(
    @PrimaryKey(autoGenerate = true) val id: Int? = 0,
    @NonNull @ColumnInfo val subjectName: String,
    @ColumnInfo val midScore: String,
    @ColumnInfo val finalScore: String,
)
