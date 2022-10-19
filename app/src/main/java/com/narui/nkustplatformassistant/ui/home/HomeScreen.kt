package com.narui.nkustplatformassistant.ui.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.narui.nkustplatformassistant.data.persistence.DataRepository
import com.narui.nkustplatformassistant.ui.theme.Nkust_platform_assistantTheme

@Composable
fun HomeScreen(homeViewModel: HomeViewModel, navController: NavController) {
    HomeScreenBase(homeViewModel, navController)
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    val context = LocalContext.current
    Nkust_platform_assistantTheme {
        HomeScreen(
            HomeViewModel(DataRepository(context), context),
            rememberNavController()
        )
    }
}