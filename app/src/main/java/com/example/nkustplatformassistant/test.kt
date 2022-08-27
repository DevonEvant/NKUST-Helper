package com.example.nkustplatformassistant

import com.example.nkustplatformassistant.data.remote.NkustUser
import com.example.nkustplatformassistant.data.remote.webapFncOption
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

fun main(){
    println("Enter id: ")
    val id = "C110152351"

    println("Enter pw: ")
    val pw = "c110ankust"

    val a = NkustUser()

    runBlocking{
        withContext(Dispatchers.IO){
            a.getWebapEtxtImg()

            println("Enter extext: ")
            val extext = readLine()!!
//            val extext = "1234"
            println(extext)

            print(a.loginWebap(id,pw,extext))

            val option =  webapFncOption()
            a.getWebapBusinessFunction(option)

        }}
}