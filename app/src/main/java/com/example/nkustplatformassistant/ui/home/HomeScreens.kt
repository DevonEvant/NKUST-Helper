package com.example.nkustplatformassistant.ui.home

import androidx.compose.foundation.layout.Spacer
import androidx.compose.material.Text
import androidx.compose.material.Icon
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.filled.Menu
import com.example.nkustplatformassistant.ui.theme.NkustPlatformAssistantTheme
import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxWidth
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

@Composable
fun ToolBar() {
    CenterAlignedTopAppBar(
        title = { Text("NKUST Platform Assistant") },
        navigationIcon = {
            IconButton(onClick = { /* doSomething() */ }) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "Localized description"
                )
            }
        },
        actions = {
            IconButton(onClick = { /* doSomething() */ }) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "Localized description"
                )
            }
        }
    )
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    NkustPlatformAssistantTheme {
        Greeting("貓貓")
    }
}


//@Preview(showBackground = true)
//@Preview(
//    uiMode = Configuration.UI_MODE_NIGHT_YES,
//    showBackground = true)
@Composable
fun DefaultPreview1() {
    NkustPlatformAssistantTheme {
        ToolBar()
    }
}