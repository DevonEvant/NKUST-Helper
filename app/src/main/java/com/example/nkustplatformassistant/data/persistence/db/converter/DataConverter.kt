package com.example.nkustplatformassistant.data.persistence.db.converter

import androidx.room.TypeConverter
import com.example.nkustplatformassistant.data.remote.Subject

class DataConverter {
    @TypeConverter
    fun fromSubjectToDB(subject: List<Subject>): List<List<String>> {
        val tempArr = mutableListOf<List<String>>()
        subject.forEach { (subjectName, midScore, finalScore) ->
            tempArr.add(listOf(subjectName, midScore, finalScore))
        }
        return tempArr
    }

//    @TypeConverter
//    fun fromDBToSubject(listSubject: List<String>): Subject {
//        return Subject(
//            listSubject[0],
//            listSubject[1],
//            listSubject[2]
//        )
//    }


}