package com.example.nkustplatformassistant.ui.home

import android.content.res.Configuration
import android.graphics.Color
import android.graphics.Color.*
import androidx.compose.foundation.layout.*
import com.example.nkustplatformassistant.ui.theme.Nkust_platform_assistantTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.R
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.material3.rememberDrawerState
import kotlinx.coroutines.launch
import androidx.compose.ui.graphics.Color.Companion

@Composable
fun HomeScreens() {

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToolBar() {
    CenterAlignedTopAppBar(
        title = { Text("NKUST Platform Assistant") },
        navigationIcon = {
            IconButton(onClick = { /* doSomething() */ }) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "Menu"
                )
            }
        },
        actions = {
            IconButton(onClick = { /* doSomething() */ }) {
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = "Feature"
                )
            }
        }
    )
}

@Composable
fun bottomBar() {
    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Filled.ShoppingCart, contentDescription = null) },
            label = { Text("校車系統") },
            selected = true,
            onClick = {  }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Star, contentDescription = null) },
            label = { Text("學期課表") },
            selected = false,
            onClick = {  }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.KeyboardArrowDown, contentDescription = null) },
            label = { Text("學期成績") },
            selected = false,
            onClick = {  }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationDrawerExample() {
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    ModalNavigationDrawer(
        drawerContent = {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Button(
                    onClick = { scope.launch { drawerState.close() } },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Companion.Red
                    )
                ) {
                    Text("123")
                }
            }
        },
        drawerState = drawerState
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Button(onClick = { scope.launch { drawerState.open() } }) {
                Text("1234")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun etxtPreview() {

}


//@Preview(
//    uiMode = Configuration.UI_MODE_NIGHT_YES,
//    showBackground = true)
//@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Nkust_platform_assistantTheme {
        Column {
//            ToolBar()
//            bottomBar()
            NavigationDrawerExample()

        }
    }
}




