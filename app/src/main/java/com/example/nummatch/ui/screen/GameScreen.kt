package com.example.nummatch.ui.screen

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
import androidx.compose.material3.Scaffold
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
import com.example.nummatch.model.GameCard
import com.example.nummatch.model.GameResult
import com.example.nummatch.ui.theme.EnableButtonBackground
import com.example.nummatch.util.Difficulty
import com.example.nummatch.viewmodel.GameViewModel
import com.example.nummatch.viewmodel.SettingsViewModel

@Composable
fun GameScreen(
    difficulty: Difficulty,
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: GameViewModel = hiltViewModel<GameViewModel>(),
    userName: String
) {
    val score = viewModel.score
    val timeLeft = viewModel.timeLeft
    val cards = viewModel.cards
    val gameResult = viewModel.gameResult.value
    val settingsViewModel: SettingsViewModel = hiltViewModel()
    val isTimerVisible by settingsViewModel.isTimerVisible.collectAsState()

    LaunchedEffect(difficulty, isTimerVisible) {
        viewModel.initializeGame(difficulty, isTimerVisible)
    }

    Scaffold(modifier = modifier) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .padding(innerPadding)
        ) {
            // Üst Bilgi: Skor ve Süre
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
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
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxSize(),
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
                            onClick = { viewModel.onCardClick(index) }
                        )
                    }
                }
            }
        }
    }

    // Kazanma durumu
    if (gameResult == GameResult.Win) {
        AlertDialog(
            onDismissRequest = {},
            confirmButton = {
                TextButton(onClick = {
                    viewModel.saveScore(userName, score)
                    viewModel.resetResult()
                    navController.navigate("score_screen")
                }) {
                    Text(stringResource(R.string.save_score))
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    viewModel.resetResult()
                    navController.popBackStack()
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

    // Kaybetme durumu
    if (gameResult == GameResult.Lose) {
        AlertDialog(
            onDismissRequest = {},
            confirmButton = {
                TextButton(onClick = {
                    viewModel.restartGame(difficulty)
                    viewModel.resetResult()
                }) {
                    Text(stringResource(R.string.play_again))
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    viewModel.resetResult()
                    navController.popBackStack()
                }) {
                    Text(stringResource(R.string.close))
                }
            },
            title = { Text(stringResource(R.string.time_is_up), color = EnableButtonBackground) },
            text = { Text(stringResource(R.string.time_expired)) }
        )
    }
}

@Composable
fun GameCardView(card: GameCard, onClick: () -> Unit) {
    val rotation by animateFloatAsState(
        targetValue = if (card.isRevealed || card.isMatched) 180f else 0f,
        animationSpec = tween(durationMillis = 400),
        label = "card_flip"
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
            // Kart açık yüzü
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
                    contentDescription = "Card Front",
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier.fillMaxSize()
                )
                Text(
                    text = card.number.toString(),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (card.isMatched) Color(0xFF4CAF50) else Color.Black
                )
            }
        } else {
            // Kart arka yüzü
            Image(
                painter = painterResource(id = R.drawable.card_design),
                contentDescription = "Card Back",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

