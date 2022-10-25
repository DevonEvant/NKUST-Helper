package com.narui.nkustplatformassistant.ui.schedule

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.provider.CalendarContract
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.twotone.ArrowBack
import androidx.compose.material.icons.twotone.EditCalendar
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
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
import com.narui.nkustplatformassistant.R
import com.narui.nkustplatformassistant.data.persistence.DataRepository
import com.narui.nkustplatformassistant.data.persistence.db.entity.ScheduleEntity
import com.narui.nkustplatformassistant.navigation.Screen
import kotlinx.datetime.*
import kotlinx.datetime.TimeZone
import java.util.*

private val currentTime = Clock.System.now().toLocalDateTime(TimeZone.of("UTC+8"))
private var strTitle: String = ""
private var startDate =
    LocalDateTime(currentTime.year, currentTime.month, currentTime.dayOfMonth, 8, 0)

private var endDate =
    LocalDateTime(currentTime.year, currentTime.month, currentTime.dayOfMonth, 8, 0)

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ScheduleContent(scheduleViewModel: ScheduleViewModel, navController: NavController) {
    val context = LocalContext.current

    val schedules by scheduleViewModel.schedules.observeAsState()
    val events = mutableListOf<KalendarEvent>()

    schedules?.forEach { eachSchedule: ScheduleEntity ->

        val startSplit = eachSchedule.startDate.split("/")
        val startDate: Pair<Int, Int> = Pair(startSplit[0].toInt(), startSplit[1].toInt())

        // endDate 用不到...
        //        val endSplit = eachSchedule.endDate?.split("/")

        //        val endDate: Pair<Int, Int>? = endSplit?.let { nonNullEndSplit: List<String> ->
        //            Pair(nonNullEndSplit[0].toInt(), nonNullEndSplit[1].toInt())
        //        }

        events.add(
            KalendarEvent(
                date = LocalDate(currentTime.year, startDate.first, startDate.second),
                eventName = eachSchedule.agency,
                eventDescription = eachSchedule.description)
        )
    }

    var eventDescription by remember { mutableStateOf(context.getString(R.string.schedule_noevent)) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        CalenderHeader(navController, context)

        Kalendar(kalendarType = KalendarType.Firey,
            kalendarEvents = events,
            onCurrentDayClick = { kalendarDay, kalendarEvents ->
                startDate = LocalDateTime(
                    currentTime.year,
                    kalendarDay.localDate.month,
                    kalendarDay.localDate.dayOfMonth, 8, 0)
                endDate = LocalDateTime(
                    currentTime.year,
                    kalendarDay.localDate.month,
                    kalendarDay.localDate.dayOfMonth, 9, 0)

                strTitle = ""
                eventDescription = ""

                for (event in kalendarEvents) {
                    if (kalendarDay.localDate != event.date) {
                        strTitle = context.getString(R.string.schedule_noevent)
                        eventDescription = context.getString(R.string.schedule_noevent)
                    } else if (kalendarDay.localDate == event.date) {
                        strTitle = event.eventDescription.toString()
                        eventDescription = event.eventDescription.toString()
                        break
                    }
                }
            })
        Column(modifier = Modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally) {

            Text(text = eventDescription, textAlign = TextAlign.Center)
            Text(stringResource(R.string.schedule_declaration))
        }

    }
}

@Composable
fun CalenderHeader(navController: NavController, context: Context) {
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
            text = stringResource(R.string.schedule_title),
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        Row(
            modifier = Modifier.weight(1F),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // https://developer.android.com/guide/topics/providers/calendar-provider#intent-insert
            fun calculateCalendarMillis(dateTime: LocalDateTime): Long {
                return Calendar.getInstance().run {
                    set(dateTime.year,
                        dateTime.month.number,
                        dateTime.dayOfMonth,
                        dateTime.hour,
                        dateTime.minute)
                    timeInMillis
                }
            }

            val intent = Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.Events.TITLE, strTitle)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,
                    calculateCalendarMillis(startDate))
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, calculateCalendarMillis(endDate))
                .putExtra(CalendarContract.Events.AVAILABILITY,
                    CalendarContract.Events.AVAILABILITY_BUSY)


            fun checkAndStartIntent(intent: Intent) {
                if (intent.resolveActivity(context.packageManager) != null) {
                    context.startActivity(intent)
                } else {
                    Toast.makeText(context, context.getString(R.string.schedule_no_calendar_app), Toast.LENGTH_SHORT).show()
                }
            }

            Row(modifier = Modifier.clickable { checkAndStartIntent(intent) },
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically) {

                Text(stringResource(R.string.schedule_add_to_calendar))
                Icon(imageVector = Icons.TwoTone.EditCalendar, contentDescription = null)
            }
        }
    }
}

@Composable
fun FileCard() {
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

