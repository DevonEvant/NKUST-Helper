package com.example.nkustplatformassistant.ui.curriculum

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.*
import androidx.compose.material3.AlertDialogDefaults.shape
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.nkustplatformassistant.ui.theme.Nkust_platform_assistantTheme
import com.soywiz.klogger.AnsiEscape

@Composable
fun CurriculumContext(curriculumViewModel: CurriculumViewModel) {
    val timeVisibility by curriculumViewModel.timeVisibility.observeAsState(true)
    val timeCodeVisibility by curriculumViewModel.timeCodeVisibility.observeAsState(true)

    val state = rememberScrollState()
    LaunchedEffect(Unit) { state.animateScrollTo(100) }

    Column {
//    -----DisplayOption-----
        Row(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .horizontalScroll(state),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Default.AutoAwesome,
                contentDescription = null,
                modifier = Modifier.padding(horizontal = 2.dp)
            )
            Text(text = "Display：")

            ChipCell(
                "Time",
                timeVisibility
            ) { curriculumViewModel.onTimeVisibilityChange() }

            ChipCell(
                "Time Code",
                timeCodeVisibility
            ) { curriculumViewModel.onTimeCodeVisibilityChange() }

        }

//    -----CurriculumTable-----
        val itemsList = (0..80).toList()
        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            contentPadding = PaddingValues(
                start = 12.dp,
//                top = 16.dp,
                end = 12.dp,
                bottom = 16.dp
            )
        ) {
            items(77) { index ->
                if (index == 0) {
                } else if (index < 7) {
                    val week = Weeks[index - 1]
                    WeeksCard(week)
                } else if (index % 7 == 0) {
                    val curriculumTime = CurriculumTime[(index / 7) - 1]
                    CurriculumTimeCard(curriculumTime, timeVisibility, timeCodeVisibility)
                } else
                    Text(itemsList[index].toString())


            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChipCell(lable: String, state: Boolean, onClick: (() -> Unit)) {
//    val uid: String by loginParamsViewModel.uid.observeAsState("")
//    val pwd: String by loginParamsViewModel.pwd.observeAsState("")
    FilterChip(
        selected = state,
        onClick = onClick,
        label = { Text(lable) },
        modifier = Modifier.padding(2.dp)

    )
}

@Composable
fun WeeksCard(week: Weeks) {
    Card(
        modifier = Modifier.padding(2.dp),
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(week.shortCode)
        }
    }
}


fun LazyGridScope.CourseItem(course: Course) {
    val index: Int = 12

    item(
        key = { index },
        span = {
            GridItemSpan(maxLineSpan)
        }
    ) {
        CourseCard(course)
    }
}

@Composable
fun CourseCard(course: Course) {
    Card(
        modifier = Modifier.padding(2.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
        ) {
//            todo option

            Text(course.courseName)
            Text(course.professor)
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
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                curriculumTime.id,
                modifier = Modifier
                    .clip(shape)
                    .background(Color.LightGray)
                    .padding(2.dp)
            )

            if (timeVisibility)
                Text(curriculumTime.startTime)
            if (timeCodeVisibility)
                Text(curriculumTime.endTime)
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

// ------------------

//@Preview(showBackground = true)
//@Composable
//fun CurriculumTimeCardPreview() {
//    CurriculumTimeCard(CurriculumTime[1])
////    CurriculumTimeCard(CurriculumTime[2])
//
//}

@Preview(showBackground = true)
@Composable
fun CourseBlockPreview() {
    Nkust_platform_assistantTheme {
        CourseCard(
            Course(
                "123",
                "123",
                "123",
                "123",
                "123",
                "123",
                false,
                "123",
                "123",
                "123",
                "123"
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    Nkust_platform_assistantTheme {
        val curriculumViewModel: CurriculumViewModel = viewModel()
        CurriculumContext(curriculumViewModel)
    }
}
