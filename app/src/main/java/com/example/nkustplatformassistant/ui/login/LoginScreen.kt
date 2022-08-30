@file:Suppress("SpellCheckingInspection")

package com.example.nkustplatformassistant.ui.login

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.nkustplatformassistant.ui.theme.Nkust_platform_assistantTheme

class LoginScreen : ComponentActivity() {
    lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // https://stackoverflow.com/questions/68971231/activity-view-model-in-jetpack-compose
        setContent() {
            val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
                throw Error("No ViewModelStoreOwner was provided via LocalViewModelStoreOwner")
            }
            val loginParamsViewModel: LoginParamsViewModel =
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
                    LoginScreenBase(loginParamsViewModel)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        throw Error("No ViewModelStoreOwner was provided via LocalViewModelStoreOwner")
    }
    val loginParamsViewModel: LoginParamsViewModel =
        viewModel(viewModelStoreOwner = viewModelStoreOwner)
    Nkust_platform_assistantTheme {
        LoginScreenBase(loginParamsViewModel)
    }
}

@Preview(showBackground = true)
@Composable
fun LoginWhateverPreview() {
    Nkust_platform_assistantTheme() {
        Column() {
            ShowDialogBase(showDialog = remember { mutableStateOf(true) })
        }
    }
}
