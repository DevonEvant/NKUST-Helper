package com.example.nkustplatformassistant

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.nkustplatformassistant.data.persistence.DataRepository
import com.example.nkustplatformassistant.navigation.Screen
import com.example.nkustplatformassistant.ui.home.HomeScreen
import com.example.nkustplatformassistant.ui.home.HomeViewModel
import com.example.nkustplatformassistant.ui.login.LoginParamsViewModel
import com.example.nkustplatformassistant.ui.login.LoginScreen
import com.example.nkustplatformassistant.ui.theme.Nkust_platform_assistantTheme

// TODO(1. check network, 2. launch repository call when login state is initiated)

class NkustActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dataRepository = DataRepository.getInstance(applicationContext)

        val dataIsReady = NkustViewModel(dataRepository).dataAvailability
        setContent {

            val navController = rememberNavController()

            Nkust_platform_assistantTheme {
                NavHost(
                    navController = navController,
                    startDestination =
                        if (dataIsReady.value!!) Screen.Home.route
                        else Screen.Login.route
                ) {
                    composable(Screen.Home.route) {
                        HomeScreen(HomeViewModel(dataRepository), navController)
                    }
                    composable(Screen.Login.route) {
                        LoginScreen(LoginParamsViewModel(dataRepository), navController)
                    }
                }
            }
        }
    }
}