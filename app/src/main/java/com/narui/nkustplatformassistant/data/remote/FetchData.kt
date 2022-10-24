package com.narui.nkustplatformassistant.data.remote

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.narui.nkustplatformassistant.data.NkustEvent
import com.narui.nkustplatformassistant.data.Score
import com.narui.nkustplatformassistant.data.persistence.db.entity.CourseEntity
import com.narui.nkustplatformassistant.data.persistence.db.entity.ScoreOtherEntity
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.cookies.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.util.*
import io.ktor.util.cio.*
import io.ktor.utils.io.*
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper
import org.jsoup.parser.*
import java.io.File
import java.io.FileInputStream

// TODO Timeout handling

// get xpath in jsoup
// https://stackoverflow.com/questions/16335820/convert-xpath-to-jsoup-query


/**
 * Before using this section, you must logged in.
 */
class NkustAccessor(client: HttpClient) : NkustUser(client) {

    /**
     * Parser to parse HTML into a "org.jsoup.nodes.Document"
     * Class Document inherited org.jsoup.nodes.Element,
     * org.jsoup.nodes.Node
     */
    private val parser: Parser = Parser.htmlParser()

    suspend fun getFncJspBase(): Map<String, String> {
        val url = NKUST_ROUTES.WEBAP_LEFT_PANEL
        client.get(url = Url(url))
            .bodyAsText().let { content ->
                val mapOfInfo = mapOf<String, String>(
//                    "fncid" to parser.parseInput(content, url)
//                        .selectXpath("//*[@id=\"fncid\"]")
//                        .attr("value"),
                    "std_id" to parser.parseInput(content, url)
                        .selectXpath("//*[@id=\"std_id\"]")
                        .attr("value"),
                    "local_ip" to parser.parseInput(content, url)
                        .selectXpath("//*[@id=\"local_ip\"]")
                        .attr("value"),
                    "sysyear" to parser.parseInput(content, url)
                        .selectXpath("//*[@id=\"sysyear\"]")
                        .attr("value"),
                    "syssms" to parser.parseInput(content, url)
                        .selectXpath("//*[@id=\"syssms\"]")
                        .attr("value"),
                    "online" to parser.parseInput(content, url)
                        .selectXpath("//*[@id=\"online\"]")
                        .attr("value"),
                    "loginid" to parser.parseInput(content, url)
                        .selectXpath("//*[@id=\"loginid\"]")
                        .attr("value")
                )

                // Debug...
                // mapOfInfo.forEach { (k, v) ->
                //     println("getFncJspInformationMap: $k:$v")
                // }
                // println()
                return mapOfInfo
            }
    }

