package com.example.nkustplatformassistant


import android.graphics.BitmapFactory
import android.util.Log
import com.example.nkustplatformassistant.data.NKUST_ROUTES
import com.example.nkustplatformassistant.data.remote.NkustUser
import org.junit.Test
//import android.content.res.Resources


import org.junit.Assert.*
import kotlinx.coroutines.*


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    @Test
    fun addition_isCorrect() {
        val u = NkustUser()

        runBlocking {
            u.getWebapEtxtImg()
            println(">>>>>>>>>>>>>>>>>>Enter text: ")
            val stringInput = "ABCD"

            u.loginWebap("C110152351","c110ankust",stringInput)
            println(u.checkLoginValid().toString())
        }


    }

    @Test
    fun a() {
        println(NKUST_ROUTES.WEDAP_ETXT_WITH_SYMBOL)
    }

//    https://webap.nkust.edu.tw/nkust/validateCode.jsp/?it=1.3759458507944256
//    https://webap.nkust.edu.tw/nkust/validateCode.jsp?it=0.26295017141077903
}