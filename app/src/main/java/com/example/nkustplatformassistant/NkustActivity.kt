package com.example.nkustplatformassistant

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NkustActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dataRepository = DataRepository(applicationContext)

        setContent {

            val navController = rememberNavController()

            Nkust_platform_assistantTheme {
                NavHost(
                    navController = navController,
                    startDestination = Screen.Home.route
                ) {
                    composable(Screen.Home.route) {
                        HomeScreen(HomeViewModel(dataRepository), navController)
                    }
                    composable(Screen.Login.route) {
                        LoginScreen(LoginParamsViewModel(dataRepository),navController)
                    }
                }
            }
        }
    }
}