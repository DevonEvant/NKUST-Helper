package com.narui.nkustplatformassistant

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.web.*
import android.webkit.CookieManager
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import com.narui.nkustplatformassistant.data.remote.NKUST_ROUTES
import com.narui.nkustplatformassistant.ui.theme.Nkust_platform_assistantTheme
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.cookies.*
import io.ktor.http.*

class WebViewActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Nkust_platform_assistantTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background) {
                    Column {
                        Greeting("Android")
                        ViewWeb(url = "https://webap0.nkust.edu.tw/")
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun WebViewPreview() {
    Nkust_platform_assistantTheme {
        Column {
            Greeting("Android")
            ViewWeb(url = "https://webap0.nkust.edu.tw/")
        }

    }
}

@Composable
@SuppressLint("SetJavaScriptEnabled")
@OptIn(ExperimentalMaterial3Api::class)
fun ViewWeb(url: String) {
    // Status bar
    val state = rememberWebViewState(url = url)
    SmallTopAppBar(
        title = { Text(state.pageTitle.toString()) },
        navigationIcon = {
            IconButton(onClick = {
                TODO("chk cookie: if it can retrieve data from server")
            }) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = null
                )
            }
        },
//        actions = {
//            IconButton(onClick = { /* doSomething() */ }) {
//                Icon(
//                    imageVector = Icons.Filled.Favorite,
//                    contentDescription = "Localized description"
//                )
//            }
//        }
    )
    val loadingState = state.loadingState
    if (loadingState is LoadingState.Loading) {
        LinearProgressIndicator(
            color = Color.DarkGray,
            progress = loadingState.progress,
            modifier = Modifier.fillMaxWidth()
        )
    }

    // WebView Client method override
    val webClient = remember {
        object : AccompanistWebViewClient() {
            override fun onPageStarted(
                view: WebView?,
                url: String?,
                favicon: Bitmap?,
            ) {
                super.onPageStarted(view, url, favicon)
                Log.d("Accompanist WebView", "Page started loading for $url")

            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)

                if (url == NKUST_ROUTES.WEBAP_LOGIN) {
                    val cookies = CookieManager.getInstance().getCookie(url)?.let {
                        Log.v("onPageFinished", "Cookie received:" +
                                String(it.toByteArray(), charset("UTF-8")))
                        String(it.toByteArray(), charset("UTF-8"))
                            .split(";")
                            .toTypedArray()
                    }
                    if (cookies != null) {
                        for (item in cookies) {
                            Log.e("onPageFinished", item)
                        }
                    }
                }
            }
        }
    }

    // Start WebView
    WebView(
        state = state,
        modifier = Modifier.fillMaxWidth(),
        onCreated = { webView ->
            webView.settings.javaScriptEnabled = true
        },
        client = webClient
    )
}