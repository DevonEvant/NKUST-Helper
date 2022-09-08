package com.example.nkustplatformassistant.ui.home

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.nkustplatformassistant.navigation.Screen

@Composable
fun HomeScreenBase(homeViewModel: HomeViewModel, navController: NavController) {
    val context = LocalContext.current
//    if (homeViewModel.isDataReady.observeAsState(false).value) {
    HomeBase(homeViewModel, navController)
//    } else {
//        CheckData(context, navController)
//    }
}

@Composable
fun HomeBase(
    homeViewModel: HomeViewModel,
    navController: NavController,
) {
    LaunchedEffect(Unit) {
        if (!homeViewModel.checkDBHasData()) {
            homeViewModel.startFetch(true)
        }
    }

    MyIndicator(homeViewModel.progress.value!!)

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

        Column() {
            LazyRow() {

            }
        }
    }
}

@Composable
fun Car() {
    Dialog(onDismissRequest = {}) {
        OutlinedCard(
            modifier = Modifier.aspectRatio(1.2F),
            elevation = CardDefaults.outlinedCardElevation(15.dp),
        ) {
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Please input validate code below:",
                    style = MaterialTheme.typography.titleMedium,
                )
                Spacer(modifier = Modifier.padding(10.dp))


                Spacer(modifier = Modifier.padding(5.dp))


            }
        }
    }
}

@Composable
fun MyIndicator(indicatorProgress: Float) {
    var progress by remember { mutableStateOf(0f) }
    val progressAnimDuration = 1500
    val progressAnimation by animateFloatAsState(
        targetValue = indicatorProgress,
        animationSpec = tween(durationMillis = progressAnimDuration, easing = FastOutSlowInEasing)
    )
    LinearProgressIndicator(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp)), // Rounded edges
        progress = progressAnimation
    )
    LaunchedEffect(indicatorProgress) {
        progress = indicatorProgress
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