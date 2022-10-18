package com.example.nkustplatformassistant

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.MutableLiveData
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.nkustplatformassistant.data.persistence.DataRepository
import com.example.nkustplatformassistant.navigation.Screen
import com.example.nkustplatformassistant.ui.curriculum.CurriculumScreen
import com.example.nkustplatformassistant.ui.curriculum.CurriculumViewModel
import com.example.nkustplatformassistant.ui.home.HomeScreen
import com.example.nkustplatformassistant.ui.home.HomeViewModel
import com.example.nkustplatformassistant.ui.login.LoginParamsViewModel
import com.example.nkustplatformassistant.ui.login.LoginScreen
import com.example.nkustplatformassistant.ui.score.ScoreScreen
import com.example.nkustplatformassistant.ui.score.ScoreViewModel
import com.example.nkustplatformassistant.ui.theme.Nkust_platform_assistantTheme
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase

// TODO(1. check network, 2. launch repository call when login state is initiated)

val dbDataAvailability = MutableLiveData(false)

class NkustActivity : ComponentActivity() {
    private lateinit var analytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Obtain the FirebaseAnalytics instance.
        analytics = Firebase.analytics
        val dataRepository = DataRepository.getInstance(applicationContext)

        setContent {
            val navController = rememberNavController()

            Nkust_platform_assistantTheme {
                val context = LocalContext.current
                NkustViewModel(dataRepository)

                dbDataAvailability.observeAsState(false).let {
                    dbDataAvailability.postValue(it.value)
                    if (it.value) navController.navigate(Screen.Home.route) {
                        popUpTo(0)
                    }
                }
                NavHost(
                    navController = navController,
                    startDestination = Screen.Login.route
                ) {

                    composable(Screen.Home.route) {
                        HomeScreen(HomeViewModel.getInstance(dataRepository, context), navController)
                    }
                    composable(Screen.Login.route) {
                        LoginScreen(LoginParamsViewModel(dataRepository), navController)
                    }
                    composable(Screen.Curriculum.route) {
                        CurriculumScreen(CurriculumViewModel(dataRepository), navController)
                    }
                    composable(Screen.Score.route) {
                        ScoreScreen(ScoreViewModel(dataRepository), navController)
                    }
                }
            }
        }
    }
}