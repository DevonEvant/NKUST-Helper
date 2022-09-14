package com.example.nkustplatformassistant.ui.home

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

    homeViewModel.dbHasData.value?.let {
        if (!it)
            MyIndicator(homeViewModel.startFetch(true))

    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        Text("Today", fontWeight = FontWeight.Bold, fontSize = 44.sp)
        Spacer(modifier = Modifier.padding(vertical = 15.dp))

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item {
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium,
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text(text = "welcome", fontWeight = FontWeight.Bold, fontSize = 32.sp)
                        Spacer(modifier = Modifier.padding(8.dp))
                        Text(text = "Login for better service.", fontSize = 20.sp)
                        Spacer(modifier = Modifier.padding(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Button(onClick = {
                                // TODO: POP dialog to confirm user's choose
                                navController.navigate(Screen.Login.route)
                                homeViewModel.clearDB(true)
                            }) {
                                Text(text = "Press me to clear DB!")
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

        var courseWidgetParams: Map<HomeViewModel.SubjectWidgetEnum, String> = remember { mapOf() }
        homeViewModel.courseWidgetParams.observe(lifecycleOwner) {
            courseWidgetParams = it
        }

        HorizontalPager(
            count = 3,
            contentPadding = PaddingValues(horizontal = 32.dp)
        ) { currentPage ->
            when (currentPage) {
                0 -> SubjectCard(courseWidgetParams)
                1 -> Text(text = "22")
                2 -> Text(text = "33")
            }
        }
    }
}

@Composable

fun MyIndicator(
    indicatorProgress: LiveData<Float>,
//    lifecycleOwner: LifecycleOwner,
) {

    var progress by remember { mutableStateOf(0f) }
    val progressAnimDuration = 1500

//    indicatorProgress.observe(lifecycleOwner) {
//        progress = it
//    }

    progress = indicatorProgress.observeAsState().value!!

    val progressAnimation by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = progressAnimDuration, easing = FastOutSlowInEasing)
    )

    LinearProgressIndicator(
        modifier =
        if (progress != 1F) {
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
        } else {
            Modifier
                .alpha(0F)
        }, // Rounded edges
        progress = progressAnimation
    )
}

@Preview
@Composable
fun CardPreview() {
    SubjectCard(
        mapOf()
    )
}

// TODO: Observe data is fully loaded

@Composable
fun SubjectCard(
    courseWidgetParams: Map<HomeViewModel.SubjectWidgetEnum, String>,
) {
    // Given SubjectWidgetEnum and it'll return string
    @Composable
    fun textOut(field: HomeViewModel.SubjectWidgetEnum) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly) {

            val content = if (courseWidgetParams[field].isNullOrBlank()) "Please Wait..." else courseWidgetParams[field]

            Text(text = content!!)
            Text(text = content!!) //TODO: detail of SubjectWidget in (maybe enum class needed)
        }
    }

    Card {
        Column(
            modifier = Modifier
                .size(width = 320.dp, height = 180.dp)
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
                            .background(color = Color.Blue)
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
                                    .background(brush = Brush.sweepGradient(
                                        listOf(
                                            MaterialTheme.colorScheme.primary,
                                            MaterialTheme.colorScheme.primaryContainer
                                        )
                                    ))
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
                            .background(brush = Brush.sweepGradient(
                                listOf(
                                    MaterialTheme.colorScheme.secondary,
                                    MaterialTheme.colorScheme.secondaryContainer
                                )
                            ))
                    ) {
//                        when (item) {
//                            1 -> Text(text = "1")
//                            2 -> Text(text = "2")
//                            3 -> Text(text = "3")
//                            4 -> Text(text = "4")
//                        }
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