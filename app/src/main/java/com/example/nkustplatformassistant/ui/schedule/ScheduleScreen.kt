@file:Suppress("SpellCheckingInspection")

package com.example.nkustplatformassistant.ui.schedule

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.nkustplatformassistant.data.persistence.DataRepository


@Composable
fun ScheduleScreen(scheduleViewModel: ScheduleViewModel, nacController: NavController) {
    ScheduleContent(scheduleViewModel, nacController)
}

@Preview(showBackground = true)
@Composable
fun ScheduleScreenPreview() {
    ScheduleScreen(
        scheduleViewModel = ScheduleViewModel(DataRepository(LocalContext.current)),
        nacController = rememberNavController()
    )
}


