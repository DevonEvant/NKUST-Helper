package com.example.nkustplatformassistant.data.remote

import android.graphics.Bitmap
import android.util.Log
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.cookies.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.util.*
import io.ktor.util.cio.*
import io.ktor.utils.io.*
import java.security.cert.X509Certificate
import java.util.Date
import javax.net.ssl.X509TrustManager
import javax.security.cert.CertificateExpiredException
import javax.security.cert.CertificateNotYetValidException

/**
 * Declare current data's state
 */


/**
 * About login to NKUST system.
 */
open class NkustUser {

    companion object {
        val client = HttpClient(CIO) {
            install(HttpCookies) {
                storage = AcceptAllCookiesStorage()
            }
//            engine {
//                https {
//                    // TODO: insecure network traffic for trusting all ssl
//
//                    trustManager = object : X509TrustManager {
//                        override fun checkClientTrusted(
//                            chain: Array<out X509Certificate>?,
//                            authType: String?,
//                        ) {
//                        }
//
//                        override fun checkServerTrusted(
//                            chain: Array<out X509Certificate>?,
//                            authType: String?,
//                        ) {
//                        }
//
//                        override fun getAcceptedIssuers(): Array<X509Certificate> {
//                            return emptyArray()
//                        }
//                    }
//                }
//            }
        }
    }


    /**
     * Refresh Session if session unavailable.
     * We'll check [checkLoginValid] first, if session expire (which return false),
     * it'll automatic refresh session, of course you will need to re-login.
     * If parameter [forced] is true, the session will be forced to refresh
     */
    suspend fun refreshSession(forced: Boolean = false): Unit {

        val doRefreshSession = suspend {
            client.get {
                url(NKUST_ROUTES.WEBAP_HOME)
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
     * Login to WEBAP, if you need etxt_code image for testing,
     * call [getAndSaveWebapEtxtImage].
     */
    suspend fun loginWebap(
        uid: String,
        pwd: String,
        etxtCode: String,
    ): Boolean {
        val url = NKUST_ROUTES.WEBAP_LOGIN
        client.post(
            url = Url(url)) {
            url {
                parameters.append("uid", uid)
                parameters.append("pwd", pwd)
                parameters.append("etxt_code", etxtCode)
            }
        }.let { httpResponse ->
            if (!httpResponse.status.isSuccess()) return false

            httpResponse.bodyAsText().let { perchkAsText ->
                return !perchkAsText.contains("alert('")
            }
        }
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

        val res = client.get(NKUST_ROUTES.WEBAP_HEAD)

        return res.bodyAsText().contains("NKUST")
    }

//    suspend fun getEtxtText(): String {
//        return ""
//    }


}

const val ETXTTAG: String = "etxtPaser"
private fun etxtPaser(imgBitmap: Bitmap): IntArray {
//    val imgBitmap: Bitmap =
//        BitmapFactory.decodeResource(
//            getResources(),
//            R.drawable.validate_code
//        )

    Log.v(ETXTTAG, "=>> ${imgBitmap.height} ${imgBitmap.width}")

    val aWordSize = object {
        val height: Int = imgBitmap.height
        val width: Int = imgBitmap.width / 4
    }


    for (h in 0 until imgBitmap.height) {
        for (w in (0 until imgBitmap.width) step 4) {
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

