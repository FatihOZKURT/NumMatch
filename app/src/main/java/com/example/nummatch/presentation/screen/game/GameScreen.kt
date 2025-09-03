package com.example.nummatch.presentation.screen.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.nummatch.domain.model.GameCard
import com.example.nummatch.presentation.component.card.GameCard
import com.example.nummatch.presentation.component.dialog.GameResultDialog
import com.example.nummatch.presentation.component.header.GameHeader
import com.example.nummatch.util.Difficulty
import com.example.nummatch.presentation.screen.settings.SettingsViewModel

@Composable
fun GameScreen(
    difficulty: Difficulty,
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: GameViewModel = hiltViewModel<GameViewModel>(),
    userName: String
) {
    val uiState by viewModel.uiState.collectAsState()
    val settingsViewModel: SettingsViewModel = hiltViewModel()
    val settingsUiState by settingsViewModel.uiState.collectAsState()

    LaunchedEffect(difficulty, settingsUiState.isTimerVisible) {
        viewModel.initializeGame(difficulty, settingsUiState.isTimerVisible)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = if (uiState.cards.size == 24) 8.dp else 16.dp)
    ) {
        // Header: Score and Time
        GameHeader(
            score = uiState.score,
            timeLeft = uiState.timeLeft,
            isTimerVisible = uiState.isTimerVisible,
            progress = uiState.progress
        )

        Spacer(modifier = Modifier.height(if (uiState.cards.size == 24) 8.dp else 16.dp))

        // Game Cards
        GameBoard(
            cards = uiState.cards,
            onCardClick = viewModel::onCardClick,
            modifier = Modifier.fillMaxSize()
        )
    }
    GameResultDialog(
        gameResult = uiState.gameResult,
        score = uiState.score,
        userName = userName,
        difficulty = difficulty,
        onSaveScore = viewModel::saveScore,
        onResetResult = viewModel::resetResult,
        onRestartGame = viewModel::restartGame,
        navController = navController
    )
}

@Composable
private fun GameBoard(
    cards: List<GameCard>,
    onCardClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        val columnCount = when {
            cards.size <= 16 -> 4
            cards.size == 20 -> 5
            cards.size == 24 -> 4
            else -> 6
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(columnCount),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxWidth(if (cards.size == 24) 0.98f else 0.95f)
                .aspectRatio(
                    when {
                        cards.size == 16 -> 1f
                        cards.size == 24 -> 0.65f
                        else -> 1f
                    }
                )
        ) {
            items(cards.size) { index ->
                GameCard(
                    card = cards[index],
                    onClick = { onCardClick(index) }
                )
            }
        }
    }
}


