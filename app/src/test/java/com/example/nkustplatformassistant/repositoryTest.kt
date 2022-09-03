package com.example.nkustplatformassistant

import com.example.nkustplatformassistant.data.remote.NkustAccessor
import kotlinx.coroutines.runBlocking


fun main() {
    System.setProperty("com.sun.security.enableAIAcaIssuers", "true");

    val accessor = NkustAccessor()

    runBlocking {
        accessor.getAndSaveWebapEtxtImage()
        println("Input Etxt:")
        val etxt = readLine()

        println("Login State: " +
                accessor.loginWebap("C110152351","c110ankust",etxt!!)
        )

//        accessor.getCurrYearAndSemester().split(",").let {
//            accessor.getSpecCurriculum(it[0],it[1])
//        }

            accessor.getSpecCurriculum("110","1").let {
                println(it.toString())
            }


    }
}