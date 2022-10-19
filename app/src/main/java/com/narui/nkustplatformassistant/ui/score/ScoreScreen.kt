@file:Suppress("SpellCheckingInspection")

package com.narui.nkustplatformassistant.ui.score

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.twotone.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import androidx.compose.material.icons.*
import androidx.navigation.NavController
import com.narui.nkustplatformassistant.data.persistence.DataRepository
import com.narui.nkustplatformassistant.ui.theme.Nkust_platform_assistantTheme

@Composable
fun ScoreScreen(viewModel: ScoreViewModel, navController: NavController) {
    ScoreContent(viewModel, navController)
}

@Preview(showBackground = true)
@Composable
fun ScoreScreenPreview() {
    val viewModel = ScoreViewModel(DataRepository.getInstance(LocalContext.current))
    Nkust_platform_assistantTheme {
        ScoreContent(
            viewModel,
            rememberNavController()
        )
    }
}
