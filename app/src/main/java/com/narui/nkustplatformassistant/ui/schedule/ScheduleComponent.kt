package com.narui.nkustplatformassistant.ui.schedule

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.twotone.ArrowBack
import androidx.compose.material.icons.twotone.FileOpen
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.himanshoe.kalendar.Kalendar
import com.himanshoe.kalendar.model.KalendarEvent
import com.himanshoe.kalendar.model.KalendarType
import com.narui.nkustplatformassistant.data.persistence.DataRepository
import com.narui.nkustplatformassistant.navigation.Screen
import kotlinx.datetime.LocalDate

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ScheduleContent(scheduleViewModel: ScheduleViewModel, navController: NavController) {
    val context = LocalContext.current
    val schedules by scheduleViewModel.schedules.observeAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val events = listOf<KalendarEvent>(
//            KalendarEvent(LocalDate(2022, 9, 29), "1", "1"),
//            KalendarEvent(LocalDate(2022, 9, 30), "2", "2"),
            KalendarEvent(LocalDate(2022, 10, 10), "3", "3"),
//            KalendarEvent(LocalDate(2022, 10, 2), "4", "4"),
//            KalendarEvent(LocalDate(2022, 10, 3), "5", "5"),
        )
        CalenderHeader(navController)

        var eventDescription by remember { mutableStateOf("no event") }
        Kalendar(kalendarType = KalendarType.Firey,
            kalendarEvents = events,
            onCurrentDayClick = { kalendarDay, kalendarEvents ->
                for (event in kalendarEvents) {
                    if (kalendarDay.localDate != event.date) {
                        eventDescription = "no event"
                    } else if (kalendarDay.localDate == event.date) {
                        eventDescription =
                            kalendarDay.localDate.toString() + "\n" + event.eventDescription
                        break
                    }
                }
            })

        Text(eventDescription)
    }
}

@Composable
fun CalenderHeader(navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1F),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { navController.navigate(Screen.Home.route) }) {
                Icon(imageVector = Icons.TwoTone.ArrowBack, contentDescription = null)
            }
        }

        Text(
            modifier = Modifier.weight(1F),
            text = "Calendar",
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        Row(
            modifier = Modifier.weight(1F),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.TwoTone.FileOpen, contentDescription = null)
            }
        }
    }
}

@Composable
fun FileCard(){
    OutlinedCard(modifier = Modifier.padding(16.dp)) {

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleItem() {
    ListItem(
        headlineText = { Text("123") },
        leadingContent = {
            Icon(
                Icons.Filled.Palette,
                modifier = Modifier.size(36.dp),
                contentDescription = null,
            )
        }
    )

}

@Composable
@Preview(showBackground = true)
fun ScheduleItemPreview() {
    ScheduleItem()

}

@Composable
@Preview(showBackground = true)
fun SchedulePreview() {
    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        throw Error("No ViewModelStoreOwner was provided via LocalViewModelStoreOwner")
    }
    ScheduleContent(
        ScheduleViewModel(DataRepository(LocalContext.current)),
        rememberNavController()
    )

}

