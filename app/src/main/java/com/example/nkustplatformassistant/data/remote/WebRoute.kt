package com.example.nkustplatformassistant.data.remote

object NKUST_ROUTES {
    val WEBAP_BASE = "http://webap.nkust.edu.tw"
    val WEBAP_HOME = "$WEBAP_BASE/nkust/index_main.html"
    val WEBAP_LOGIN = "$WEBAP_BASE/nkust/perchk.jsp"
    val WEBAP_ETXT = "$WEBAP_BASE/nkust/validateCode.jsp"
    val WEBAP_ETXT_WITH_SYMBOL = "$WEBAP_ETXT?it=${Math.random()}"
    val WEBAP_ENTRY_FRAME = "$WEBAP_BASE/nkust/f_index.html"
    val WEBAP_HEAD = "$WEBAP_BASE/nkust/f_head.jsp"
    val WEBAP_LEFT_PANEL = "$WEBAP_BASE/nkust/f_left.jsp"
    val WEBAP_RIGHT_PANEL = "$WEBAP_BASE/nkust/f_right.jsp"
    val SCHOOL_TABLETIME = "$WEBAP_BASE/nkust/ag_pro/ag222.jsp"
    val WEBAP_FNC = "$WEBAP_BASE/nkust/fnc.jsp"
    val ALL_PDF_ID_PAGE = "https://acad.nkust.edu.tw/p/412-1004-1588.php?Lang=zh-tw"

    /**
     * return calendar Url
     * [year] , if year.lenght == 4, [year] is going to treated as AD . if not, [year] is going to treated as ROC era
     * [semester] , semester must is "1" or "2"
     */
    fun getCnCalendarUrl(year: String, semester: String): String {
        lateinit var _year: String
        lateinit var _semester: String

        if (year.length == 4)
            _year = (year.toInt() - 1911).toString()
        else if (year.length == 3)
            _year = year
        else if (year.length == 2)
            _year = "0" + year
        else
            throw Error("illegal parameter -> 'year'")

        if (!(semester == "1" || semester == "2"))
            throw Error("illegal parameter -> 'semester' must is '1' or '2'")

        _semester = semester
        return "https://acad.nkust.edu.tw/var/file/4/1004/img/273/cal$_year-$_semester.pdf"
    }
}