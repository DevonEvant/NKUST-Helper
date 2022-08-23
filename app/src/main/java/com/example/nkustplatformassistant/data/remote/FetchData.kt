package com.example.nkustplatformassistant.data.remote

import com.example.nkustplatformassistant.data.NKUST_ROUTES
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.util.*
import io.ktor.util.cio.*
import io.ktor.utils.io.*
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.text.PDFTextStripper
import org.jsoup.Jsoup
import org.jsoup.parser.*
import java.io.File
import java.net.URI
import java.util.*


// get xpath in jsoup
// https://stackoverflow.com/questions/16335820/convert-xpath-to-jsoup-query

data class Subject(
    val subjectName: String,
    val midScore: String,
    val finalScore: String,
)

/**
 * Before using this section, you must logged in.
 */
class FetchData : NkustUser() {

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

                mapOfInfo.forEach { (k, v) ->
                    println("getFncJspInformationMap: $k:$v")
                }
                println()
                return mapOfInfo
            }
    }

    /**
     * Get payload of destination (Must be uppercase),
     * it'll return Map<String,String>.
     * Notice: first value of map is action, which the value
     * the website will auto submitted for, second value is spath,
     *  按鈕按下後表單submit用
     */
    suspend fun getDstJspBase(dst: String): Map<String, String> {
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
            println("action: http://webap.nkust.edu.tw/nkust/${
                parser.parseInput(content, urlFnc)
                    .selectXpath("//*[@id=\"thisform\"]")
                    .attr("action")
            }")

            val mapOfDstInfo = mapOf<String, String>(
                // TODO use parser only once
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

            mapOfDstInfo.forEach { (k, v) ->
                println("getFncJspInformationMap: $k:$v")
            }
            return mapOfDstInfo
        }
    }

    /**
     * return current year and semester as "year,semester" String
     */
    suspend fun getCurrCurriculum(): String {
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
     * but we can't guarantee that all value is useful,
     * because the designer put some useless value in it.
     */
    suspend fun getYearsOfDropDownListByMap(): Map<String, String> {
        // init...
        val startOfCurr = yearOfEnrollment()
        val dstBase: Map<String, String> = getDstJspBase("AG008")

        println(startOfCurr)

        val urlDst = dstBase.values.elementAt(0)

        client.post(url = Url(urlDst)) {
            url {
                // TODO: action added
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
     * By providing year and semester, you can get a list of [Subject]
     * consist of subjectName, Mid-term-exam Score, and Final-exam Score,
     * but we won't guarantee it's non-null.
     */
    suspend fun getYearlyScore(
        year: String,
        semester: String,
    ): List<Subject> {
        // temprory usage
        val dropdownList = getDstJspBase("AG008") // score page

        val scoreUrl = Url(NKUST_ROUTES.WEBAP_ENTRY_FRAME + "/../ag_pro/ag008.jsp")

        client.post(scoreUrl) {
            url {
                parameters.append("yms", "$year,$semester")
                parameters.append("spath", dropdownList["spath"]!!)
                parameters.append("arg01", year)
                parameters.append("arg02", semester)
            }
        }.bodyAsText().let {
            val subject = parser
                .parseInput(it, scoreUrl.toString())
                .selectXpath("(//td[@align='left'])")
                .eachText()

            val midScore = parser
                .parseInput(it, scoreUrl.toString())
                .selectXpath("//form[@name='thisform']/table[1]/tbody[1]/tr/td[7]")
                .eachText().drop(1)


            val finalScore = parser
                .parseInput(it, scoreUrl.toString())
                .selectXpath("//form[@name='thisform']/table[1]/tbody[1]/tr/td[8]")
                .eachText().drop(1)

            val subjectList = mutableListOf<Subject>()

            subject.forEachIndexed { index, _ ->
                subjectList.add(index, Subject(
                    subject[index],
                    midScore[index],
                    finalScore[index])
                )
            }

//          Debug...
//            for (item in subjectList) {
//                println("Subject: ${item.subjectName} " +
//                        "Mid-term Score: ${item.midScore} " +
//                        "Final Score: ${item.finalScore}"
//                )
//            }
            return subjectList
        }

    }


    //    https://acad.nkust.edu.tw/var/file/4/1004/img/273/cal110-1.pdf
//    https://acad.nkust.edu.tw/var/file/4/1004/img/273/cal110-2.pdf
//    https://acad.nkust.edu.tw/var/file/4/1004/img/273/cal111-1.pdf
//    https://acad.nkust.edu.tw/var/file/4/1004/img/273/cal111-2.pdf
    @OptIn(InternalAPI::class)
    suspend fun getNkustCnCalenderPdf(year: String, semester: String, path: String?): File {

        // todo not complete. unable to find valid certification path to requested target

        val res = client.get {
//            url(NKUST_ROUTES.getCnCalenarUrl(year, semester))
            url("http://acad.nkust.edu.tw/var/file/4/1004/img/273/cal110-2.pdf")
        }

        if (!res.status.isSuccess())
            throw Error("Fetch URL Error. HttpStateCode is ${res.status} ")

        val calenderPdf = File(path)
        res.content.copyAndClose(calenderPdf.writeChannel())

        return calenderPdf
    }
}

data class NkustEvent(
    val agency: String,
    val time: String,
    val description: String
)

enum class Agency {
    OfficeOfTheSecretariat,
    PersonnelOffice,
    CurriculumDivision,
    RegistrationDivision,
    StudentLearningCounselingDivision,
    OfficeOfStudent,
    OfficeOfGeneralAffairs,
    OfficeOfPhysicalEducation,
    CenterOfGeneralStudies,
    ForeignLanguageEducationCente
}

/**
 * paser Nkust Calender. give Nkust Calender pdf file. return all of eventlist in the pdf file
 */
fun paserNkustCalender(pdf: File): MutableList<NkustEvent> {
    val doc = PDDocument.load(pdf)
    val docTextSoup = PDFTextStripper().getText(doc).let {
//        it.dropLast(5)
        it.dropLast(it.indexOfLast { it == '1' })
        it
    }

    val nkustEvents = mutableListOf<NkustEvent>()

    Regex("[A○].+\\((\\S* ?\\S)+\\s").findAll(docTextSoup).forEach {
        val event = docTextSoup.substring(it.range)

        val agency = event.substring(0, event.indexOf('(')).let {
            it.replace("[A○E \\s]+".toRegex(), "")
        }
        val time = event.substring(event.indexOf('(') + 1, event.indexOf(')'))
        val description = event.substringAfter(')')

//        println("=>> $agency | $time | $description")

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









