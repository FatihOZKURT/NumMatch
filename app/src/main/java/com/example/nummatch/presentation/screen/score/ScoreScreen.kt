package com.example.nummatch.presentation.screen.score

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.material3.Icon
import androidx.compose.material3.TextButton
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.nummatch.R
import com.example.nummatch.domain.model.Score
import com.example.nummatch.presentation.theme.Bronze
import com.example.nummatch.presentation.theme.FirstScore
import com.example.nummatch.presentation.theme.Gold
import com.example.nummatch.presentation.theme.OtherScores
import com.example.nummatch.presentation.theme.SecondScore
import com.example.nummatch.presentation.theme.Silver
import com.example.nummatch.presentation.theme.ThirdScore
import kotlin.collections.isNotEmpty

@Composable
fun ScoreScreen(
    modifier: Modifier = Modifier,
    viewModel: ScoreViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .fillMaxSize()
    ) {
        // Header
        ScoreHeader(
            onBackClick = onBackClick
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Statistics
        if (uiState.scores.isNotEmpty()) {
            ScoreStatistics(
                totalScores = uiState.totalScores,
                averageScore = uiState.averageScore,
                highestScore = uiState.highestScore
            )

            Spacer(modifier = Modifier.height(16.dp))
        }

        // Content
        ScoreContent(
            uiState = uiState,
            onRefresh = viewModel::refreshScores,
            onClearError = viewModel::clearError
        )
    }
}

@Composable
private fun ScoreHeader(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxWidth()
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier.align(Alignment.CenterStart)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = stringResource(R.string.back)
            )
        }

        Text(
            text = stringResource(R.string.scores),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
private fun ScoreStatistics(
    totalScores: Int,
    averageScore: Double,
    highestScore: Int,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.statistics),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatItem(stringResource(R.string.total_games), totalScores.toString())
                StatItem(stringResource(R.string.average), "%.1f".format(averageScore))
                StatItem(stringResource(R.string.best_score), highestScore.toString())
            }

        }
    }
}

@Composable
private fun StatItem(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun ScoreContent(
    uiState: ScoreUiState,
    onRefresh: () -> Unit,
    onClearError: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        when {
            uiState.isLoading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            uiState.error != null -> {
                ErrorMessage(
                    error = uiState.error,
                    onRetry = onRefresh,
                    onDismiss = onClearError,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            uiState.isEmpty -> {
                EmptyScoresMessage(
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            uiState.hasNoResults -> {
                NoResultsMessage(
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            else -> {
                ScoreList(
                    scores = uiState.filteredScores,
                )
            }
        }
    }
}

@Composable
private fun ScoreList(
    scores: List<Score>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        itemsIndexed(scores) { index, score ->
            val backgroundColor = when (index) {
                0 -> FirstScore
                1 -> SecondScore
                2 -> ThirdScore
                else -> OtherScores
            }

            ScoreItem(
                rank = index + 1,
                score = score,
                backgroundColor = backgroundColor
            )
        }
    }
}

@Composable
private fun ScoreItem(
    rank: Int,
    score: Score,
    backgroundColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            RankDisplay(rank = rank)
            Spacer(modifier = Modifier.width(16.dp))

            // User Name
            Text(
                text = score.userName,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = if (rank <= 3) FontWeight.Bold else FontWeight.Normal,
                color = MaterialTheme.colorScheme.tertiary
            )

            // Score
            Text(
                text = score.score.toString(),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.tertiary
            )

        }
    }
}

@Composable
private fun RankDisplay(
    rank: Int,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.size(32.dp),
        contentAlignment = Alignment.Center
    ) {
        when (rank) {
            1 -> Icon(
                imageVector = Icons.Default.EmojiEvents,
                contentDescription = stringResource(R.string.first_place),
                tint = Gold
            )

            2 -> Icon(
                imageVector = Icons.Default.EmojiEvents,
                contentDescription = stringResource(R.string.second_place),
                tint = Silver
            )

            3 -> Icon(
                imageVector = Icons.Default.EmojiEvents,
                contentDescription = stringResource(R.string.third_place),
                tint = Bronze
            )

            else -> Text(
                text = "$rank.",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun EmptyScoresMessage(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.EmojiEvents,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.no_scores),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun NoResultsMessage(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.no_results_found),
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = stringResource(R.string.try_adjusting_your_filters),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
private fun ErrorMessage(
    error: String,
    onRetry: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Error,
            contentDescription = null,
            modifier = Modifier.size(48.dp),
            tint = MaterialTheme.colorScheme.error
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.error_loading_scores),
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = error,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        Row {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.dismiss))
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = onRetry) {
                Text(stringResource(R.string.retry))
            }
        }
    }
}