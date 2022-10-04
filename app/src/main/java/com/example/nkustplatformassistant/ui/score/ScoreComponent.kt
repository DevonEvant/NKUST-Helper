package com.example.nkustplatformassistant.ui.score

import android.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.Color.Companion.Yellow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.navigation.NavController
import com.example.nkustplatformassistant.data.persistence.DataRepository
import com.example.nkustplatformassistant.data.persistence.db.entity.ScoreEntity
import com.example.nkustplatformassistant.ui.theme.Nkust_platform_assistantTheme

@Composable
fun ScoreContent(scoreViewModel: ScoreViewModel, navController: NavController) {

    val scores by scoreViewModel.scores.observeAsState()

    Column(
        verticalArrangement = Arrangement.spacedBy(20.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(
                PaddingValues(
                    start = 12.dp, top = 16.dp, end = 12.dp, bottom = 16.dp
                )
            )
    ) {

        scores?.let { ScoresDataTable(it) } ?: Card {
            Text(text = "Score not found")
        }

        Row() {
            Text(
                "操行成績",
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )
            Text(
                "12",
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )
        }

        Row() {
            Text(
                "總平均",
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )
            Text(
                "12",
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )
        }

        Row() {
            Text(
                "班名次",
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )
            Text(
                "12",
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )
        }

        Row() {
            Text(
                "系名次",
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )
            Text(
                "12",
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )
        }

    }
}

@Composable
fun ScoresDataTable(scores: List<ScoreEntity?>) {

    val tableLine = @Composable { Divider() }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
            .border(1.dp, DividerDefaults.color, RoundedCornerShape(15.dp))
            .padding(vertical = 10.dp)
    ) {
        item {
            Row() {
                Text(
                    "Subject",
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    "Mid score",
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    "Final score",
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item { tableLine() }

        scores.forEachIndexed { index, score ->
            item {
                Row() {
                    score?.subjectName?.let {
                        Text(
                            it,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    score?.midScore?.let {
                        Text(
                            it,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    score?.finalScore?.let {
                        Text(
                            it,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            if (index < scores.lastIndex)
                item { tableLine() }
        }

    }
}


@Composable
@Preview(showBackground = true)
fun LoginScreenPreview() {
    val context = LocalContext.current
    Nkust_platform_assistantTheme {
        val scoreViewModel = ScoreViewModel(DataRepository(context))
        scoreViewModel.rS(
            mutableListOf(
                ScoreEntity(
                    1,
                    2,
                    "3",
                    "123",
                    "s1",
                    "m1",
                ),
                ScoreEntity(
                    1,
                    2,
                    "3",
                    "123",
                    "s1",
                    "m1",
                ),
                ScoreEntity(
                    1,
                    2,
                    "3",
                    "123",
                    "s1",
                    "m1",
                ),
            )
        )
        val navController = null
//        ScoreContent(scoreViewModel, navController)
    }
}
