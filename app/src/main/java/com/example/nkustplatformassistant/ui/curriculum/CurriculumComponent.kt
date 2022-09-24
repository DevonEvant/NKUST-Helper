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
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import com.example.nkustplatformassistant.data.*
import com.example.nkustplatformassistant.data.persistence.db.entity.CourseEntity

private var gridHeight = 50
private var gridWidth = 50

@Composable
fun CurriculumContent(curriculumViewModel: CurriculumViewModel, navController: NavController) {
    val startTimeVisibility by curriculumViewModel.startTimeVisibility.observeAsState(true)
    val endTimeVisibility by curriculumViewModel.endTimeVisibility.observeAsState(true)

    val dropDownParams by curriculumViewModel.dropDownParams.observeAsState(listOf())

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
            SemesterSelector(curriculumViewModel.dropDownParams.value!!)
//            SemesterSelector(semesterList = dropDownParams)
        }

        Divider()
//    -----CurriculumTable-----
//        val a = CourseEntity(
//            1111,
//            1,
//            1,
//            "123",
//            "123",
//            "123",
//            "123",
//            "123",
//            "123",
//            "123",
//            "123",
//            "123",
//            true
//        )
//        a.courseTime = listOf<CourseTime>(
//            CourseTime(
//                Weeks.Wed,
//                CurriculumTime._3..CurriculumTime._5,
//            )
//        )

//        val courses by curriculumViewModel.courses.observeAsState(listOf(a))
            val courses by curriculumViewModel.courses.observeAsState(listOf())

        if (courses.isNotEmpty()) {
            CurriculumTable( startTimeVisibility,endTimeVisibility, courses)
        } else {
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
                    CurriculumTimeCard(curriculumTime,startTimeVisibility, endTimeVisibility)
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
                                        CourseCard(courseName = eachCourse.courseName)
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
        ChipCell(state = true, onClick = { expanded = !expanded }) {
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
fun CourseCard(courseName: String) {
    Card(
        modifier = Modifier
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
                    width = LocalDensity.current.run { gridWidth.toDp() }),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = courseName)
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

