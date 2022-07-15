package com.example.nkustplatformassistant.ui.home

import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
//import androidx.compose.material.icons.filled.Menu
import com.example.nkustplatformassistant.ui.theme.Nkust_platform_assistantTheme
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Modifier
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.IconButton


@Composable
fun HomeScreens(){

}

//@Composable
//fun ToolBar() {
//    CenterAlignedTopAppBar(
//        title = { Text("NKUST Platform Assistant") },
//        navigationIcon = {
//            IconButton(onClick = { /* doSomething() */ }) {
//                Icon(
//                    imageVector = Icons.Filled.Menu,
//                    contentDescription = "Localized description"
//                )
//            }
//        },
//        actions = {
//            IconButton(onClick = { /* doSomething() */ }) {
//                Icon(
//                    imageVector = Icons.Filled.Menu,
//                    contentDescription = "Localized description"
//                )
//            }
//        }
//    )
//}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Nkust_platform_assistantTheme {
        Greeting("11")
    }
}


//@Preview(showBackground = true)
//@Preview(
//    uiMode = Configuration.UI_MODE_NIGHT_YES,
//    showBackground = true)
//@Composable
//fun DefaultPreview1() {
//    Nkust_platform_assistantTheme {
//        ToolBar()
//    }
//}