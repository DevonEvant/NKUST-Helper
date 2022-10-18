package com.example.nkustplatformassistant.ui.home

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.twotone.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.nkustplatformassistant.R
import com.example.nkustplatformassistant.data.DropDownParams
import com.example.nkustplatformassistant.data.persistence.db.entity.CourseEntity
import com.example.nkustplatformassistant.data.persistence.db.entity.ScoreOtherEntity
import com.example.nkustplatformassistant.dbDataAvailability
import com.example.nkustplatformassistant.navigation.Screen
import com.example.nkustplatformassistant.ui.curriculum.AutosizeText
import com.example.nkustplatformassistant.ui.curriculum.SemesterSelector
import com.example.nkustplatformassistant.ui.score.ScoreOtherWidget
import java.time.Duration

const val defaultTime = 15L

@Composable
fun HomeScreenBase(homeViewModel: HomeViewModel, navController: NavController) {
    val showBackCard = remember { mutableStateOf(false) }
    HomeBase(homeViewModel, navController)
    if (showBackCard.value) {
        BackCard(showBackCard = showBackCard)
    }
    BackHandler(enabled = true) {
        showBackCard.value = true
    }
}

@Composable
fun BackCard(showBackCard: MutableState<Boolean>) {
    val activity = LocalContext.current as Activity
    Dialog(onDismissRequest = { showBackCard.value = false }) {
        Card(modifier = Modifier.padding(20.dp)) {
            Column(
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(stringResource(R.string.home_leaving_message))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(onClick = { showBackCard.value = false }) {
                        Text(stringResource(R.string.no))
                    }
                    Button(onClick = {
                        showBackCard.value = false
                        activity.finish()
                    }) {
                        Text(stringResource(R.string.yes))
                    }
                }
            }
        }
    }
}

@Composable
fun HomeBase(
    homeViewModel: HomeViewModel,
    navController: NavController,
) {
    val context = LocalContext.current

    val displayContent = remember { mutableStateOf(false) }
    println("Database Availability: ${dbDataAvailability.value}")


    dbDataAvailability.observeAsState(false).value.let { dbDataAvailability ->
        if (dbDataAvailability) {
            displayContent.value = true

        } else {

            val fetchingDetails = homeViewModel.fetchingDetails
                .observeAsState(stringResource(R.string.home_viewmodel_waitingresource)).value
            ShowProgress(
                fetchingDetails = fetchingDetails,
                startfetch = { homeViewModel.startFetch(true, context) }
            )
        }
    }

    if (displayContent.value) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Text(
                    stringResource(R.string.home_recenthighlight_text),
                    fontWeight = FontWeight.Bold,
                    fontSize = 44.sp,
                    lineHeight = 40.sp
                )

                WelcomeCard(navController = navController, homeViewModel = homeViewModel)

                var recentCourse by remember {
                    mutableStateOf(
                        CourseEntity(
                            -1, -1, -1, "",
                            "", "", "", "", "",
                            "", "", "", false
                        )
                    )
                }

                // Text Color: https://m3.material.io/styles/color/dynamic-color/overview

                homeViewModel.recentCourse.observeAsState(recentCourse).value.let {
                    recentCourse = it
                }

                val todayCourse by homeViewModel.todayCourse.observeAsState()
                LaunchedEffect(todayCourse) {
                    todayCourse?.let {
                        homeViewModel.getRecentCourse(defaultTime)
                    }
                }

                SubjectCard(
                    recentCourse = recentCourse,
                    minuteBefore = homeViewModel.minuteBefore
                        .observeAsState(Duration.ofMinutes(defaultTime)).value.toMinutes(),
                    getRecentCourse = { homeViewModel.getRecentCourse(it) },
                    navController = navController
                )

                val scoreOther by homeViewModel.scoreOther.observeAsState(
                    ScoreOtherEntity(
                        "-1", "-1", "-1", "", "",
                        null, null, null, null,
                    )
                )

                val scoreDropDownList by homeViewModel.scoreDropDownList.observeAsState(listOf())


                ScoreCard(scoreDropDownList = scoreDropDownList,
                    scoreOther = scoreOther,
                    onScoreOtherDropDownChange = { homeViewModel.onScoreOtherDropDownChange(it) },
                    navController = navController)
            }
        }
    }
}

@Composable
fun ShowProgress(
    fetchingDetails: String, startfetch: () -> Unit,
) {
    var showingText by remember { mutableStateOf("") }
    showingText = fetchingDetails

    LaunchedEffect(Unit) {
        startfetch.invoke()
    }


    Column(modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        LinearProgressIndicator()
        Spacer(modifier = Modifier.padding(bottom = 10.dp))
        Text(showingText)
    }
}

