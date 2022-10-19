package com.narui.nkustplatformassistant.ui.curriculum

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.twotone.*
import androidx.compose.material3.*
import androidx.compose.material3.AlertDialogDefaults.shape
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.narui.nkustplatformassistant.R
import com.narui.nkustplatformassistant.data.CurriculumTime
import com.narui.nkustplatformassistant.data.DropDownParams
import com.narui.nkustplatformassistant.data.Weeks
import com.narui.nkustplatformassistant.data.curriculumParams
import com.narui.nkustplatformassistant.data.persistence.db.entity.CourseEntity
import com.narui.nkustplatformassistant.navigation.Screen

@Composable
fun CurriculumContent(curriculumViewModel: CurriculumViewModel, navController: NavController) {
    val startTimeVisibility by curriculumViewModel.startTimeVisibility.observeAsState(true)
    val endTimeVisibility by curriculumViewModel.endTimeVisibility.observeAsState(true)

    val dropDownParams by curriculumViewModel.dropDownParams.observeAsState(listOf())
    val courses by curriculumViewModel.courses.observeAsState(listOf())

    val scrollState = rememberScrollState(0)

    Column(modifier = Modifier.padding(8.dp)) {
//    -----DisplayOption-----
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.navigate(Screen.Home.route) }) {
                Icon(imageVector = Icons.TwoTone.ArrowBack, contentDescription = null)
            }

            Icon(
                imageVector = Icons.Default.AutoAwesome,
                contentDescription = null,
                modifier = Modifier.padding(end = 2.dp)
            )
            Text(stringResource(R.string.curriculum_filter))

            Row(modifier = Modifier.horizontalScroll(scrollState)) {

                ChipCell(
                    startTimeVisibility,
                    { curriculumViewModel.onStartTimeVisibilityChange() },
                ) { Text(stringResource(R.string.curriculum_start_time)) }

                ChipCell(
                    endTimeVisibility,
                    { curriculumViewModel.onEndTimeVisibilityChange() },
                ) { Text(stringResource(R.string.curriculum_end_time)) }

                // TODO: Debug usage here
//            val dropDownParams = listOf(
//                DropDownParams(110,1,"110-1"),
//                DropDownParams(110,2,"110-2"),
//                DropDownParams(110,3,"110-3"),
//            )
                SemesterSelector(dropDownParams) {
                    curriculumViewModel.onSelectDropDownChange(it)
                }
            }
        }

        Divider()
//    -----CurriculumTable-----
//        val itemsList = (0..80).toList()

        if (courses.isNotEmpty()) {
            CurriculumTable(startTimeVisibility, endTimeVisibility, courses)
        } else {
            LaunchedEffect(Unit) {
                if (dropDownParams.isNotEmpty()) {
                    curriculumViewModel.getCourse(
                        dropDownParams[0].year, dropDownParams[0].semester
                    )
                }
            }

            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(stringResource(R.string.curriculum_waiting))
                Spacer(modifier = Modifier.padding(bottom = 20.dp))
                CircularProgressIndicator()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChipCell(
    state: Boolean,
    onClick: (() -> Unit),
    content: @Composable () -> Unit,
) {
    FilterChip(
        selected = state,
        onClick = onClick,
        label = content,
        modifier = Modifier.padding(2.dp)
    )
}

@Composable
fun SemesterSelector(
    dropDownList: List<DropDownParams>,
    onSelectDropDownChange: (DropDownParams) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    var currentSelectDropDownText by remember { mutableStateOf("Please wait...") }

    LaunchedEffect(dropDownList) {
        currentSelectDropDownText =
            if (dropDownList.isNotEmpty()) dropDownList.first().semDescription else "Please wait..."
    }

    ChipCell(state = true, onClick = { expanded = true }) {
        Box(modifier = Modifier.wrapContentSize()) {
            Row(horizontalArrangement = Arrangement.spacedBy(5.dp),
                verticalAlignment = Alignment.CenterVertically) {
                Text(text = currentSelectDropDownText)
                Icon(imageVector = if (expanded) Icons.TwoTone.ArrowDropUp
                else Icons.TwoTone.ArrowDropDown,
                    contentDescription = null)
            }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                if (dropDownList.isEmpty()) {
                    DropdownMenuItem(text = { Text(text = currentSelectDropDownText) },
                        onClick = { })
                } else {
                    dropDownList.forEach {
                        DropdownMenuItem(
                            text = { Text(text = it.semDescription) },
                            onClick = {
                                currentSelectDropDownText = it.semDescription
                                onSelectDropDownChange(it)
                                expanded = false
                            }
                        )
                    }
                }
            }
        }
    }

}

@Composable
fun AutosizeText(text: String, maxLines: Int, textColor: Color = TextStyle.Default.color) {

    var multiplier by remember { mutableStateOf(1F) }
    var readyToDraw by remember { mutableStateOf(false) }

    Text(
        text = text,
        color = textColor,
        maxLines = maxLines, // modify to fit your need
        overflow = TextOverflow.Visible,
        style = LocalTextStyle.current.copy(
            fontSize = LocalTextStyle.current.fontSize * multiplier
        ),
        textAlign = TextAlign.Center,
        modifier = Modifier.drawWithContent {
            if (readyToDraw) drawContent()
        },
        onTextLayout = {
            if (it.hasVisualOverflow && !readyToDraw) {
                multiplier *= 0.9F // you can tune this constant
            } else {
                readyToDraw = true
            }
        }
    )
}

