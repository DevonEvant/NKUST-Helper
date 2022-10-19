@file:Suppress("SpellCheckingInspection")

package com.narui.nkustplatformassistant.ui.curriculum

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavController
import com.narui.nkustplatformassistant.data.persistence.DataRepository
import com.narui.nkustplatformassistant.ui.theme.Nkust_platform_assistantTheme

@Composable
fun CurriculumScreen(viewModel: CurriculumViewModel, navController: NavController) {
    CurriculumContent(viewModel, navController)
}

@Preview(showBackground = true)
@Composable
fun CurriculumScreenPreview() {
    val viewModel = CurriculumViewModel(DataRepository.getInstance(LocalContext.current))
    Nkust_platform_assistantTheme {
        CurriculumContent(
            viewModel,
            rememberNavController()
        )
    }
}