package com.example.nkustplatformassistant.ui.score

import androidx.compose.material.BottomSheetScaffold
import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material3.*
import androidx.compose.material3.AlertDialogDefaults.shape
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.nkustplatformassistant.data.NkustEvent
import com.example.nkustplatformassistant.ui.login.LoginParamsViewModel
import com.example.nkustplatformassistant.ui.theme.Nkust_platform_assistantTheme
import com.soywiz.klogger.AnsiEscape
import com.soywiz.korio.async.launch
import kotlinx.coroutines.GlobalScope

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class,
    ExperimentalMaterialApi::class,
)
@Composable
fun ScheduleContent(scheduleViewModel: ScheduleViewModel) {
    val schedules by scheduleViewModel.schedules.observeAsState()


    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState()
//    GlobalScope.launch {
//        bottomSheetScaffoldState.bottomSheetState.expand()
//    }

    val lazyListState = rememberLazyListState()

    BottomSheetScaffold(
        modifier = Modifier.fillMaxSize(),
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        scaffoldState = bottomSheetScaffoldState,
        sheetContent = {
            Column(
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp),
            )
            {
                Text("Today", fontSize = 32.sp)
                Text("aaaaa", fontSize = 16.sp)
                Text("aaaaa", fontSize = 16.sp)
                Text("aaaaa", fontSize = 16.sp)
                Text("aaaaa", fontSize = 16.sp)
                Text("aaaaa", fontSize = 16.sp)
            }
        },
    ) {
        LazyColumn(
            state = lazyListState,
            modifier = Modifier
                .fillMaxSize()
//                .padding(16.dp),
        ) {
            schedules?.forEach { index ->
                stickyHeader(
                ) {
                    Text(text = "manufacturer")
                }
                item {
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
            }
        }
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
fun HomeScreen() {
//    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
//        bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed)
//    )
//    coroutineScope.launch {
//
//        if (bottomSheetScaffoldState.bottomSheetState.isCollapsed) {
//            bottomSheetScaffoldState.bottomSheetState.expand()
//        } else {
//            bottomSheetScaffoldState.bottomSheetState.collapse()
//        }
//    }

}

@Composable
@Preview(showBackground = true)
fun ScheduleItemPreview() {
    ScheduleItem()

}

@Composable
@Preview(showBackground = true)
fun tPreview() {
    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        throw Error("No ViewModelStoreOwner was provided via LocalViewModelStoreOwner")
    }
    val scheduleViewModel: ScheduleViewModel =
        viewModel(viewModelStoreOwner = viewModelStoreOwner)
    ScheduleContent(scheduleViewModel)

}

