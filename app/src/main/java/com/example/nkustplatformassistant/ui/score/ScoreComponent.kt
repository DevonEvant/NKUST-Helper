package com.example.nkustplatformassistant.ui.score

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.*
import androidx.compose.material3.AlertDialogDefaults.shape
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import com.example.nkustplatformassistant.data.persistence.db.entity.ScoreEntity
import com.example.nkustplatformassistant.ui.curriculum.CurriculumTimeCard
import com.example.nkustplatformassistant.ui.curriculum.WeeksCard
import com.example.nkustplatformassistant.ui.theme.Nkust_platform_assistantTheme
import com.soywiz.klogger.AnsiEscape

@Composable
fun ScoreContent(scoreViewModel: ScoreViewModel) {
    val scores by scoreViewModel.scores.observeAsState()

    Column() {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            contentPadding = PaddingValues(
                start = 12.dp,
                top = 16.dp,
                end = 12.dp,
                bottom = 16.dp
            )
        ) {
            scores?.let { scores ->
                items(scores.size) { index ->
                    val gridOrder = index % 3
                    val scoreOrder = index / 3

                    if (gridOrder == 0)
                        Text(scores[scoreOrder]!!.subjectName)
                    else if (gridOrder == 1)
                        Text(scores[scoreOrder]!!.midScore)
                    else if (gridOrder == 2)
                        Text(scores[scoreOrder]!!.finalScore)

                }
            }
        }
    }
}
