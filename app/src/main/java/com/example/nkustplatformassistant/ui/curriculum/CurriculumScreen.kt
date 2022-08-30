@file:Suppress("SpellCheckingInspection")

package com.example.nkustplatformassistant.ui.curriculum

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.HorizontalScrollView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.nkustplatformassistant.data.remote.NkustUser
import com.example.nkustplatformassistant.ui.theme.Nkust_platform_assistantTheme
//import com.example.nkustplatformassistant.ViewModeliewModel
import kotlinx.coroutines.launch
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import com.example.nkustplatformassistant.ui.login.EtxtCodeViewModel
import com.example.nkustplatformassistant.ui.login.LoginParamsViewModel
import com.example.nkustplatformassistant.ui.login.LoginScreenBase

val user = NkustUser()

class CurriculumScreen : ComponentActivity() {
        lateinit var navController: NavHostController

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            // https://stackoverflow.com/questions/68971231/activity-view-model-in-jetpack-compose
            setContent() {
                val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
                    throw Error("No ViewModelStoreOwner was provided via LocalViewModelStoreOwner")
                }
                val curriculumViewModel: CurriculumViewModel =
                    viewModel(viewModelStoreOwner = viewModelStoreOwner)

                Nkust_platform_assistantTheme {

                    // navController As start destination
                    navController = rememberNavController()
//                NkustpaNavGraph(navController = navController)
                    // A surface container using the 'background' color from the theme
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        CurriculumContext(curriculumViewModel)
                    }
                }
            }
        }
    }