@Preview
@Composable
fun CardPreview() {
    ScoreCard(scoreDropDownList = listOf(
        DropDownParams(110, 1, "110-1"),
        DropDownParams(110, 2, "110-2")),
        scoreOther = ScoreOtherEntity(
            "-1", "-1", "-1", "", "",
            null, null, null, null,
        ),
        onScoreOtherDropDownChange = {},
        navController = rememberNavController())
}

// TODO: Observe data is fully loaded

@Composable
fun WelcomeCard(navController: NavController, homeViewModel: HomeViewModel) {
    OutlinedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
    ) {
        Column(Modifier.padding(16.dp)) {
            val showConfirmDialog = remember { mutableStateOf(false) }
            Text(
                stringResource(R.string.home_welcome),
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp
            )
            Spacer(modifier = Modifier.padding(8.dp))
            Text(
                stringResource(R.string.home_datamissing_text),
                fontSize = 20.sp
            )
            Spacer(modifier = Modifier.padding(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(onClick = {
                    showConfirmDialog.value = true
                }) {
                    Text(stringResource(R.string.home_datamissing_refresh))
                }
            }
            // Confirm Dialog
            if (showConfirmDialog.value) {
                Dialog(onDismissRequest = { showConfirmDialog.value = false }) {
                    Card(modifier = Modifier.padding(8.dp)) {
                        Column(
                            modifier = Modifier.padding(
                                horizontal = 8.dp,
                                vertical = 20.dp
                            ),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                stringResource(R.string.home_datamissing_refresh_confirm),
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.padding(bottom = 20.dp))
                            Icon(
                                imageVector = Icons.TwoTone.Warning,
                                contentDescription = null, modifier = Modifier.scale(2F)
                            )
                            Spacer(modifier = Modifier.padding(bottom = 20.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                Button(onClick = { showConfirmDialog.value = false }) {
                                    Text(stringResource(R.string.no))
                                }
                                Button(modifier = Modifier.padding(end = 4.dp), onClick = {
                                    dbDataAvailability.value = false
                                    navController.navigate(Screen.Login.route)
                                    homeViewModel.clearDB(true)
                                }) {
                                    Text(stringResource(R.string.yes))
                                }
                            }
                        }

                    }
                }
            }
        }
    }
}

@Composable
fun SubjectCard(
    recentCourse: CourseEntity,
    minuteBefore: Long,
    getRecentCourse: (Long) -> Unit,
    navController: NavController,
) {
    if (recentCourse.courseId == -1) {
        NoCourseCard(minuteBefore, getRecentCourse, navController)
    } else {
        CourseCard(recentCourse, minuteBefore, getRecentCourse, navController)
    }

}

@Composable
fun NoCourseCard(
    minuteBefore: Long,
    getRecentCourse: (Long) -> Unit,
    navController: NavController,
) {
    OutlinedCard {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            // TODO: 休息時間就不算了?

            Text(
                stringResource(R.string.home_coursecard_subject),
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp
            )
            Spacer(modifier = Modifier.padding(8.dp))
            Text(
                stringResource(
                    id = R.string.home_coursecard_time_selector,
                    minuteBefore.toInt()
                )
            )
            Spacer(modifier = Modifier.padding(bottom = 20.dp))

            BeforeTimeSelector(minuteBefore, getRecentCourse, navController)
        }
    }
}

