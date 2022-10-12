package com.example.nkustplatformassistant

import com.example.nkustplatformassistant.data.remote.NkustAccessor
import com.example.nkustplatformassistant.data.remote.NkustClient
import io.ktor.client.*
import kotlinx.coroutines.runBlocking
import kotlin.reflect.full.memberProperties


//fun main() {
//    System.setProperty("com.sun.security.enableAIAcaIssuers", "true");
//
//    val accessor = NkustAccessor(NkustClient.getInstance())
//
//    runBlocking {
//        accessor.getAndSaveWebapEtxtImage()
//        println("Input Etxt:")
//        val etxt = readLine()
//
//        println("Login State: " +
//                accessor.loginWebap("C110152351", "c110ankust", etxt!!)
//        )
//
////        accessor.getSpecCurriculum("110", "1", "").let {
////            println(it.toString())
////        }
//
//        accessor.getSemesterScoreOther("110","1","d").let {
//            for (item in it::class.members){
//                println(item)
//            }
//        }
//
//
//    }
//}