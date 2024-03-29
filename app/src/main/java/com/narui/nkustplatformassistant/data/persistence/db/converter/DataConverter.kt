package com.narui.nkustplatformassistant.data.persistence.db.converter

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.narui.nkustplatformassistant.data.CourseTime
import com.narui.nkustplatformassistant.data.CurriculumTime
import com.narui.nkustplatformassistant.data.Weeks

@ProvidedTypeConverter
class DataConverter {
    @TypeConverter
    fun strToTime(stringToProcess: String): List<CourseTime> {
        // maybe: (一)A-B(二)4-5(三)6
        val weekTimeList: MutableList<List<String>> = mutableListOf()
        val weekTimeTemp: MutableList<List<String>> = mutableListOf()
        val finalData = mutableListOf<CourseTime>()

        stringToProcess
            .replace(" ", "")
            .split("(")
            .forEach { secondSplit -> // 一)A-B, 二)4-5
                weekTimeTemp.add(secondSplit.split(")")) // [一, A-B], [二, 4-5]
            }
        weekTimeTemp.removeFirst()

        weekTimeTemp.forEach { weekTime -> // [一, A-B]
            try {
                val timeSplit = weekTime[1].split("-")
                weekTimeList.add(
                    listOf(weekTime[0], timeSplit[0], timeSplit[1])
                )

            } catch (e: IndexOutOfBoundsException) {
                weekTimeList.add(
                    listOf(weekTime[0], weekTime[1], weekTime[1])
                )
            }
        }

        weekTimeList.forEach {
            finalData.add(
                CourseTime(
                    week = Weeks.getByShortCnCode(it[0].single()),
                    curriculumTimeRange = CurriculumTime.getById(it[1].single())!!
                            ..CurriculumTime.getById(it[2].single())!!
                )
            )
        }

        return finalData
    }


}