package com.example.nkustplatformassistant

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.nkustplatformassistant.data.remote.NkustUser
import com.example.nkustplatformassistant.ui.theme.Nkust_platform_assistantTheme


class HomeScreen : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Nkust_platform_assistantTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                }
            }
        }
    }
}

// TODO(1. On system navigate back: logoff
//      2. A list for (1. class time table
//                     2. school calender
//                     3. specific function))

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview(
) {
    Nkust_platform_assistantTheme {
        Greeting1("Android111111")
    }
}

@Composable
@Preview(showBackground = true)
fun WhateverPreview() {
    Nkust_platform_assistantTheme {
        ComposeCard({}, {})
    }
}

/* Reference:
 * https://stackoverflow.com/questions/67897100/jetpack-compose-textfield-capture-keyboard-enter-input
 * https://stackoverflow.com/questions/65304229/toggle-password-field-jetpack-compose
 * https://medium.com/google-developer-experts/focus-in-jetpack-compose-6584252257fe
 * TODO: TextField Style improvement
 * https://pratikchauhan11.medium.com/playing-with-textfield-in-compose-android-declarative-ui-de8c03aa4748
 */

@Composable
fun Greeting1(name: String) {
    Text(text = "Hello $name!")
}



@Composable
fun ComposeCard(
    onPositiveClick: () -> Unit,
    onNegativeClick: () -> Unit,
) {
    OutlinedCard(
        elevation = CardDefaults.outlinedCardElevation(15.dp),
    )
    {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(text = "Please input validate code:")
            Spacer(modifier = Modifier.padding(10.dp))
            Image(
                painter = painterResource(id = R.drawable.validate_code),
                contentDescription = null,
                alignment = Alignment.Center,
                modifier = Modifier.aspectRatio(2F),
            )
            Spacer(modifier = Modifier.padding(10.dp))

            //button
            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedButton(onClick = onNegativeClick) {
                    Icon(imageVector = Icons.TwoTone.Cancel, contentDescription = null)
                    Text(text = "Cancel login")
                }
                Spacer(modifier = Modifier.width(4.dp))
                OutlinedButton(onClick = {
                    onPositiveClick.invoke()
                }) {
                    Icon(imageVector = Icons.TwoTone.Verified, contentDescription = null)
                    Text(text = "Login")
                }
            }
        }
    }
}