    /**
     * Get payload of destination,
     * @param dst must be uppercase
     * it'll return Map<String,String>.
     * Notice: first value of map is action, which the value
     * the website will auto submitted for, second value is spath,
     *  按鈕按下後表單submit用
     */
    private suspend fun getDstJspBase(dst: String): Map<String, String> {
//        val dstPre = dst.slice(IntRange(start = 0, endInclusive = 1))
//        val dstPost = dst.slice(IntRange(start = 2, endInclusive = 4))
//        val urlDst = NKUST_ROUTES.WEBAP_PERCHK + "/${dstPre}_pro/${dstPost}.jsp?"

        val fncBase = getFncJspBase()

        val urlFnc = NKUST_ROUTES.WEBAP_FNC

        client.post(url = Url(urlFnc)) {
            url {
                parameters.append("fncid", dst)
                fncBase.forEach { (k, v) ->
                    parameters.append(k, v)
                }
            }
        }.bodyAsText().let { content ->
            // testing print
            // println("action: http://webap.nkust.edu.tw/nkust/${
            //     parser.parseInput(content, urlFnc)
            //         .selectXpath("//*[@id=\"thisform\"]")
            //         .attr("action")
            // }")

            val mapOfDstInfo = mapOf<String, String>(
                "action" to "http://webap.nkust.edu.tw/nkust/${
                    parser.parseInput(content, urlFnc)
                        .selectXpath("//*[@id=\"thisform\"]")
                        .attr("action")
                }",
                "spath" to parser.parseInput(content, urlFnc)
                    .selectXpath("//*[@id=\"thisform\"]")
                    .attr("action")
                    .split("?", limit = 2)[1],
                "arg01" to parser.parseInput(content, urlFnc)
                    .selectXpath("//*[@id=\"arg01\"]")
                    .attr("value"),
                "arg02" to parser.parseInput(content, urlFnc)
                    .selectXpath("//*[@id=\"arg02\"]")
                    .attr("value"),
                "arg03" to parser.parseInput(content, urlFnc)
                    .selectXpath("//*[@id=\"arg03\"]")
                    .attr("value"),
                "arg04" to parser.parseInput(content, urlFnc)
                    .selectXpath("//*[@id=\"arg04\"]")
                    .attr("value"),
                "arg05" to parser.parseInput(content, urlFnc)
                    .selectXpath("//*[@id=\"arg05\"]")
                    .attr("value"),
                "arg06" to parser.parseInput(content, urlFnc)
                    .selectXpath("//*[@id=\"arg06\"]")
                    .attr("value"),
                "fncid" to parser.parseInput(content, urlFnc)
                    .selectXpath("//*[@id=\"fncid\"]")
                    .attr("value"),
                "uid" to parser.parseInput(content, urlFnc)
                    .selectXpath("//*[@id=\"uid\"]")
                    .attr("value"),
                "ls_randnum" to parser.parseInput(content, urlFnc)
                    .selectXpath("//*[@id=\"ls_randnum\"]")
                    .attr("value"),
            )

            // Debug...
            // mapOfDstInfo.forEach { (k, v) ->
            //     println("getFncJspInformationMap: $k:$v")
            // }
            return mapOfDstInfo

        }
    }

    /**
     * return current year and semester as "year,semester" String
     */
    suspend fun getCurrYearAndSemester(): String {
        client.get(url = Url(NKUST_ROUTES.WEBAP_LEFT_PANEL))
            .bodyAsText().let { content ->
                val sysyear = parser
                    .parseInput(content, NKUST_ROUTES.WEBAP_LEFT_PANEL)
                    .selectXpath("//*[@id=\"sysyear\"]").attr("name")
                val sysms = parser
                    .parseInput(content, NKUST_ROUTES.WEBAP_LEFT_PANEL)
                    .selectXpath("//*[@id=\"syssms\"]").attr("name")
                return "$sysyear,$sysms"
            }
    }

    suspend fun yearOfEnrollment(): String {
        val mapInput = getFncJspBase()

        return mapInput["loginid"]!!
            .slice(IntRange(start = 1, endInclusive = 3))
    }

    /**
     * Get Dropdown list as Map<String, String>.
     * @param dst must be uppercase
     * E.g "111,1", "111學年度第一學期"
     * but we can't guarantee that all value is useful,
     * because the designer put some useless value in it.
     */
    suspend fun getYearsOfDropDownListByMap(dst: String): Map<String, String> {
        // init...
        val startOfCurr = yearOfEnrollment()
        val dstBase: Map<String, String> = getDstJspBase(dst)

        println("startOfCurr: $startOfCurr")

        val urlDst = dstBase.values.elementAt(0)

        client.post(url = Url(urlDst)) {
            url {
                dstBase.forEach { (k, v) ->
                    if (k != "action" && k != "spath") {
                        parameters.append(k, v)
                    }
                }
            }
        }.bodyAsText().let { content ->
            // key (e.g. 110,1)
            val dropdownListValue: List<String> = parser
                .parseInput(content, urlDst)
                .select("select")
                .select("option")
                .eachAttr("value")

            // value (e.g. 110學年度第1學期)
            val dropdownListString: List<String> = parser
                .parseInput(content, urlDst)
                .select("select")
                .select("option")
                .eachText()

            val enrollment = yearOfEnrollment()
            val dropdownList = mutableMapOf<String, String>()

            dropdownListString.forEachIndexed { index, _ ->
                dropdownList[dropdownListValue[index]] = dropdownListString[index]
            }

            return dropdownList.let {
                val tempListMap = mutableMapOf<String, String>()
                it.forEach { (key, value) ->
                    if (key.split(",")[0].toInt() >= enrollment.toInt()) {
                        tempListMap[key] = value
                    }
                }
                tempListMap
            }
        }
    }

    /**
     * By providing year and semester, you can get a list of [Score]
     * consist of subjectName, Mid-term-exam Score, and Final-exam Score,
     * but we won't guarantee it's non-null.
     */
    suspend fun getYearlyScore(
        year: String,
        semester: String,
    ): List<Score> {
        // temprory usage
        val dropdownList = getDstJspBase("AG008") // score page

        val scoreUrl = NKUST_ROUTES.WEBAP_ENTRY_FRAME + "/../ag_pro/ag008.jsp"

        client.post(url = Url(scoreUrl)) {
            url {
                parameters.append("yms", "$year,$semester")
                parameters.append("spath", dropdownList["spath"]!!)
                parameters.append("arg01", year)
                parameters.append("arg02", semester)
            }
        }.bodyAsText().let {
            val subject = parser
                .parseInput(it, scoreUrl)
                .selectXpath("(//td[@align='left'])")
                .eachText()

            val midScore = parser
                .parseInput(it, scoreUrl)
                .selectXpath("//form[@name='thisform']/table[1]/tbody[1]/tr/td[7]")
                .eachText().drop(1)


            val finalScore = parser
                .parseInput(it, scoreUrl)
                .selectXpath("//form[@name='thisform']/table[1]/tbody[1]/tr/td[8]")
                .eachText().drop(1)

            val scoreList = mutableListOf<Score>()

            subject.forEachIndexed { index, _ ->
                scoreList.add(
                    Score(subject[index], midScore[index], finalScore[index])
                )
            }

//          Debug...
//            for (item in subjectList) {
//                println("Subject: ${item.subjectName} " +
//                        "Mid-term Score: ${item.midScore} " +
//                        "Final Score: ${item.finalScore}"
//                )
//            }
            return scoreList
        }
    }


    suspend fun getSemesterScoreOther(
        year: String,
        semester: String,
        semDescription: String,
    ): ScoreOtherEntity {
        val dropdownList = getDstJspBase("AG008") // score page

        val scoreUrl = NKUST_ROUTES.WEBAP_ENTRY_FRAME + "/../ag_pro/ag008.jsp"
        client.post(url = Url(scoreUrl)) {
            url {
                parameters.append("yms", "$year,$semester")
                parameters.append("spath", dropdownList["spath"]!!)
                parameters.append("arg01", year)
                parameters.append("arg02", semester)
            }
        }.bodyAsText().let {
            val stringToParse = parser
                .parseInput(it, scoreUrl)
                .selectXpath("//form[@action='./ag008_pdf.jsp']//div[1]")
                .text()

            /*
             * 操行成績：81.60
             * 總平均：85.86
             * 班名次/班人數：2/59
             * 系名次/系人數：9/160
             */

            /* when no data
             * 操行成績：0
             * 總平均：
             * 班名次/班人數：/
             * 系名次/系人數：/
             */

            val stringList = stringToParse.split("[\\^\\h]+".toRegex())
            // 班名次 != null

            try {

                val classRanking = stringList[2].split("：")[1].split("/")[0].let { classRanking ->
                    if (classRanking.isNotEmpty()) {
                        classRanking.toInt()
                    } else {
                        null
                    }
                }
                val classPeople = stringList[2].split("：")[1].split("/")[1].let { classPeople ->
                    if (classPeople.isNotEmpty()) {
                        classPeople.toInt()
                    } else {
                        null
                    }
                }
                val deptRanking = stringList[3].split("：")[1].split("/")[0].let { deptRanking ->
                    if (deptRanking.isNotEmpty()) {
                        deptRanking.toInt()
                    } else {
                        null
                    }
                }
                val deptPeople = stringList[3].split("：")[1].split("/")[1].let { deptPeople ->
                    if (deptPeople.isNotEmpty()) {
                        deptPeople.toInt()
                    } else {
                        null
                    }
                }

                return ScoreOtherEntity(
                    year = year,
                    semester = semester,
                    semDescription = semDescription,
                    behaviorScore = stringList[0].split("：")[1],
                    average = stringList[1].split("：")[1],
                    classRanking = classRanking,
                    classPeople = classPeople,
                    deptRanking = deptRanking,
                    deptPeople = deptPeople
                )
            } catch (e: IndexOutOfBoundsException) {
                println("$e is okay because there's no data yet.")

                return ScoreOtherEntity(
                    year = year,
                    semester = semester,
                    semDescription = semDescription,
                    behaviorScore = "",
                    average = "",
                    classRanking = null,
                    classPeople = null,
                    deptRanking = null,
                    deptPeople = null
                )
            }
        }
    }

    /**
     * @return a List<Pair<String, String>> (year, semester)
     */
    suspend fun scheduleToGet(): List<Pair<String, String>> {

        val url = NKUST_ROUTES.WEBAP_HEAD

        val response = client.get(url).bodyAsText()

        val currYear = parser.parseInput(response, url)
            .selectXpath("//div[@class='personal']//span[1]")
            .text()
            .substring(IntRange(start = 0, endInclusive = 2))

        return listOf(Pair(currYear, "1"), Pair(currYear, "2"))
    }

