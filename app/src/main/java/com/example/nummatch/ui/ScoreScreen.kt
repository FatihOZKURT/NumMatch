package com.example.nummatch.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.nummatch.R
import com.example.nummatch.model.Score
import com.example.nummatch.ui.theme.FirstScore
import com.example.nummatch.ui.theme.OtherScores
import com.example.nummatch.ui.theme.SecondScore
import com.example.nummatch.ui.theme.ThirdScore
import com.example.nummatch.viewmodel.ScoreViewModel
import kotlin.collections.sortedByDescending

@Composable
fun ScoreScreen(
    modifier: Modifier = Modifier,
    viewModel: ScoreViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {}
) {
    val scores by viewModel.scoreList.collectAsState()
    val sortedScores = scores.sortedByDescending { it.score }

    Scaffold(modifier = modifier) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            // Geri Butonu ve Başlık
            Box(modifier = Modifier.fillMaxWidth()) {
                IconButton(
                    onClick = { onBackClick() },
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back)
                    )
                }

                Text(
                    text = stringResource(R.string.top_scores),
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (sortedScores.isEmpty()) {
                // Boş liste mesajı
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.no_scores),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    itemsIndexed(sortedScores) { index, score ->
                        val backgroundColor = when (index) {
                            0 -> FirstScore
                            1 -> SecondScore
                            2 -> ThirdScore
                            else -> OtherScores
                        }
                        ScoreItem(rank = index + 1, score = score, backgroundColor = backgroundColor)
                    }
                }

            }
        }
    }
}

@Composable
fun ScoreItem(rank: Int, score: Score, backgroundColor: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .padding(vertical = 8.dp, horizontal = 8.dp)
    ) {
        Text(
            text = "$rank.",
            modifier = Modifier.width(40.dp),
            style = MaterialTheme.typography.bodyLarge
        )

        Text(
            text = score.userName,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyLarge
        )

        Text(
            text = score.score.toString(),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}



