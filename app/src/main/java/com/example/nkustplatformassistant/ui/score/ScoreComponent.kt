package com.example.nkustplatformassistant.ui.score

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.nkustplatformassistant.R
import com.example.nkustplatformassistant.data.persistence.DataRepository
import com.example.nkustplatformassistant.data.persistence.db.entity.ScoreEntity
import com.example.nkustplatformassistant.data.persistence.db.entity.ScoreOtherEntity
import com.example.nkustplatformassistant.ui.theme.Nkust_platform_assistantTheme

@Composable
fun ScoreContent(scoreViewModel: ScoreViewModel, navController: NavController) {

    val scores by scoreViewModel.scores.observeAsState()
    val scoreOther by scoreViewModel.scoreOther.observeAsState(
        ScoreOtherEntity(
            "-1", "-1", "-1", "0F", "0F",
            null, null, null, null,
        )
    )

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

        val classRanking = if (scoreOther.classRanking == null) 0 else scoreOther.classRanking!!
        val deptRanking = if (scoreOther.deptRanking == null) 0 else scoreOther.deptRanking!!

        val displayList: List<Pair<Int, Any>> = listOf(
            R.string.score_beheavior to scoreOther.behaviorScore,
            R.string.score_average to scoreOther.average,
            R.string.score_class_ranking to classRanking,
            R.string.score_dept_ranking to deptRanking
        )

        Row {
            for (item in displayList) {
                Text(stringResource(item.first), fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f))
                Text(
                    item.second.toString(),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f)
                )
            }
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
