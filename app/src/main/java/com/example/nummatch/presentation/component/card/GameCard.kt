package com.example.nummatch.presentation.component.card

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.example.nummatch.R
import com.example.nummatch.domain.model.GameCard
import com.example.nummatch.presentation.theme.Green
import com.example.nummatch.presentation.theme.TextLight


@Composable
fun GameCard(
    card: GameCard,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    animationDuration: Int = 400,
    cardBackPainter: Painter = painterResource(id = R.drawable.card_design),
    cardFrontPainter: Painter = painterResource(id = R.drawable.card_design_empty),
    matchedColor: Color = Green,
    unMatchedColor: Color = TextLight,
    fontSize: TextUnit = 24.sp,
    fontWeight: FontWeight = FontWeight.Bold
) {
    val rotation by animateFloatAsState(
        targetValue = if (card.isRevealed || card.isMatched) 180f else 0f,
        animationSpec = tween(durationMillis = animationDuration),
        label = stringResource(R.string.card_flip)
    )

    val textColor by animateColorAsState(
        targetValue = if (card.isMatched) matchedColor else unMatchedColor,
        animationSpec = tween(durationMillis = animationDuration / 2),
        label = "card_text_color"
    )

    val isFront = rotation >= 90f

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .graphicsLayer {
                rotationY = rotation
                cameraDistance = 8 * density
            }
            .clickable(enabled = !card.isMatched && !card.isRevealed) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        if (isFront) {
            // Card Front (revealed)
            CardFront(
                card = card,
                painter = cardFrontPainter,
                textColor = textColor,
                fontSize = fontSize,
                fontWeight = fontWeight
            )
        } else {
            // Card Back (hidden)
            CardBack(painter = cardBackPainter)
        }
    }
}

@Composable
private fun CardFront(
    card: GameCard,
    painter: Painter,
    textColor: Color,
    fontSize: TextUnit,
    fontWeight: FontWeight
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer {
                rotationY = 180f
            },
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painter,
            contentDescription = stringResource(R.string.card_front),
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )
        Text(
            text = card.number.toString(),
            fontSize = fontSize,
            fontWeight = fontWeight,
            color = textColor
        )
    }
}

@Composable
private fun CardBack(
    painter: Painter
) {
    Image(
        painter = painter,
        contentDescription = stringResource(R.string.card_back),
        contentScale = ContentScale.FillBounds,
        modifier = Modifier.fillMaxSize()
    )
}