//   TODO: rename Calender to schedule
    /**
     * Providing an cache location, and it'll store pdf in there named
     * @param year
     * @param semester
     * @param mode when using android system, set this flag to true, and store file using context in another place
     * @param file providing a location of android cache
     */
    @OptIn(InternalAPI::class)
    suspend fun getNkustScheduleCn(
        year: String,
        semester: String,
        mode: Boolean = false,
        file: File = File("./", "${year}-${semester}.pdf"),
    ): File {
        // https://stackoverflow.com/questions/4690228/how-to-save-downloaded-files-in-cache-android
        // https://developer.android.com/training/connectivity/avoid-unoptimized-downloads
        val res = client.get {
            url(NKUST_ROUTES.getCnCalendarUrl(year, semester))
//            url("https://acad.nkust.edu.tw/var/file/4/1004/img/273/cal110-2.pdf")
        }

        if (!res.status.isSuccess())
            throw Error("Fetch URL Error. HttpStateCode is ${res.status} ")

//        val calenderPdf = File(path)
        if (!mode) {
            res.content.copyAndClose(file.writeChannel())
        }

        return file
    }


    /**
     * paser Nkust schedule. give Nkust schedule pdf file. return all of eventlist in the pdf file
     */
    fun parseNkustSchedule(pdfStream: FileInputStream): MutableList<NkustEvent> {
        val doc = PDDocument.load(pdfStream)
        val docTextSoup = PDFTextStripper().getText(doc).let {
//        it.dropLast(5)
            it.dropLast(it.indexOfLast { it == '1' })
            it
        }

        val nkustEvents = mutableListOf<NkustEvent>()

        Regex("[A○].+\\((\\S* ?\\S)+\\s").findAll(docTextSoup).forEach {
            val event = docTextSoup.substring(it.range)

            val agency =
                event.substring(0, event.indexOf('(')).replace("[A○E \\s]+".toRegex(), "")
            val time = event.substring(event.indexOf('(') + 1, event.indexOf(')'))
            val description = event.substringAfter(')')

            println("=>> $agency | $time | $description")

            nkustEvents.add(
                NkustEvent(
                    agency = agency,
                    time = time,
                    description = description
                )
            )
        }
        return nkustEvents
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

        if (!etxtRes.status.isSuccess())
            throw Error("Fetch URL Error. HttpStateCode is ${etxtRes.status} ")

        val imgByte = etxtRes.body<ByteArray>()

        val bitmapOption = BitmapFactory.Options().apply {
            this.inPreferredConfig = Bitmap.Config.ARGB_8888
        }
        val imgBitmap = BitmapFactory
            .decodeByteArray(imgByte, 0, imgByte.size, bitmapOption)
            .asImageBitmap()

        return imgBitmap
    }


    /**
     * Get specific semester curriculum
     * By providing year and semester, you can get a [listOf]<[Course]>
     */
    suspend fun getSpecCurriculum(
        year: String,
        semester: String,
        semDescription: String,
    ): List<CourseEntity> {
        val url = NKUST_ROUTES.SCHOOL_TABLETIME
        val curriculumRes = client.post(url = Url(url)) {
            url {
                parameters.append("yms", "$year,$semester")
                parameters.append("spath", "ag_pro/ag222.jsp?")
                parameters.append("arg01", year)
                parameters.append("arg02", semester)
            }
        }

        val body = curriculumRes.bodyAsText()


//        if (!curriculumRes.status.isSuccess())
//            throw Error("Fetch URL Error. HttpStateCode is ${curriculumRes.status} ")
//        else if ("您請求的網址無法在此服務器上找到" in body)
//            throw Error("Error! no timetable information found ")
//        else if ("查無相關學年期課表資料" in body)
//            throw Error("Error! no timetable information found ")
//        else if ("學生目前無選課資料!" in body)
//            throw Error("Error! no timetable information found ")

        val parser: Parser = Parser.htmlParser()
        val courseList = mutableListOf<CourseEntity>()

        body.let { content ->
            val allCourse = parser.parseInput(content, url)
                .select("form tr")
            allCourse.removeFirst()

            allCourse.forEach { processedAllCourses ->
                processedAllCourses.select("td").let { oneCourse ->
                    oneCourse.eachText().let { element: List<String> ->
                        courseList.add(
                            CourseEntity(
                                courseId = element[0].toInt(),
                                year = year.toInt(),
                                semester = semester.toInt(),
                                semDescription = semDescription,
                                courseName = element[1],
                                className = element[2],
                                classGroup = element[3],
                                credits = element[4],
                                teachingHours = element[5],
                                importance = element[6].contains("必修"),
                                classTime = element[8],
                                professor = element[9],
                                classLocation = element[10],
                            )
                        )
                    }

                }
            }
        }
        return courseList
    }
}





