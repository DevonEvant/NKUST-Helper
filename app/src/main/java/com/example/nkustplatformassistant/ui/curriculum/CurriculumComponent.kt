package com.example.nkustplatformassistant.ui.curriculum

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.AlertDialogDefaults.shape
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.nkustplatformassistant.data.CurriculumTime
import com.example.nkustplatformassistant.data.DropDownParams
import com.example.nkustplatformassistant.data.Weeks
import com.example.nkustplatformassistant.data.persistence.db.entity.CourseEntity


@Composable
fun CurriculumContent(curriculumViewModel: CurriculumViewModel, navController: NavController) {
    val timeVisibility by curriculumViewModel.timeVisibility.observeAsState(true)
    val timeCodeVisibility by curriculumViewModel.timeCodeVisibility.observeAsState(true)

    val dropDownParams by curriculumViewModel.dropDownParams.observeAsState(listOf())
    val courses by curriculumViewModel.courses.observeAsState(listOf())

    LaunchedEffect(dropDownParams) {
        if (dropDownParams.isNotEmpty()) {
            curriculumViewModel.getCourse(
                dropDownParams[0].year, dropDownParams[0].semester
            )
        }
    }

    val state = rememberScrollState()
    LaunchedEffect(Unit) { state.animateScrollTo(0) }

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
                timeVisibility,
                { curriculumViewModel.onTimeVisibilityChange() }
            ) { Text("Time") }

            ChipCell(
                timeCodeVisibility,
                { curriculumViewModel.onTimeCodeVisibilityChange() }
            ) { Text("Time Code") }

            // TODO: Debug usage here
            SemesterSelector(curriculumViewModel.dropDownParams.value!!)
        }

        Divider()
//    -----CurriculumTable-----
//        val itemsList = (0..80).toList()

        if (courses.isNotEmpty()) {
            CurriculumTable(timeVisibility, timeCodeVisibility, courses)
        } else {
            Column(modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center) {
                Text(text = "Please wait a while...")
                Spacer(modifier = Modifier.padding(bottom = 20.dp))
                CircularProgressIndicator()
            }
        }

    }
}

@Composable
fun CurriculumTable(
    timeVisibility: Boolean,
    timeCodeVisibility: Boolean,
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
        columns = GridCells.Fixed(7),
        contentPadding = PaddingValues(vertical = 8.dp)
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
                    CurriculumTimeCard(curriculumTime, timeVisibility, timeCodeVisibility)
                }
            }
//            index.let {
//                courses.forEach { eachCourse ->
//                    eachCourse.courseTime.forEach { courseTime ->
//                        when (courseTime.week) {
//                            Weeks.Mon -> {
//                                curriculumParams[Weeks.Mon]
//
//                            }
//                        }
//                        if ((courseTime.curriculumTimeRange.start.ordinal) == week)
//                    }
//                }
//            }
        }

        courses.forEachIndexed { index, course ->
            val span: Int = 0

            item(
//                key = { 8 },
//                span = {
//                    GridItemSpan(span)
//                }
            ) {
                println(course)
                CourseCard(course)
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SemesterSelector(semesterList: List<DropDownParams>) {
    var expanded by remember { mutableStateOf(false) }

    // TODO: wait until all data is ready and display
    var selectedOption: String =
        if (semesterList.isNotEmpty()) semesterList.first().semDescription else "Null"

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.padding(start = 8.dp)
    ) {
        ChipCell(state = true, onClick = { expanded = true }) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly) {
                Text(text = selectedOption)
                Icon(imageVector = if (expanded) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown,
                    contentDescription = null)
            }
        }

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            semesterList.forEach {
                DropdownMenuItem(
                    text = { Text(text = it.semDescription) },
                    onClick = {
                        selectedOption = it.semDescription
                        expanded = false
                    })
            }
        }
    }

}

@Composable
fun WeeksCard(week: Weeks) {
    Card(
        modifier = Modifier.padding(2.dp),
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
    Card(
        modifier = Modifier.padding(2.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
        ) {
//            todo option

            Text(course.courseName)
//            Text(course.professor)
            Text(course.classLocation)
        }
    }
}

@Composable
fun CurriculumTimeCard(
    curriculumTime: CurriculumTime,
    timeVisibility: Boolean = true,
    timeCodeVisibility: Boolean = true,
) {
    Card(
        modifier = Modifier.padding(2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                curriculumTime.id.toString(),
                modifier = Modifier
                    .clip(shape)
                    .background(Color.LightGray)
                    .padding(2.dp)
            )

            if (timeVisibility)
                Text(curriculumTime.time.start.toIsoDescription())
            if (timeCodeVisibility)
                Text(curriculumTime.time.endInclusive.toIsoDescription())
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

