package com.example.nummatch.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.nummatch.R
import com.example.nummatch.model.GameCard
import com.example.nummatch.model.GameResult
import com.example.nummatch.viewmodel.GameViewModel

@Composable
fun GameScreen(
    difficulty: String,
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: GameViewModel = hiltViewModel<GameViewModel>(),
    userName: String
) {
    val score = viewModel.score
    val timeLeft = viewModel.timeLeft
    val cards = viewModel.cards
    val initialized = remember { mutableStateOf(false) }
    val gameResult = viewModel.gameResult.value

    // Sadece ilk kez yüklendiğinde setupGame çağır
    LaunchedEffect(Unit) {
        if (!initialized.value) {
            viewModel.setupGame(difficulty)
            initialized.value = true
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .padding(WindowInsets.statusBars.asPaddingValues())

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

            Text(
                text = stringResource(R.string.time_format, timeLeft),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
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
                    .fillMaxWidth(0.95f) // kenarlardan az boşluk
                    .aspectRatio(1f) // grid alanını kare tutar, ortalanmış görünür
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

    // Kazanma durumu
    if (gameResult == GameResult.Win) {
        AlertDialog(
            onDismissRequest = {},
            confirmButton = {
                TextButton(onClick = {
                    viewModel.saveScore(userName, score)
                    viewModel.resetResult()
                    navController.navigate("score_screen") // veya ana sayfaya
                }) {
                    Text(stringResource(R.string.save_score))
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    viewModel.resetResult()
                    navController.popBackStack()
                }) {
                    Text("Kapat")
                }
            },
            title = { Text(stringResource(R.string.congratulations)) },
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
                    viewModel.resetResult()
                    navController.navigate("game_setup_screen")
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
            title = { Text(stringResource(R.string.time_is_up)) },
            text = { Text(stringResource(R.string.time_expired)) }
        )
    }


}


@Composable
fun GameCardView(card: GameCard, onClick: () -> Unit) {
    val backgroundColor = when {
        card.isMatched -> Color(0xFF4CAF50)
        card.isRevealed -> Color.White
        else -> Color.LightGray
    }

    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .clickable(enabled = !card.isMatched && !card.isRevealed) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        if (card.isRevealed || card.isMatched) {
            Text(
                text = card.number.toString(),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun GameScreenPreview() {
    val sampleCards = listOf(
        GameCard(12), GameCard(45), GameCard(12), GameCard(45),
        GameCard(7), GameCard(7), GameCard(34), GameCard(34)
    ).shuffled()

//    NumMatchTheme {
//        GameScreen( cards = sampleCards)
//    }
}
