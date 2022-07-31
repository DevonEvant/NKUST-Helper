package com.example.nkustplatformassistant

import android.R.color
import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.nkustplatformassistant.data.remote.NkustUser
import com.example.nkustplatformassistant.ui.theme.Nkust_platform_assistantTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.util.*

/**
 * Interface to the Login data layer.
 */
class MainActivity : ComponentActivity() {
    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Nkust_platform_assistantTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    val u = NkustUser()

                    lateinit var imgBitmap: Bitmap
                    val coroutineScope = rememberCoroutineScope()

                    val getimgBitmap: () -> Unit = {
                        coroutineScope.launch {
                            println("0==============================")

                            imgBitmap = BitmapFactory.decodeFile(u.getWebapEtxtImg().path)
                        }
                    }

                    getimgBitmap()


                    fun sent(etxt: String) {
                        if (etxt.length == 4)
                            coroutineScope.launch {
                                u.loginWebap("C110152351", "c110ankust", etxt)
                                println(u.checkLoginValid().toString())
                            }
                    }

                    var w: String by remember {
                        mutableStateOf("")
                    }

//                    Image(bitmap = imgBitmap!!.asImageBitmap(), contentDescription = "123")
                    TextField(value = w, onValueChange = {
                        w = it
                        sent(it)
                    })

                }
            }
        }
    }


}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Nkust_platform_assistantTheme {
//        Greeting("Android111111")
    }
}