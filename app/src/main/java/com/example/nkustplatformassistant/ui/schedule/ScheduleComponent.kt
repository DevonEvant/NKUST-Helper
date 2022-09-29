package com.example.nkustplatformassistant.ui.schedule

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.himanshoe.kalendar.Kalendar
import com.himanshoe.kalendar.model.KalendarEvent
import com.himanshoe.kalendar.model.KalendarType
import kotlinx.datetime.LocalDate

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class,
    ExperimentalMaterialApi::class,
)
@Composable
fun ScheduleContent(scheduleViewModel: ScheduleViewModel, navController: NavController) {
    val context = LocalContext.current
    val schedules by scheduleViewModel.schedules.observeAsState()

    Column(modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally) {
        val events = listOf<KalendarEvent>(
            KalendarEvent(LocalDate(2022, 9, 29), "1", "1"),
            KalendarEvent(LocalDate(2022, 9, 30), "2", "2"),
            KalendarEvent(LocalDate(2022, 10, 1), "3", "3"),
            KalendarEvent(LocalDate(2022, 10, 2), "4", "4"),
            KalendarEvent(LocalDate(2022, 10, 3), "5", "5"),
        )

        var eventDescription by remember { mutableStateOf("no event") }
        Kalendar(kalendarType = KalendarType.Firey,
            kalendarEvents = events,
            onCurrentDayClick = { kalendarDay, kalendarEvents ->
                for (event in kalendarEvents) {
                    if (kalendarDay.localDate != event.date) {
                        eventDescription = "no event"
                    } else if (kalendarDay.localDate == event.date ) {
                        eventDescription =
                            kalendarDay.localDate.toString() + "\n" + event.eventDescription
                        break
                    }
                }
            })

        Text(eventDescription)


    }


//    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState()
////    GlobalScope.launch {
////        bottomSheetScaffoldState.bottomSheetState.expand()
////    }
//
//    val lazyListState = rememberLazyListState()
//
//    BottomSheetScaffold(
//        modifier = Modifier.fillMaxSize(),
//        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
//        scaffoldState = bottomSheetScaffoldState,
//        sheetContent = {
//            Column(
//                modifier = Modifier
//                    .padding(start = 16.dp, end = 16.dp, top = 16.dp),
//            )
//            {
//                Text("Today", fontSize = 32.sp)
//                Text("aaaaa", fontSize = 16.sp)
//                Text("aaaaa", fontSize = 16.sp)
//                Text("aaaaa", fontSize = 16.sp)
//                Text("aaaaa", fontSize = 16.sp)
//                Text("aaaaa", fontSize = 16.sp)
//            }
//        },
//    ) {
//        LazyColumn(
//            state = lazyListState,
//            modifier = Modifier
//                .fillMaxSize()
////                .padding(16.dp),
//        ) {
//            schedules?.forEach { index ->
//                stickyHeader(
//                ) {
//                    Text(text = "manufacturer")
//                }
//                item {
//                    ListItem(
//                        headlineText = { Text("123") },
//                        leadingContent = {
//                            Icon(
//                                Icons.Filled.Palette,
//                                modifier = Modifier.size(36.dp),
//                                contentDescription = null,
//                            )
//                        }
//                    )
//                }
//            }
//        }
//    }
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
    val scheduleViewModel: ScheduleViewModel =
        viewModel(viewModelStoreOwner = viewModelStoreOwner)
    ScheduleContent(scheduleViewModel, rememberNavController())

}

