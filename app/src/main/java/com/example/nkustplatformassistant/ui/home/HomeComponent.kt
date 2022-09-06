package com.example.nkustplatformassistant.ui.home

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
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
    LaunchedEffect(Unit){
        homeViewModel.startFetch()
    }

    LinearProgressIndicator(modifier = Modifier
        .padding(top = 0.dp)
        .fillMaxWidth(), progress = homeViewModel.progress.value!!)

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Spacer(modifier = Modifier.padding(20.dp))
        Text(text = "Hello, Welcome Home!")
        Text(text = "Navigate To Login page!")

        Button(onClick = {
//            homeViewModel.navController.navigate(Screen.Login.route)
        }) {
            Text(text = "Press me!")
        }

        Spacer(modifier = Modifier.padding(vertical = 50.dp))

        Column() {
            LazyRow() {

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
            Toast.makeText(context,
                "It seems database is null, please re-login to get data from web again.",
                Toast.LENGTH_LONG).show()
            navController.navigate(Screen.Login.route)
        }
    }
}