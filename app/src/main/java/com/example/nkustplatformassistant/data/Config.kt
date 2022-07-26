package com.example.nkustplatformassistant.data

object NKUST_ROUTES {
    val WEDAP_BASE = "http://webap.nkust.edu.tw"
    val WEDAP_HOME = "$WEDAP_BASE/nkust/index_main.html"
    val WEDAP_LOGIN = "$WEDAP_BASE/nkust/perchk.jsp"
    val WEDAP_ETXT = "$WEDAP_BASE/nkust/validateCode.jsp"
    val WEDAP_ETXT_WITH_SYMBOL = "$WEDAP_ETXT?it=${Math.random()}"
    val WEDAP_ENTRY_FRAME = "$WEDAP_BASE/nkust/f_index.html"


}