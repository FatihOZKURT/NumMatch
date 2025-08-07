package com.example.nummatch.presentation.screen.game

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.nummatch.R
import com.example.nummatch.domain.model.GameCard
import com.example.nummatch.domain.model.GameResult
import com.example.nummatch.presentation.theme.EnableButtonBackground
import com.example.nummatch.util.Difficulty
import com.example.nummatch.presentation.screen.settings.SettingsViewModel
import com.example.nummatch.presentation.theme.Green
import com.example.nummatch.presentation.theme.Red
import com.example.nummatch.presentation.theme.TextLight

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
            .padding(horizontal = 16.dp)
    ) {
        // Header: Score and Time
        GameHeader(
            score = uiState.score,
            timeLeft = uiState.timeLeft,
            isTimerVisible = uiState.isTimerVisible,
            progress = uiState.progress
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Game Cards
        GameBoard(
            cards = uiState.cards,
            onCardClick = viewModel::onCardClick,
            modifier = Modifier.fillMaxSize()
        )
    }

    // Game Result Dialogs
    GameResultDialogs(
        gameResult = uiState.gameResult,
        score = uiState.score,
        userName = userName,
        difficulty = difficulty,
        onSaveScore = viewModel::saveScore,
        onResetResult = viewModel::resetResult,
        onRestartGame = viewModel::restartGame,
        onNavigateToScores = { navController.navigate("score_screen") },
        onNavigateBack = { navController.popBackStack() }
    )
}

@Composable
private fun GameHeader(
    score: Int,
    timeLeft: Int,
    isTimerVisible: Boolean,
    progress: Float,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = stringResource(R.string.score_format, score),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            if (isTimerVisible) {
                Text(
                    text = stringResource(R.string.time_format, timeLeft),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (timeLeft <= 10) Red else Color.Unspecified
                )
            }
        }

        // Progress bar for matched pairs
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp),
            color = MaterialTheme.colorScheme.primary,
            trackColor = ProgressIndicatorDefaults.linearTrackColor,
            strokeCap = ProgressIndicatorDefaults.CircularIndeterminateStrokeCap
        )
    }
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
        val columnCount = if (cards.size == 16) 4 else 6

        LazyVerticalGrid(
            columns = GridCells.Fixed(columnCount),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .aspectRatio(1f)
        ) {
            items(cards.size) { index ->
                GameCardView(
                    card = cards[index],
                    onClick = { onCardClick(index) }
                )
            }
        }
    }
}

@Composable
private fun GameResultDialogs(
    gameResult: GameResult,
    score: Int,
    userName: String,
    difficulty: Difficulty,
    onSaveScore: (String, Int) -> Unit,
    onResetResult: () -> Unit,
    onRestartGame: (Difficulty) -> Unit,
    onNavigateToScores: () -> Unit,
    onNavigateBack: () -> Unit
) {
    when (gameResult) {
        GameResult.Win -> {
            WinDialog(
                score = score,
                userName = userName,
                onSaveScore = onSaveScore,
                onResetResult = onResetResult,
                onNavigateToScores = onNavigateToScores,
                onNavigateBack = onNavigateBack
            )
        }

        GameResult.Lose -> {
            LoseDialog(
                difficulty = difficulty,
                onRestartGame = onRestartGame,
                onResetResult = onResetResult,
                onNavigateBack = onNavigateBack
            )
        }
        GameResult.None -> {}
    }
}

@Composable
private fun WinDialog(
    score: Int,
    userName: String,
    onSaveScore: (String, Int) -> Unit,
    onResetResult: () -> Unit,
    onNavigateToScores: () -> Unit,
    onNavigateBack: () -> Unit
) {
    AlertDialog(
        onDismissRequest = {},
        confirmButton = {
            TextButton(onClick = {
                onSaveScore(userName, score)
                onResetResult()
                onNavigateToScores()
            }) {
                Text(stringResource(R.string.save_score))
            }
        },
        dismissButton = {
            TextButton(onClick = {
                onResetResult()
                onNavigateBack()
            }) {
                Text(stringResource(R.string.close))
            }
        },
        title = {
            Text(
                stringResource(R.string.congratulations),
                color = EnableButtonBackground
            )
        },
        text = {
            Text(stringResource(R.string.matched_all_cards))
        }
    )
}

@Composable
private fun LoseDialog(
    difficulty: Difficulty,
    onRestartGame: (Difficulty) -> Unit,
    onResetResult: () -> Unit,
    onNavigateBack: () -> Unit
) {
    AlertDialog(
        onDismissRequest = {},
        confirmButton = {
            TextButton(onClick = {
                onRestartGame(difficulty)
                onResetResult()
            }) {
                Text(stringResource(R.string.play_again))
            }
        },
        dismissButton = {
            TextButton(onClick = {
                onResetResult()
                onNavigateBack()
            }) {
                Text(stringResource(R.string.close))
            }
        },
        title = {
            Text(
                stringResource(R.string.time_is_up),
                color = EnableButtonBackground
            )
        },
        text = {
            Text(stringResource(R.string.time_expired))
        }
    )
}

@Composable
fun GameCardView(card: GameCard, onClick: () -> Unit) {
    val rotation by animateFloatAsState(
        targetValue = if (card.isRevealed || card.isMatched) 180f else 0f,
        animationSpec = tween(durationMillis = 400),
        label = stringResource(R.string.card_flip)
    )

    val isFront = rotation >= 90f

    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .graphicsLayer {
                rotationY = rotation
                cameraDistance = 8 * density
            }
            .clickable(enabled = !card.isMatched && !card.isRevealed) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        if (isFront) {
            //  Card Front
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        rotationY = 180f
                    },
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.card_design_empty),
                    contentDescription = stringResource(R.string.card_front),
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier.fillMaxSize()
                )
                Text(
                    text = card.number.toString(),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (card.isMatched) Green else TextLight
                )
            }
        } else {
            // Card Back
            Image(
                painter = painterResource(id = R.drawable.card_design),
                contentDescription = stringResource(R.string.card_back),
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

