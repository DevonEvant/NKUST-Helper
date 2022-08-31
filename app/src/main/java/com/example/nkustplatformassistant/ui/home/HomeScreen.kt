package com.example.nkustplatformassistant.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.nkustplatformassistant.ui.theme.Nkust_platform_assistantTheme

@Composable
fun HomeScreen(name: String, btnOnClick: () -> Unit) {
    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Hello $name, Welcome Home!")
        Text(text = "Navigate To Login page!")

        Button(onClick = { btnOnClick.invoke() }) {
            Text(text = "Press me!")
        }
    }

}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Nkust_platform_assistantTheme {
        HomeScreen("Brad", {})
    }
}