package com.example.nkustplatformassistant.ui.curriculum

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.nkustplatformassistant.data.CurriculumTime
import com.example.nkustplatformassistant.data.DropDownParams
import com.example.nkustplatformassistant.data.Weeks
import com.example.nkustplatformassistant.data.curriculumParams
import com.example.nkustplatformassistant.data.persistence.db.entity.CourseEntity

private var gridHeight = 50
private var gridWidth = 50

@Composable
fun CurriculumContent(curriculumViewModel: CurriculumViewModel, navController: NavController) {
    val startTimeVisibility by curriculumViewModel.startTimeVisibility.observeAsState(true)
    val endTimeVisibility by curriculumViewModel.endTimeVisibility.observeAsState(true)

    val dropDownParams by curriculumViewModel.dropDownParams.observeAsState(listOf())
    val courses by curriculumViewModel.courses.observeAsState(listOf())

    val state = rememberScrollState(0)

    Column(modifier = Modifier.padding(8.dp)) {
//    -----DisplayOption-----
        Row(
            modifier = Modifier.horizontalScroll(state),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.AutoAwesome,
                contentDescription = null,
                modifier = Modifier.padding(end = 2.dp)
            )
            Text(text = "Display:")

            ChipCell(
                startTimeVisibility,
                { curriculumViewModel.onStartTimeVisibilityChange() }
            ) { Text("Start Time") }

            ChipCell(
                endTimeVisibility,
                { curriculumViewModel.onEndTimeVisibilityChange() }
            ) { Text("End Time") }

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
                Text(text = "Please wait a while...")
                Spacer(modifier = Modifier.padding(bottom = 20.dp))
                CircularProgressIndicator()
            }
        }
    }
}

// 2
@Composable
fun CurriculumTable(
    startTimeVisibility: Boolean,
    endTimeVisibility: Boolean,
    courses: List<CourseEntity>,
) {
    val itemToPlace: MutableMap<Weeks, List<CourseEntity>> = mutableMapOf()

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
                                        CourseCard(course = eachCourse)
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

// 1
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SemesterSelector(
    dropDownList: List<DropDownParams>,
    onSelectDropDownChange: (DropDownParams) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    // TODO: wait until all data is ready and display
    var selectedOption by remember { mutableStateOf("null") }

    LaunchedEffect(dropDownList) {
        if (dropDownList.isNotEmpty()) {
            selectedOption = dropDownList.first().semDescription
        }
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.padding(start = 8.dp)
    ) {
        ChipCell(state = true, onClick = { expanded = true }) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(text = selectedOption)
                Icon(
                    imageVector = if (expanded) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown,
                    contentDescription = null
                )
            }
        }

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            dropDownList.forEach {
                DropdownMenuItem(
                    text = { Text(text = it.semDescription) },
                    onClick = {
                        onSelectDropDownChange.invoke(it)
                        selectedOption = it.semDescription
                        expanded = false
                    }
                )
            }
        }
    }

}

@Composable
fun WeeksCard(week: Weeks) {
    Card(
//        modifier = Modifier.padding(2.dp),
    ) {
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
fun CourseCard(course: CourseEntity) {
    var showCourseDetail by remember { mutableStateOf(false) }

    Card(
//            .padding(2.dp)
//            .size(
//                height = LocalDensity.current.run { gridHeight.toDp() },
//                width = LocalDensity.current.run { gridWidth.toDp() })
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .size(
                    height = LocalDensity.current.run { gridHeight.toDp() },
                    width = LocalDensity.current.run { gridWidth.toDp() })
                .animateContentSize()
                .clickable { showCourseDetail = true },
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = course.courseName)
        }
    }

    if (showCourseDetail) {
        Dialog(onDismissRequest = { showCourseDetail = false }) {
            CourseDetail(course = course)
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
                Text(text = buildAnnotatedString {
                    appendInlineContent("course")
                    append("${item[1]}:\t${item[2]}")
                }, inlineContent = mapOf(
                    Pair("course", InlineTextContent(
                        Placeholder(1.7.em,
                            height = 23.sp,
                            placeholderVerticalAlign = PlaceholderVerticalAlign.TextTop)
                    ) {
                        Icon(imageVector = item[0] as ImageVector, contentDescription = null)
                    })
                ))
            }

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
//        modifier = Modifier.padding(2.dp)
        modifier = Modifier
            .fillMaxSize()
            .animateContentSize(
                animationSpec = tween(durationMillis = 300,
                easing = LinearOutSlowInEasing)),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .onGloballyPositioned {
                    gridHeight = it.size.height
                    gridWidth = it.size.width
                }
                .padding(vertical = 5.dp, horizontal = 2.dp),
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
                if (startTimeVisibility) Text(curriculumTime.time.start.toIsoDescription())
                if (endTimeVisibility) Text(curriculumTime.time.endInclusive.toIsoDescription())
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

