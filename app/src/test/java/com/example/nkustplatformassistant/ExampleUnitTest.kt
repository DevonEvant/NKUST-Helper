package com.example.nkustplatformassistant


import com.example.nkustplatformassistant.data.remote.NKUST_ROUTES
import com.example.nkustplatformassistant.data.remote.NkustUser
import org.junit.Test
//import android.content.res.Resources


import kotlinx.coroutines.*


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    @Test
    fun addition_isCorrect() {
        val user = NkustUser()

        runBlocking {
            user.getWebapEtxtBitmap()
            println(">>>>>>>>>>>>>>>>>>Enter text: ")
            val stringInput = "ABCD"

            user.loginWebap("C110152351","c110ankust",stringInput)
            println(user.checkLoginValid().toString())
        }


    }

    @Test
    fun a() {
        println(NKUST_ROUTES.WEBAP_ETXT_WITH_SYMBOL)
    }

//    https://webap.nkust.edu.tw/nkust/validateCode.jsp/?it=1.3759458507944256
//    https://webap.nkust.edu.tw/nkust/validateCode.jsp?it=0.26295017141077903
}