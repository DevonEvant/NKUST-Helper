package com.example.nkustplatformassistant.data

object NKUST_ROUTES {
    val WEBAP_BASE = "http://webap.nkust.edu.tw"
    val WEBAP_HOME = "$WEBAP_BASE/nkust/index_main.html"
    val WEBAP_PERCHK = "$WEBAP_BASE/nkust/perchk.jsp"
    val WEBAP_FNC = "$WEBAP_BASE/nkust/fnc.jsp"
//    val WEBAP_FNC = "http://webap0.nkust.edu.tw/nkust/system/sys001_00.jsp?spath=ag_pro/ag222.jsp?"
    val WEBAP_ETXT = "$WEBAP_BASE/nkust/validateCode.jsp"
    val WEBAP_ETXT_WITH_SYMBOL = "$WEBAP_ETXT?it=${Math.random()}"
    val WEDAP_LOGIN = "$WEBAP_BASE/nkust/perchk.jsp"
    val WEBAP_ENTRY_FRAME = "$WEBAP_BASE/nkust/f_index.html"


    val SCHOOL_TABLETIME = "$WEBAP_BASE/ag_pro/ag222.jsp"

    val WEBAP_HEAD = "$WEBAP_BASE/nkust/f_head.jsp"
    val WEBAP_LEFT_PANEL = "$WEBAP_BASE/nkust/f_left.jsp"
    val WEBAP_RIGHT_PANEL = "$WEBAP_BASE/nkust/f_right.jsp"
}