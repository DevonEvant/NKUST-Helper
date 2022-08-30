package com.example.nkustplatformassistant.data.persistence.db.converter

import androidx.room.TypeConverter
import com.example.nkustplatformassistant.data.remote.Score

class DataConverter {
    @TypeConverter
    fun fromSubjectToDB(score: Score): List<String> {
        return listOf(
            score.subjectName,
            score.midScore,
            score.finalScore
        )
    }

    @TypeConverter
    fun fromDBToSubject(listSubject: List<String>): Score {
        return Score(
            listSubject[0],
            listSubject[1],
            listSubject[2]
        )
    }


}