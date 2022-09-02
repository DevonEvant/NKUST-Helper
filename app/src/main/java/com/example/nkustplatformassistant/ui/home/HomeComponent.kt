package com.example.nkustplatformassistant.ui.home

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.nkustplatformassistant.navigation.Screen

@Composable
fun HomeScreenBase(homeViewModel: HomeViewModel, navController: NavController) {
    val context = LocalContext.current
    if (homeViewModel.isDataReady.observeAsState(false).value) {
        Column(
            modifier = Modifier
                .padding(vertical = 20.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
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
    } else {
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

}