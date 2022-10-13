package com.example.nkustplatformassistant.ui.score

import android.content.res.Resources
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
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
import com.example.nkustplatformassistant.navigation.Screen
import com.example.nkustplatformassistant.ui.curriculum.SemesterSelector
import com.example.nkustplatformassistant.ui.theme.Nkust_platform_assistantTheme
import java.security.AccessController.getContext

@Composable
fun ScoreContent(scoreViewModel: ScoreViewModel, navController: NavController) {

    val scores by scoreViewModel.scores.observeAsState()
    val scoreOther by scoreViewModel.scoreOther.observeAsState(
        ScoreOtherEntity(
            "-1", "-1", "-1", "", "",
            null, null, null, null,
        )
    )

    val scoreDropDownList by scoreViewModel.scoreDropDownList.observeAsState(listOf())

    Column {
        Row(horizontalArrangement = Arrangement.spacedBy(5.dp),
            verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { navController.navigate(Screen.Home.route) }) {
                Icon(imageVector = Icons.TwoTone.ArrowBack, contentDescription = null)
            }

            Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                if (scoreDropDownList.isNotEmpty()) {
                    SemesterSelector(scoreDropDownList) {
                        scoreViewModel.onSelectDropDownChange(it)
                    }
                }
            }
        }

        Divider()

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier
                .padding(PaddingValues(start = 12.dp, top = 16.dp, end = 12.dp, bottom = 16.dp))
        ) {
            item {
                scores?.let { ScoresDataTable(it) } ?: Card {
                    Text(text = "Score not found")
                }
            }

            item {
                ScoreOtherWidget(scoreOther)
            }
        }
    }
}

@Composable
fun ScoreOtherWidget(scoreOther: ScoreOtherEntity) {
    val localContext = LocalContext.current
    val displayList: List<Pair<Int, Any>> = listOf(
        R.string.score_beheavior to scoreOther.behaviorScore.ifEmpty { localContext.getString(R.string.nullstring) },
        R.string.score_average to scoreOther.average.ifEmpty { localContext.getString(R.string.nullstring) },
        R.string.score_class_ranking to
                if (scoreOther.classRanking.toString() == "null") localContext.getString(R.string.nullstring)
                else scoreOther.classRanking.toString(),
        R.string.score_dept_ranking to
                if (scoreOther.deptRanking.toString() == "null") localContext.getString(R.string.nullstring)
                else scoreOther.deptRanking.toString()
    )

    Row {
        Column(
            modifier = Modifier
                .weight(0.5F)
                .fillMaxSize()
                .padding(start = 8.dp),
            horizontalAlignment = Alignment.Start

        ) {
            for (item in displayList) {
                Text(
                    stringResource(item.first),
                    fontWeight = FontWeight.Bold,
                )
                Text(item.second.toString())
            }
        }

        @Composable
        fun GradeBar(percent: Float) {
            Canvas(modifier = Modifier
                .height(5.dp)
                .fillMaxWidth(0.7F)) {
                drawRoundRect(color = Color.Red)
                drawRoundRect(color = Color.Green,
                    size = Size(width = size.width * (1 - percent),
                        height = size.height))

            }
        }

        Column(
            modifier = Modifier.weight(0.5F),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (scoreOther.deptRanking != null && scoreOther.deptPeople != null &&
                scoreOther.classRanking != null && scoreOther.classPeople != null
            ) {
                val classPercent: Float =
                    scoreOther.classRanking.toFloat() / scoreOther.classPeople
                val deptPercent: Float =
                    scoreOther.deptRanking.toFloat() / scoreOther.deptPeople

                Text(stringResource(R.string.score_class_ranking_rate,
                    classPercent * 100), fontWeight = FontWeight.Bold)
                GradeBar(percent = classPercent)
                Text(stringResource(R.string.score_dept_ranking_rate,
                    deptPercent * 100), fontWeight = FontWeight.Bold)
                GradeBar(percent = deptPercent)
            }
        }
    }
}

@Composable
fun ScoresDataTable(scores: List<ScoreEntity?>) {

    val tableLine = @Composable { Divider() }

    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
            .border(1.dp, DividerDefaults.color, RoundedCornerShape(15.dp))
            .padding(vertical = 10.dp)
    ) {

        Row {
            Text(
                stringResource(R.string.score_scoretable_subject),
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )
            Text(
                stringResource(R.string.score_scoretable_midscore),
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )
            Text(
                stringResource(R.string.score_scoretable_finalscore),
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )
        }

        tableLine()

        scores.forEachIndexed { index, score ->

            Row {
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

            if (index < scores.lastIndex)
                tableLine()
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
//        val navController = null
//        ScoreContent(scoreViewModel, navController)
    }
}
