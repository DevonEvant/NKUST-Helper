package com.example.nkustplatformassistant.data.remote

import com.example.nkustplatformassistant.data.NKUST_ROUTES
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import org.jsoup.nodes.FormElement
import org.jsoup.parser.*


// get xpath in jsoup
// https://stackoverflow.com/questions/16335820/convert-xpath-to-jsoup-query

/**
 * Before using this section, you must logged in.
 */
class FetchData : NkustUser() {
    // TODO: 1. get 入學年分 2. get dropdown list and pop curryear < 入學年分

    /**
     * Parser to parse HTML into a "org.jsoup.nodes.Document"
     * Class Document inherited org.jsoup.nodes.Element,
     * org.jsoup.nodes.Node
     */
    val parser: Parser = Parser.htmlParser()

//    suspend fun showSomeData() = coroutineScope {
//        val data = async(Dispatchers.IO) { // <- extension on current scope
//            ... load some Contacts.Intents.UI data for the Main thread ...
//        }
//
//        withContext(Dispatchers.Main) {
//            doSomeWork()
//            val result = data.await()
//            display(result)
//        }
//    }

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
     * the website will auto submitted for.
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
                "action" to "http://webap.nkust.edu.tw/nkust/${
                    parser.parseInput(content, urlFnc)
                        .selectXpath("//*[@id=\"thisform\"]")
                        .attr("action")
                }",
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
     * because the design of the owner put some useless value in it.
     */
    suspend fun getYearsOfDropDownList(): Map<String, String> {
        // init...
        val startOfCurr = yearOfEnrollment()
        val dstBase: Map<String, String> = getDstJspBase("AG008")

        println(startOfCurr)

        val urlDst = dstBase.values.elementAt(0)

        client.post(url = Url(urlDst)) {
            url {
                // TODO: action added
                dstBase.forEach { (k, v) ->
                    if (k != "action") {
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

            val dropdownList = mutableMapOf<String, String>()
            dropdownListString.forEachIndexed { index, _ ->
                dropdownList[dropdownListValue[index]] = dropdownListString[index]
            }
            return dropdownList
        }
    }
}




