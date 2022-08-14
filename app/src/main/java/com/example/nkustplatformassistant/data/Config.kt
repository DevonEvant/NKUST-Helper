package com.example.nkustplatformassistant.data

object NKUST_ROUTES {
    val WEBAP_BASE = "http://webap.nkust.edu.tw"
    val WEBAP_HOME = "$WEBAP_BASE/nkust/index_main.html"
    val WEBAP_LOGIN = "$WEBAP_BASE/nkust/perchk.jsp"
    val WEBAP_ETXT = "$WEBAP_BASE/nkust/validateCode.jsp"
    val WEBAP_ETXT_WITH_SYMBOL = "$WEBAP_ETXT?it=${Math.random()}"
    val WEBAP_ENTRY_FRAME = "$WEBAP_BASE/nkust/f_index.html"

    val SCHOOL_TABLETIME = "$WEBAP_BASE/ag_pro/ag222.jsp"

}