@Composable
fun CurriculumTable(
    startTimeVisibility: Boolean,
    endTimeVisibility: Boolean,
    courses: List<CourseEntity>,
) {
    val itemToPlace: MutableMap<Weeks, List<CourseEntity>> = mutableMapOf()

    // int multiply this and add .dp will transfer to dp
    val localDensity = LocalDensity.current

    var weekCardMinHeight by remember {
        mutableStateOf(50)
    }

    for (week in Weeks.values()) {
        val thisWeekCourseList = mutableListOf<CourseEntity>()
        for (eachCourse in courses) {
            for (courseTime in eachCourse.courseTime) {
                if (courseTime.week!! == week) {
                    thisWeekCourseList.add(eachCourse)
                }
            }
        }
        itemToPlace[week] = thisWeekCourseList
    }

    @Composable
    fun WeeksCard(week: Weeks) {
        Card {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(week.shortCode.toString())
            }
        }
    }

    @Composable
    fun CurriculumTimeCard(
        curriculumTime: CurriculumTime,
        startTimeVisibility: Boolean = true,
        endTimeVisibility: Boolean = true,
    ) {
        Card(
            modifier = Modifier
                .fillMaxSize()
                .animateContentSize(
                    animationSpec = tween(durationMillis = 300,
                        easing = LinearOutSlowInEasing)),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 5.dp, horizontal = 2.dp)
                    .heightIn(min = localDensity.run { weekCardMinHeight.toDp() })
                    .onGloballyPositioned {
                        weekCardMinHeight = it.size.height
                    },
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = curriculumTime.id.toString(),
                        modifier = Modifier
                            .clip(shape)
                            .background(MaterialTheme.colorScheme.onBackground)
                            .padding(2.dp),
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.background
                        )
                    )

                    if (startTimeVisibility) AutosizeText(
                        text = curriculumTime.time.start.toIsoDescription(),
                        maxLines = 1
                    )
                    if (endTimeVisibility) AutosizeText(
                        text = curriculumTime.time.endInclusive.toIsoDescription(),
                        maxLines = 1,
                    )
                }
            }
        }
    }

    @Composable
    fun CourseDetail(course: CourseEntity) {
        OutlinedCard {
            val itemListToDisplay = listOf(
                listOf(Icons.TwoTone.MenuOpen, "課程名稱", course.courseName),
                listOf(Icons.TwoTone.EmojiPeople, "指導教授", course.professor),
                listOf(Icons.TwoTone.LocationOn, "上課地點", course.classLocation),
                listOf(Icons.TwoTone.AssistantPhoto, "學分數", course.credits)
            )
            Column(modifier = Modifier.padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)) {
                for (item in itemListToDisplay) {
                    Row(horizontalArrangement = Arrangement.spacedBy(5.dp),
                        verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = item[0] as ImageVector, contentDescription = null)

                        AutosizeText(text = "${item[1]}:\t${item[2]}", maxLines = 1)
                    }
                }
            }
        }
    }

    @Composable
    fun CourseCard(course: CourseEntity) {
        var showCourseDetail by remember { mutableStateOf(false) }

        Card {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .heightIn(
                        min = localDensity.run { weekCardMinHeight.toDp() },
                        max = localDensity.run { weekCardMinHeight.toDp() })
                    .animateContentSize(animationSpec = tween(durationMillis = 300,
                        easing = LinearOutSlowInEasing))
                    .clickable { showCourseDetail = true },
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                AutosizeText(text = course.courseName, maxLines = 3)
            }
        }

        if (showCourseDetail) {
            Dialog(onDismissRequest = { showCourseDetail = false }) {
                CourseDetail(course = course)
            }
        }
    }

    LazyVerticalGrid(
        modifier = Modifier.animateContentSize(),
        columns = GridCells.Fixed(7),
        contentPadding = PaddingValues(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {

        items(83) { index ->
            when {
                index == 0 -> {}
                index < 7 -> {
                    val week = Weeks[index - 1]
                    WeeksCard(week)
                }
                index % 7 == 0 -> {
                    val curriculumTime = CurriculumTime[(index / 7) - 1]
                    CurriculumTimeCard(curriculumTime, startTimeVisibility, endTimeVisibility)
                }
            }

            for (week in Weeks.values()) {
                curriculumParams[week]?.apply {
                    for (gridIndex in this) {
                        if (index == gridIndex) {
                            itemToPlace[week]?.forEach { eachCourse ->
                                eachCourse.courseTime.forEach {
                                    if (curriculumParams[week]!!.indexOf(gridIndex) in
                                        it.curriculumTimeRange.start.ordinal..
                                        it.curriculumTimeRange.endInclusive.ordinal
                                    ) {
                                        CourseCard(eachCourse)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


//{ //top
//    item(span = { GridItemSpan(6) }) { TopInfo() } //一行2個
//    itemsIndexed(items = list1, spans = { _, _ -> GridItemSpan(3) },
//        itemContent = { index, item -> GridItemContent(item.second) })
//    //一行3個
//    itemsIndexed(items = list2, spans = { _, _ -> GridItemSpan(2) },
//        itemContent = { index, item -> GridItemContent(item.second) })
//}

//// ------------------
//
//@Preview(showBackground = true)
//@Composable
//fun WhatEverPreview() {
//    Nkust_platform_assistantTheme {
//        val curriculumViewModel = CurriculumViewModel(DataRepository(LocalContext.current))
//        CurriculumContext(curriculumViewModel)
////        curriculumViewModel.t()
//
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//fun CurriculumTimeCardPreview() {
//    Text("123")
//
//}

