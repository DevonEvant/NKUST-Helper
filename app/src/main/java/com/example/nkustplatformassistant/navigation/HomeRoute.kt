package com.example.nkustplatformassistant.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.*
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.rememberNavController
import com.example.nkustplatformassistant.ui.home.HomeScreen
import com.example.nkustplatformassistant.ui.login.LoginScreen


sealed class Screen(val route: String){
    object Login: Screen("login")
    object Home: Screen("home")
    object Curriculum: Screen("curriculum")
    object Schedule: Screen("schedule")
    object Score: Screen("score")
}

fun NavGraphBuilder.composable(
    route: String,
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList(),
    content: @Composable (NavBackStackEntry) -> Unit
) {
    addDestination(
        ComposeNavigator.Destination(provider[ComposeNavigator::class], content).apply {
            this.route = route
            arguments.forEach { (argumentName, argument) ->
                addArgument(argumentName, argument)
            }
            deepLinks.forEach { deepLink ->
                addDeepLink(deepLink)
            }
        }
    )
}
