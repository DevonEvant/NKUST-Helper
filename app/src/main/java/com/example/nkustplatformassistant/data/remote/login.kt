package com.example.nkustplatformassistant.data.remote

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.example.nkustplatformassistant.data.NKUST_ROUTES
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.cookies.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.util.*
import io.ktor.util.cio.*
import io.ktor.utils.io.*

import java.io.File

/**
 * About login to NKUST system.
 */
class NkustUser {
    var client = HttpClient(CIO) {
        install(HttpCookies) {
            storage = AcceptAllCookiesStorage()
        }
    }

    val session = suspend { client.cookies(NKUST_ROUTES.WEDAP_BASE) }
//    private val client = HttpClient(CIO) {
//        engine {
//            https {
//                trustManager = object: X509TrustManager {
//                    override fun checkClientTrusted(p0: Array<out X509Certificate>?, p1: String?) { }
//
//                    override fun checkServerTrusted(p0: Array<out X509Certificate>?, p1: String?) { }
//
//                    override fun getAcceptedIssuers(): Array<X509Certificate>? = null
//                }
//            }
//        }
//    }

    /**
     * Refresh Session if session unavailable.
     * if parameter [forced] is true, the session will be forced to refresh
     */
    suspend fun refreshSession(forced: Boolean = false): Unit {

        val doRefreshSession = suspend {
            client.get {
                url(NKUST_ROUTES.WEDAP_HOME)
            }
        }

        if (forced)
            doRefreshSession()
        else {
            if (!checkLoginValid())
                doRefreshSession()
        }
    }

    /**
     * Login to NKUST educational administration system (webap).
     * If you need etxt image, please call [getWebapEtxtImg] function
     */
    suspend fun loginWebap(
        uid: String,
        pwd: String,
        etxtCode: String,
    ): Boolean {


        val loginResponse = client.submitForm(
            url = NKUST_ROUTES.WEDAP_LOGIN,
            formParameters = Parameters.build {
                append("uid", uid)
                append("pwd", pwd)
                append("etxt_code", etxtCode)
            }
        )

        if (!loginResponse.status.isSuccess())
            throw Error("Fetch URL Error. HttpStateCode is ${loginResponse.status} ")

        if ("驗證碼錯誤" in loginResponse.bodyAsText())
            return false

        return true
    }

    /**
     * GET etxt image on NKUST educational administration system(webap).
     */
    @OptIn(InternalAPI::class)
    suspend fun getWebapEtxtImg(): File {
//        println("1==============================")

        val etxtRes = client.get {
            url(NKUST_ROUTES.WEDAP_ETXT_WITH_SYMBOL)
        }
        println("2==============================")

        if (!etxtRes.status.isSuccess())
            throw Error("Fetch URL Error. HttpStateCode is ${etxtRes.status} ")
        println("3==============================")

        val imgFile: File = File("./etxt.jpg")
        etxtRes.content.copyAndClose(imgFile.writeChannel())
        // val imgBitmap = BitmapFactory.decodeFile(imgFile.path)
        println("4==============================")

        return imgFile
    }

    /**
     * Get Image to ImageBitmap with same session
     */
    suspend fun getWebapEtxtBitmap(): ImageBitmap {
        val etxtRes = client.get(url = Url(NKUST_ROUTES.WEDAP_ETXT_WITH_SYMBOL))
        Log.v("getWebapEtxtBitmap", "Status: ${etxtRes.status}")

        if (!etxtRes.status.isSuccess())
            throw Error("Fetch URL Error. HttpStateCode is ${etxtRes.status} ")

        val imgByte = etxtRes.body<ByteArray>()

        val bitmapOption = BitmapFactory.Options().apply {
            this.inPreferredConfig = Bitmap.Config.ARGB_8888
        }
        val imgBitmap = BitmapFactory
            .decodeByteArray(imgByte, 0, imgByte.size, bitmapOption)
            .asImageBitmap()

        Log.v("getWebapEtxtBitmap",
            "Width: ${imgBitmap.width}, Height: ${imgBitmap.height}")

        return imgBitmap
    }


    /**
     * Login to NKUST educational administration system (mobile version).
     */
    suspend fun loginMobile(
        uid: String,
        pwd: String,
        etxtCode: String?,
    ): Result<List<String>> {
        throw Error("Sorry! this function is not completed.")
    }


    /**
     * Check login state
     */
    suspend fun checkLoginValid(): Boolean {
        val res = client.get(NKUST_ROUTES.WEDAP_ENTRY_FRAME)

        val isValid = res.bodyAsText().contains("NKUST")

        return isValid
    }

    suspend fun getEtxtText(): String {
        return ""
    }
}

const val ETXTTAG: String = "etxtPaser"
private fun etxtPaser(imgBitmap: Bitmap): IntArray {
//    val imgBitmap: Bitmap =
//        BitmapFactory.decodeResource(
//            getResources(),
//            R.drawable.validate_code
//        )

    Log.v(ETXTTAG, "=>> ${imgBitmap.getHeight()} ${imgBitmap.getWidth()}")

    val aWordSize = object {
        val height: Int = imgBitmap.getHeight()
        val width: Int = imgBitmap.getWidth() / 4
    }


    for (h in 0..(imgBitmap.height - 1)) {
        for (w in (0..imgBitmap.width - 1) step 4) {
            val color = imgBitmap.getPixel(w, h)
//                    val A: Int = color shr 24 and 0xff // or color >>> 24
            val R: Int = color shr 16 and 0xff
            val G: Int = color shr 8 and 0xff
            val B: Int = color and 0xff
            val X: Int = (R + B + G) / 3
//                    Log.e("=>>", "$X")
        }
    }

    val imgBuffer: IntArray = IntArray(aWordSize.height * aWordSize.width)

    val ans = IntArray(4)
    for (i in 0..3) {
        imgBitmap.getPixels(
            imgBuffer,
            0,
            aWordSize.width,
            aWordSize.width * i,
            0,
            aWordSize.width,
            aWordSize.height
        )
    }

//            if (i == 3)
//                return Bitmap.createBitmap(
//                    imgBuffer,
//                    aWordSize.width,
//                    aWordSize.height,
//                    Bitmap.Config.RGB_565
//                ).asImageBitmap()

//            imgBuffer  tensorflow ==> ans

//        Log.e("+>>", "${imgBuffer.size}")

    return ans
}

