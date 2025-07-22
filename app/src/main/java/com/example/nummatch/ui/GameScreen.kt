package com.example.nummatch.ui

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nummatch.R
import com.example.nummatch.data.GameCard
import com.example.nummatch.ui.theme.NumMatchTheme

@Composable
private fun GameScreen(
    modifier: Modifier = Modifier,
    score: Int = 0,
    timeLeft: Int = 60,
    cards: List<GameCard> = emptyList(),
    onCardClick: (Int) -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Üst Bilgi: Skor ve Süre
        Row(
            modifier = Modifier.fillMaxWidth(),
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

        // Kartlar Grid
        val columnCount = if (cards.size == 16) 4 else 6 // 4x4 veya 6x4

        LazyVerticalGrid(
            columns = GridCells.Fixed(columnCount),
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            content = {
                items(cards.size) { index ->
                    GameCardView(
                        card = cards[index],
                        onClick = { onCardClick(index) }
                    )
                }
            }
        )
    }
}

@Composable
fun GameCardView(card: GameCard, onClick: () -> Unit) {
    val backgroundColor = when {
        card.isMatched -> Color(0xFF4CAF50) // Yeşil
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

    NumMatchTheme {
        GameScreen(cards = sampleCards)
    }
}
