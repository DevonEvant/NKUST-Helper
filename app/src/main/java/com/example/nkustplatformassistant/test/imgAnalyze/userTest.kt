@file:Suppress("SpellCheckingInspection")

package com.example.nkustplatformassistant.test.imgAnalyze

import com.example.nkustplatformassistant.data.NKUST_ROUTES
import com.example.nkustplatformassistant.data.remote.FetchData
import com.example.nkustplatformassistant.data.remote.NkustUser
import com.example.nkustplatformassistant.user
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
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

//            println(user.yearOfEnrollment())
            println(user.getYearsOfDropDownList())

        }
    }
}

