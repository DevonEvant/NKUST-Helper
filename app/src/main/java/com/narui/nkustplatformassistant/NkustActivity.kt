package com.narui.nkustplatformassistant

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.narui.nkustplatformassistant.data.persistence.DataRepository
import com.narui.nkustplatformassistant.navigation.Screen
import com.narui.nkustplatformassistant.ui.curriculum.CurriculumScreen
import com.narui.nkustplatformassistant.ui.curriculum.CurriculumViewModel
import com.narui.nkustplatformassistant.ui.home.HomeScreen
import com.narui.nkustplatformassistant.ui.home.HomeViewModel
import com.narui.nkustplatformassistant.ui.login.LoginParamsViewModel
import com.narui.nkustplatformassistant.ui.login.LoginScreen
import com.narui.nkustplatformassistant.ui.score.ScoreScreen
import com.narui.nkustplatformassistant.ui.score.ScoreViewModel
import com.narui.nkustplatformassistant.ui.theme.Nkust_platform_assistantTheme

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

                    // koin, hilt, ViewModelFactory
//                    val viewModel = viewModel()

                    composable(Screen.Home.route) {
                        HomeScreen(
                            viewModel(factory = HomeViewModelFactory(dataRepository, context)),
                            navController)
                    }
                    composable(Screen.Login.route) {
                        LoginScreen(
                            viewModel(factory = LoginParamsViewModelFactory(dataRepository)),
                            navController)
                    }
                    composable(Screen.Curriculum.route) {
                        CurriculumScreen(
                            viewModel(factory = CurriculumViewModelFactory(dataRepository)),
                            navController)
                    }
                    composable(Screen.Score.route) {
                        ScoreScreen(
                            viewModel(factory = ScoreViewModelFactory(dataRepository)),
                            navController)
                    }
                }
            }
        }
    }
}

// https://developer.android.com/jetpack/compose/interop/compose-in-existing-arch
// https://developer.android.com/training/dependency-injection/hilt-android

class HomeViewModelFactory(private val repository: DataRepository, val context: Context) :
    ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(repository, context) as T
    }
}

class LoginParamsViewModelFactory(private val repository: DataRepository) :
    ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LoginParamsViewModel(repository) as T
    }
}

class CurriculumViewModelFactory(private val repository: DataRepository) :
    ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CurriculumViewModel(repository) as T
    }
}

class ScoreViewModelFactory(private val repository: DataRepository) :
    ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ScoreViewModel(repository) as T
    }
}