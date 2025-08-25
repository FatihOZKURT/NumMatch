package com.example.nummatch.presentation.component.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.nummatch.domain.model.Score
import com.example.nummatch.presentation.component.card.ScoreCard
import com.example.nummatch.presentation.theme.FirstScore
import com.example.nummatch.presentation.theme.OtherScores
import com.example.nummatch.presentation.theme.SecondScore
import com.example.nummatch.presentation.theme.ThirdScore

@Composable
fun ScoreList(
    scores: List<Score>,
    modifier: Modifier = Modifier,
    listState: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(4.dp),
    showRankColors: Boolean = true,
    customRankColors: List<Color>? = null,
    onScoreClick: ((Int, Score) -> Unit)? = null
) {
    val rankColors = customRankColors ?: defaultRankColors()

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        state = listState,
        contentPadding = contentPadding,
        verticalArrangement = verticalArrangement
    ) {
        itemsIndexed(scores) { index, score ->
            val backgroundColor = if (showRankColors) {
                getRankBackgroundColor(index, rankColors)
            } else {
                OtherScores
            }

            ScoreCard(
                rank = index + 1,
                score = score,
                backgroundColor = backgroundColor,
                modifier = if (onScoreClick != null) {
                    Modifier.clickable { onScoreClick(index, score) }
                } else {
                    Modifier
                }
            )
        }
    }
}

private fun defaultRankColors(): List<Color> = listOf(
    FirstScore,
    SecondScore,
    ThirdScore,
    OtherScores
)

private fun getRankBackgroundColor(index: Int, rankColors: List<Color>): Color {
    return when (index) {
        0 -> rankColors.getOrElse(0) { OtherScores }
        1 -> rankColors.getOrElse(1) { OtherScores }
        2 -> rankColors.getOrElse(2) { OtherScores }
        else -> rankColors.getOrElse(3) { OtherScores }
    }
}
