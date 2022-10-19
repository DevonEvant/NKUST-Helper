@file:Suppress("SpellCheckingInspection")

package com.narui.nkustplatformassistant.ui.schedule

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.narui.nkustplatformassistant.data.persistence.DataRepository


@Composable
fun ScheduleScreen(scheduleViewModel: ScheduleViewModel, navController: NavController) {
    ScheduleContent(scheduleViewModel, navController)
}

@Preview(showBackground = true)
@Composable
fun ScheduleScreenPreview() {
    ScheduleScreen(
        scheduleViewModel = ScheduleViewModel(DataRepository(LocalContext.current)),
        navController = rememberNavController()
    )
}


