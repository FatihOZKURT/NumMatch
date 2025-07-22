package com.example.nummatch.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.nummatch.R
import com.example.nummatch.data.Score
import com.example.nummatch.ui.theme.NumMatchTheme
import kotlin.collections.sortedByDescending

@Composable
fun ScoreScreen(
    modifier: Modifier = Modifier,
    scores: List<Score>
) {
    val sortedScores = scores.sortedByDescending { it.score }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(R.string.top_scores),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 16.dp)
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            itemsIndexed(sortedScores) { index, score ->
                ScoreItem(rank = index + 1, score = score)
            }
        }
    }
}

@Composable
fun ScoreItem(rank: Int, score: Score) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = "$rank.",
            modifier = Modifier.width(40.dp),
            style = MaterialTheme.typography.bodyLarge
        )

        Text(
            text = score.username,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyLarge
        )

        Text(
            text = score.score.toString(),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ScoresScreenPreview() {
    val sampleScores = listOf(
        Score("Fatih", 150),
        Score("Ayşe", 200),
        Score("John", 120),
        Score("Zeynep", 180)
    )

    NumMatchTheme {
        ScoreScreen(scores = sampleScores)
    }
}


