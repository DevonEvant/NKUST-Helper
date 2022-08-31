package com.example.nkustplatformassistant

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.nkustplatformassistant.navigation.Screen
import com.example.nkustplatformassistant.ui.home.HomeScreen
import com.example.nkustplatformassistant.ui.login.LoginParamsViewModel
import com.example.nkustplatformassistant.ui.login.LoginScreen
import com.example.nkustplatformassistant.ui.theme.Nkust_platform_assistantTheme

class NkustActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            Nkust_platform_assistantTheme {
                NavHost(
                    navController = navController,
                    startDestination = Screen.Home.route
                ) {
                    composable(Screen.Home.route) {
                        fun btnOnClick() {
                            navController.navigate(Screen.Login.route)
                        }
                        HomeScreen(name = "Brad") { btnOnClick() }
                    }
                    composable(Screen.Login.route) {
                        val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
                            throw Error("No ViewModelStoreOwner was provided via LocalViewModelStoreOwner")
                        }

                        val loginParamsViewModel: LoginParamsViewModel =
                            viewModel(viewModelStoreOwner = viewModelStoreOwner)

                        LoginScreen(viewmodel = loginParamsViewModel)
                    }
                }
            }
        }
    }
}