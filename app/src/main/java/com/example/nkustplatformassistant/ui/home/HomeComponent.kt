package com.example.nkustplatformassistant.ui.home

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import com.example.nkustplatformassistant.navigation.Screen
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager

@Composable
fun HomeScreenBase(homeViewModel: HomeViewModel, navController: NavController) {
    val context = LocalContext.current
//    if (homeViewModel.isDataReady.observeAsState(false).value) {
    HomeBase(homeViewModel, navController)
//    } else {
//        CheckData(context, navController)
//    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HomeBase(
    homeViewModel: HomeViewModel,
    navController: NavController,
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    val displayIndicator = remember { mutableStateOf(false) }

    val displayContent = remember { mutableStateOf(false) }

    homeViewModel.dbHasData.observeAsState(null).let {
        println("dbHasData: ${it.value}")
        when (it.value) {
            true -> displayContent.value = true
            false -> displayIndicator.value = true
            else -> {}
        }
    }

    if (displayIndicator.value) {
        MyIndicator(homeViewModel.startFetch(true), displayContent)
    }

    if (displayContent.value) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
        ) {
            Text("Recent Highlight", fontWeight = FontWeight.Bold, fontSize = 44.sp)
            Spacer(modifier = Modifier.padding(vertical = 15.dp))

            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium,
            ) {
                Column(Modifier.padding(16.dp)) {
                    val showConfirmDialog = remember { mutableStateOf(false) }
                    Text(text = "Welcome!",
                        fontWeight = FontWeight.Bold,
                        fontSize = 32.sp)
                    Spacer(modifier = Modifier.padding(8.dp))
                    Text(text = "If you faced data missing, \n" +
                            "don't hesitate to login again refresh database.",
                        fontSize = 20.sp)
                    Spacer(modifier = Modifier.padding(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Button(onClick = {
                            showConfirmDialog.value = true
                        }) {
                            Text(text = "Press me to refresh!")
                        }
                    }
                    // Confirm Dialog
                    if (showConfirmDialog.value) {
                        Dialog(onDismissRequest = { showConfirmDialog.value = false }) {
                            Card {
                                Column(modifier = Modifier.padding(10.dp),
                                    verticalArrangement = Arrangement.SpaceEvenly) {
                                    Text(text = "Are you sure to refresh database and login again?")
                                    Button(onClick = { showConfirmDialog.value = false }) {
                                        Text(text = "No")
                                    }
                                    Button(onClick = {
                                        navController.navigate(Screen.Login.route)
                                        homeViewModel.clearDB(true)
                                    }) {
                                        Text(text = "Yes")
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.padding(vertical = 50.dp))

            // https://google.github.io/accompanist/pager/
            // TODO: when finish scraping, stop showing circular progress bar and show Pager
            // loading with progress bar, when it reaches its end, show home page

            val courseWidgetParams = remember {
                mutableMapOf<HomeViewModel.SubjectWidgetEnum, String>()
            }

            // TODO: NO DATA state 抓不到資料
            // Text Color: https://m3.material.io/styles/color/dynamic-color/overview
            homeViewModel.courseWidgetParams.observeAsState(mapOf()).value.let {
                if (it.isNotEmpty()) {
                    courseWidgetParams.putAll(it)
                }
            }

            HorizontalPager(
                count = 3,
                contentPadding = PaddingValues(horizontal = 32.dp)
            ) { currentPage ->
                when (currentPage) {
                    0 -> {
                        if (courseWidgetParams.isNotEmpty())
                            SubjectCard(courseWidgetParams)
                        else NoSubjectCard(navController)
                    }
                    1 -> Text(text = "22", color = MaterialTheme.colorScheme.onSurface)
                    2 -> Text(text = "33", color = MaterialTheme.colorScheme.onSurface)
                }
            }
        }
    }
}


@Composable

fun MyIndicator(
    indicatorProgress: LiveData<Float>,
    displayContent: MutableState<Boolean>,
//    lifecycleOwner: LifecycleOwner,
) {

    var progress by remember { mutableStateOf(0f) }
    val progressAnimDuration = 1500

//    indicatorProgress.observe(lifecycleOwner) {
//        progress = it
//    }

    progress = indicatorProgress.observeAsState(0F).value.let {
        if (it == 1F) displayContent.value = true
        it
    }

    val progressAnimation by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = progressAnimDuration, easing = FastOutSlowInEasing)
    )

    LinearProgressIndicator(
        modifier =
        if (progress < 1F) {
            Modifier
                .fillMaxWidth()
                .background(color = Color.Transparent)
                .clip(RoundedCornerShape(20.dp))
        } else {
            Modifier
                .alpha(0F)
        },
        progress = progressAnimation
    )
}

@Preview
@Composable
fun CardPreview() {
    SubjectCard(
        mapOf()
    )
//    HomeBase(homeViewModel = HomeViewModel(DataRepository.getInstance(LocalContext.current)),
//        navController = rememberNavController() )
}

// TODO: Observe data is fully loaded

@Composable
fun NoSubjectCard(
    navController: NavController,
) {
    OutlinedCard {
        Column(
            modifier = Modifier
                .size(width = 340.dp, height = 200.dp)
                .padding(10.dp),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            // TODO: 休息時間就不算了?
            Text(text = "You don't have any course in ${0} minutes,\njust chill and take a break.")

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(onClick = {
                    navController.navigate(Screen.Curriculum.route)
                }) {
                    Text(text = "See all courses")
                }
            }
        }
    }
}

@Composable
fun SubjectCard(
    courseWidgetParams: Map<HomeViewModel.SubjectWidgetEnum, String>,
) {
    // Given SubjectWidgetEnum and it'll return string
    @Composable
    fun textOut(field: HomeViewModel.SubjectWidgetEnum) {
        var textSize by remember { mutableStateOf<IntSize?>(null) }
        val density = LocalDensity.current
        val maxDimensionDp = remember(textSize) {
            textSize?.let { textSize ->
                with(density) {
                    maxOf(textSize.width, textSize.height).toDp()
                }
            }
        }
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly) {

            val content =
                if (courseWidgetParams[field].isNullOrBlank()) "Please Wait..."
                else courseWidgetParams[field].toString()

            Text(text = field.name, color = MaterialTheme.colorScheme.onTertiary,
                onTextLayout = {
                    textSize = it.size
                })
            Text(text = content, color = MaterialTheme.colorScheme.onTertiary,
                onTextLayout = {
                    textSize = it.size
                })
            //TODO: detail of SubjectWidget in (maybe enum class needed)
        }
    }

    Card {
        Column(
            modifier = Modifier
                .size(width = 340.dp, height = 200.dp)
                .padding(top = 10.dp, bottom = 10.dp),
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier
                    .weight(2F)
                    .padding(bottom = 5.dp)
                    .fillMaxSize(1F),
                horizontalArrangement = Arrangement.SpaceEvenly) {

                Row(
                    modifier = Modifier.padding(horizontal = 5.dp)
                ) {

                    Box(
                        modifier = Modifier
                            .weight(0.75F)
                            .fillMaxSize(1F)
                            .clip(shape = RoundedCornerShape(15.dp))
                            .background(color = MaterialTheme.colorScheme.primary)
                    ) {
                        textOut(field = HomeViewModel.SubjectWidgetEnum.CourseName)
                    }
                    Column(
                        modifier = Modifier
                            .weight(0.25F)) {
                        for (item in listOf(HomeViewModel.SubjectWidgetEnum.StartTime,
                            HomeViewModel.SubjectWidgetEnum.EndTime)) {
                            Box(
                                modifier = Modifier
                                    .weight(1F)
                                    .fillMaxSize()
                                    .padding(6.dp)
                                    .clip(shape = RoundedCornerShape(20.dp))
                                    .background(color = MaterialTheme.colorScheme.primary)
                            ) {
                                textOut(item)
                            }
                        }
                    }
                }
            }

            Row(
                modifier = Modifier.weight(1F)
            ) {
                for (item in listOf(
                    HomeViewModel.SubjectWidgetEnum.ClassName,
                    HomeViewModel.SubjectWidgetEnum.ClassLocation,
                    HomeViewModel.SubjectWidgetEnum.ClassGroup,
                    HomeViewModel.SubjectWidgetEnum.ClassTime,
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
        }
    }
}

@Composable
fun CheckData(context: Context, navController: NavController) {
    Column(
        modifier = Modifier
            .padding(bottom = 20.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
        Text(text = "Please wait a while...")
        // https://stackoverflow.com/questions/72701963/why-it-says-list-contains-no-element-matching-the-predicate-for-android-jetp
        LaunchedEffect(Unit) {
            Toast.makeText(
                context,
                "It seems database is null, please re-login to get data from web again.",
                Toast.LENGTH_LONG
            ).show()
            navController.navigate(Screen.Login.route)
        }
    }
}