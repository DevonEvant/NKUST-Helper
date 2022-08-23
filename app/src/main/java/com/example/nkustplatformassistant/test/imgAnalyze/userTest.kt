@file:Suppress("SpellCheckingInspection")

package com.example.nkustplatformassistant.test.imgAnalyze

import com.example.nkustplatformassistant.data.remote.FetchData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

fun main() {

    val id = "C110152351"
    val pw = "c110ankust"

    val user = FetchData()

    runBlocking {
        withContext(Dispatchers.IO) {
            // get an image in base directory
            user.getAndSaveWebapEtxtImage()

            println("Enter etxtCode: ")
            val etxtCode: String = readln()
            println(etxtCode)

            println("login state: ${user.loginWebap(id, pw, etxtCode)}")


            user.getYearlyScore(year = "110", semester = "1").let {
                it.forEach { (subjectName, midScore, finalScore) ->
                    println("Subject: $subjectName " +
                            "Mid-term Score: $midScore " +
                            "Final Score: $finalScore"
                    )
                }
            }

            user.getYearsOfDropDownListByMap().forEach { (key, value) ->
                println("$key: $value")
            }
        }
    }
}

