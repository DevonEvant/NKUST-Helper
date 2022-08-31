package com.example.nkustplatformassistant.data.remote

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.example.nkustplatformassistant.data.persistence.db.entity.Course
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
import org.jsoup.parser.Parser

import java.io.File
import java.lang.Exception

/**
 * Declare current data's state
 */


/**
 * About login to NKUST system.
 */
open class NkustUser {
    var client = HttpClient(CIO) {
        install(HttpCookies) {
            storage = AcceptAllCookiesStorage()
        }
    }

//    val session = suspend { client.cookies(NKUST_ROUTES.WEBAP_BASE) }

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
        val loginResponse = client.request(
            url = Url(NKUST_ROUTES.WEBAP_LOGIN)) {
            url {
                parameters.append("uid", uid)
                parameters.append("pwd", pwd)
                parameters.append("etxt_code", etxtCode)
            }
            method = HttpMethod.Post
        }

        if (!loginResponse.status.isSuccess())
            throw Error("Fetch URL Error. HttpStateCode is ${loginResponse.status} ")

        if (loginResponse.bodyAsText().contains("驗證碼錯誤"))
            return false

        return true
    }

    /**
     * Save etxt_code image on webap using GET.
     */
    @OptIn(InternalAPI::class)
    suspend fun getAndSaveWebapEtxtImage(): File {

        val etxtRes = client.get {
            url(NKUST_ROUTES.WEBAP_ETXT_WITH_SYMBOL)
        }

        if (!etxtRes.status.isSuccess())
            throw Error("Fetch URL Error. HttpStateCode is ${etxtRes.status} ")

        val imgFile: File = File("./etxt.jpg")
        etxtRes.content.copyAndClose(imgFile.writeChannel())

        return imgFile
    }

    /**
     * Get Image to ImageBitmap with same session
     */
    suspend fun getWebapEtxtBitmap(): ImageBitmap {
        val etxtRes = client.get(url = Url(NKUST_ROUTES.WEBAP_ETXT_WITH_SYMBOL))
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

        Log.v(
            "getWebapEtxtBitmap",
            "Width: ${imgBitmap.width}, Height: ${imgBitmap.height}"
        )

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

        val res = client.get(NKUST_ROUTES.WEBAP_HEAD)

        return res.bodyAsText().contains("NKUST")
    }

    suspend fun getEtxtText(): String {
        return ""
    }


    /**
     * get School Timetable
     */
    suspend fun getCurriculum(year: String, semester: String) {

        val url = NKUST_ROUTES.SCHOOL_TABLETIME

        val curriculumRes = client.request(
            url = Url(url)
        ) {
            url {
                parameters.append("spath", "ag_pro/ag222.jsp?")
                parameters.append("arg01", year)
                parameters.append("arg02", semester)
            }
            method = HttpMethod.Post
        }

        val body = curriculumRes.bodyAsText()

        if (!curriculumRes.status.isSuccess())
            throw Error("Fetch URL Error. HttpStateCode is ${curriculumRes.status} ")
        else if ("您請求的網址無法在此服務器上找到" in body)
            throw Error("Error! no timetable information found ")
        else if ("查無相關學年期課表資料" in body)
            throw Error("Error! no timetable information found ")
        else if ("學生目前無選課資料!" in body)
            throw Error("Error! no timetable information found ")

//        println(curriculumRes.bodyAsText())

        val parser: Parser = Parser.htmlParser()
        val courses = mutableListOf<Course>()

        body.let { content ->
            val a = parser.parseInput(content, url)
                .select("form tr").forEach {
//                    val course = Courses()
                    it.select("td").forEach {
                        println(it.text())
                    }

                }
//            courses.add(course)
        }



//        return isValid
    }
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

