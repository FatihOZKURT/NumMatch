package com.example.nummatch.presentation.component.card

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.nummatch.R
import com.example.nummatch.domain.model.Score
import com.example.nummatch.presentation.theme.Bronze
import com.example.nummatch.presentation.theme.Gold
import com.example.nummatch.presentation.theme.Silver

@Composable
fun ScoreCard(
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