@Composable
fun CourseCard(
    recentCourse: CourseEntity,
    minuteBefore: Long,
    getRecentCourse: (Long) -> Unit,
    navController: NavController,
) {
    // first: stringResource: Int, second: recentCourse: String
    @Composable
    fun textOut(param: Pair<Int, String>) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally) {
            val textColor = MaterialTheme.colorScheme.onSurface
            AutosizeText(text = stringResource(param.first), maxLines = 1, textColor = textColor)
            AutosizeText(text = param.second, maxLines = 1, textColor)
        }


    }

    OutlinedCard {
        Column(
            modifier = Modifier
                .heightIn(min = 350.dp)
                .padding(10.dp),
        ) {
            Text(
                stringResource(R.string.home_coursecard_subject),
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp
            )
            Spacer(modifier = Modifier.padding(8.dp))

            Row(
                modifier = Modifier
                    .weight(2F)
                    .padding(bottom = 5.dp)
                    .fillMaxSize(1F),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {

                Row(
                    modifier = Modifier.padding(horizontal = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Box(
                        modifier = Modifier
                            .weight(0.75F)
                            .fillMaxSize(1F)
                            .clip(shape = RoundedCornerShape(15.dp))
                            .background(color = MaterialTheme.colorScheme.primary)
                    ) {
                        textOut(R.string.home_coursecard_course_name to recentCourse.courseName)
                    }
                    Column(
                        modifier = Modifier
                            .padding(start = 2.dp)
                            .weight(0.25F),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        for (item in listOf(
                            R.string.home_coursecard_start_time to
                                    recentCourse.courseTime[0].curriculumTimeRange.start.time.start.toIsoDescription(),
                            R.string.home_coursecard_end_time to
                                    recentCourse.courseTime[0].curriculumTimeRange.endInclusive.time.endInclusive.toIsoDescription()
                        )) {
                            Box(
                                modifier = Modifier
                                    .weight(1F)
                                    .fillMaxSize()
                                    .padding(6.dp)
                                    .clip(shape = RoundedCornerShape(15.dp))
                                    .background(color = MaterialTheme.colorScheme.primary)
                            ) {
                                Column {
                                    textOut(item)
                                }
                            }
                        }
                    }
                }
            }

            Row(
                modifier = Modifier.weight(1F)
            ) {
                for (item in listOf(
                    R.string.home_coursecard_class_name to recentCourse.className,
                    R.string.home_coursecard_class_location to recentCourse.classLocation,
                    R.string.home_coursecard_class_group to recentCourse.classGroup,
                    R.string.home_coursecard_class_time to recentCourse.classTime,
                )) {
                    Box(
                        modifier = Modifier
                            .weight(1F)
                            .fillMaxSize()
                            .padding(horizontal = 5.dp)
                            .clip(shape = RoundedCornerShape(3.dp))
                            .background(color = MaterialTheme.colorScheme.secondary)
                    ) {
                        textOut(item)
                    }
                }
            }

            Divider(modifier = Modifier.padding(top = 20.dp))
            BeforeTimeSelector(minuteBefore, getRecentCourse, navController)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BeforeTimeSelector(
    minuteBefore: Long,
    getRecentCourse: (Long) -> Unit,
    navController: NavController,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 5.dp),
        horizontalArrangement = Arrangement.End
    ) {
        var expanded by remember { mutableStateOf(false) }
        FilterChip(selected = true, onClick = { expanded = true }, label = {
            Box(
                modifier = Modifier.wrapContentSize()
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(3.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        stringResource(
                            R.string.home_coursecard_timeselector_text,
                            minuteBefore.toInt()
                        )
                    )
                    Icon(
                        imageVector =
                        if (expanded) Icons.Filled.ArrowDropUp
                        else Icons.Filled.ArrowDropDown,
                        contentDescription = null
                    )
                }
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    for (minute in 15..60 step 15) {
                        DropdownMenuItem(
                            text = {
                                Text(
                                    stringResource(
                                        R.string.home_coursecard_timeselector_text,
                                        minute
                                    )
                                )
                            },
                            onClick = {
                                getRecentCourse(minute.toLong())
                                expanded = false
                            }
                        )
                    }
                }
            }
        })

        Spacer(modifier = Modifier.padding(end = 8.dp))
        Button(onClick = {
            navController.navigate(Screen.Curriculum.route)
        }) {
            AutosizeText(stringResource(R.string.home_coursecard_see_all), 2)
        }
    }
}

@Composable
fun ScoreCard(
    scoreDropDownList: List<DropDownParams>,
    scoreOther: ScoreOtherEntity,
    onScoreOtherDropDownChange: (DropDownParams) -> Unit,
    navController: NavController,
) {
    OutlinedCard {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.SpaceEvenly,
        ) {
            Text(
                stringResource(R.string.home_scorecard_text),
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp,
                lineHeight = 32.sp,
                modifier = Modifier.padding(bottom = 3.dp)
            )

            ScoreOtherWidget(scoreOther = scoreOther)

            Divider(modifier = Modifier.padding(top = 3.dp, bottom = 3.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (scoreDropDownList.isNotEmpty()) {
                    SemesterSelector(scoreDropDownList) {
                        onScoreOtherDropDownChange(it)
                    }
                }

                Button(onClick = { navController.navigate(Screen.Score.route) }) {
                    AutosizeText(stringResource(R.string.home_scorecard_button), 2)
                }
            }
        }
    }
}