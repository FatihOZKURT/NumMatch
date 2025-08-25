package com.example.nummatch.presentation.screen.score

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.nummatch.R
import com.example.nummatch.presentation.component.card.StatItem
import com.example.nummatch.presentation.component.card.StatisticsCard
import com.example.nummatch.presentation.component.header.ScreenHeader
import com.example.nummatch.presentation.component.list.ScoreList
import com.example.nummatch.presentation.component.state.EmptyScoresState
import com.example.nummatch.presentation.component.state.NoSearchResultsState
import com.example.nummatch.presentation.component.state.FullScreenErrorMessage
import com.example.nummatch.presentation.component.state.FullScreenLoadingIndicator
import com.example.nummatch.presentation.navigation.Screen

@Composable
fun ScoreScreen(
    modifier: Modifier = Modifier,
    viewModel: ScoreViewModel = hiltViewModel(),
    navController: NavHostController,
    onBackClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .fillMaxSize()
    ) {
        // Header
        ScreenHeader(
            title = stringResource(R.string.scores),
            onBackClick = onBackClick
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (uiState.scores.isNotEmpty()) {
            StatisticsCard(
                title = stringResource(R.string.statistics),
                stats = listOf(
                    StatItem(
                        label = stringResource(R.string.total_games),
                        value = uiState.totalScores.toString(),
                        valueColor = MaterialTheme.colorScheme.primary
                    ),
                    StatItem(
                        label = stringResource(R.string.average),
                        value = "%.1f".format(uiState.averageScore),
                        valueColor = MaterialTheme.colorScheme.primary
                    ),
                    StatItem(
                        label = stringResource(R.string.best_score),
                        value = uiState.highestScore.toString(),
                        valueColor = MaterialTheme.colorScheme.primary
                    )
                )
            )

            Spacer(modifier = Modifier.height(16.dp))
        }

        when {
            uiState.isLoading -> {
                FullScreenLoadingIndicator(
                    message = stringResource(R.string.loading_scores)
                )
            }

            uiState.error != null -> {
                FullScreenErrorMessage(
                    message = uiState.error!!,
                    onRetry = viewModel::refreshScores,
                    onDismiss = viewModel::clearError
                )
            }

            uiState.isEmpty -> {
                EmptyScoresState(
                    onStartGame = { navController.navigate(Screen.GameSetup.route) }
                )
            }

            uiState.hasNoResults -> {
                NoSearchResultsState(
                    searchQuery = uiState.searchQuery,
                    onClearFilters = {
                        viewModel.clearFilters()
                    }
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