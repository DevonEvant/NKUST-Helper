package com.narui.nkustplatformassistant

import com.narui.nkustplatformassistant.data.NkustEvent
import com.narui.nkustplatformassistant.data.persistence.db.entity.ScheduleEntity
import com.narui.nkustplatformassistant.data.remote.NkustAccessor
import com.narui.nkustplatformassistant.data.remote.NkustClient
import kotlinx.coroutines.runBlocking

fun main() {
    val client = NkustClient.getInstance()
    val accessor = NkustAccessor(client)

    fun getDropDown(dst: String): List<List<String>> {
        val listToGet = mutableListOf<List<String>>()
        runBlocking {
            accessor.getYearsOfDropDownListByMap(dst)
                .forEach { (yearSemester, Description) ->
                    yearSemester.split(",").let {
                        listToGet.add(listOf(it[0], it[1], Description))
                    }
                }
        }
        return listToGet
    }

//    runBlocking {
//        accessor.getAndSaveWebapEtxtImage()
//        print("Enter ETXT: ")
//        val etxt = readln()
//        println(accessor.loginWebap("C110152351", "c110ankust", etxt))
//
//
//        getDropDown("AG222").forEach {
//            try {
//                accessor.getSpecCurriculum(it[0], it[1], it[2])
//                    .forEach { eachCourse ->
//                        println(eachCourse)
//                    }
//            } catch (e: NoSuchElementException) {
//                // for allCourse.removeFirst()
//                println(
//                    "Error when getting yearly score: $e\n" +
//                            "It maybe no data, but it's okay"
//                )
//
//            } catch (e: IndexOutOfBoundsException) {
//                println(
//                    "Error when getting yearly score: $e\n" +
//                            "It maybe no data, but it's okay"
//                )
//            }
//        }
//
//        getDropDown("AG008").forEach {
//            try {
//                accessor.getYearlyScore(it[0], it[1]).forEach { score ->
//                    println(score)
//                }
//                println(accessor.getSemesterScoreOther(it[0], it[1], it[2]))
//
//            } catch (e: NoSuchElementException) {
//                // for allCourse.removeFirst()
//                println(
//                    "Error when getting yearly score: $e\n" +
//                            "It maybe no data, but it's okay"
//                )
//
//            } catch (e: IndexOutOfBoundsException) {
//                println(
//                    "Error when getting yearly score: $e\n" +
//                            "It maybe no data, but it's okay"
//                )
//            }
//        }
//    }
    runBlocking {
        accessor.getAndSaveWebapEtxtImage()
        print("Enter ETXT: ")
        val etxt = readln()
        println(accessor.loginWebap("C110152351", "c110ankust", etxt))

//        accessor.scheduleToGet().forEach { (year, semester) ->
//            accessor.parseNkustSchedule(accessor.getNkustScheduleCn(year, semester)).forEach {
//                println("${it.agency}, ${it.time}, ${it.description}")
//            }
//        }
        getCurrentYearSchedule(accessor)
    }
}

private suspend fun getCurrentYearSchedule(nkustAccessor: NkustAccessor) {
    val semesterToGet = nkustAccessor.scheduleToGet()
    val schedule = mutableListOf<ScheduleEntity>()

    semesterToGet.forEach { (year, semester) ->
        val scheduleTemp = nkustAccessor
            .parseNkustSchedule(nkustAccessor.getNkustScheduleCn(year, semester).inputStream())

        scheduleTemp.forEach { eachSchedule: NkustEvent ->
            val dateSet = eachSchedule.time.split("，")[0].split("-", "～")
            schedule.add(
                ScheduleEntity(
                    agency = eachSchedule.agency,
                    startDate = Regex("(\\d*/\\d*)").find(dateSet[0])!!.value,
                    endDate = if (dateSet.size > 1) dateSet[1] else null,
                    description = eachSchedule.time + " " + eachSchedule.description
                )
            )
        }
    }

    schedule.forEach { eachSchedule: ScheduleEntity ->
        println(
            "${eachSchedule.agency}, ${eachSchedule.startDate}, " +
                    "${eachSchedule.endDate}, ${eachSchedule.description}"
        )
    }
}
