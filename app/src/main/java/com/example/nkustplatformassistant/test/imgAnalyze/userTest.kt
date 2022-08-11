@file:Suppress("SpellCheckingInspection")

package com.example.nkustplatformassistant.test.imgAnalyze

import com.example.nkustplatformassistant.data.remote.NkustUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

fun main() {

    val id = "C110152351"
    val pw = "c110ankust"

    val a = NkustUser()

    runBlocking {
        withContext(Dispatchers.IO) {
            // get an image in base directory
            a.getWebapEtxtImg()

            println("Enter etxtCode: ")
            val etxtCode: String = readln()
            println(etxtCode)

            print(a.loginWebap(id, pw, etxtCode))
        }
    }
}
