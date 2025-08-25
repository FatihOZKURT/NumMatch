package com.example.nummatch.presentation.component.header

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nummatch.R
import com.example.nummatch.presentation.theme.Red

@Composable
fun GameHeader(
    score: Int,
    timeLeft: Int? = null,
    isTimerVisible: Boolean = true,
    progress: Float = 0f,
    showProgress: Boolean = true,
    lowTimeThreshold: Int = 10,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        // Score and Timer Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Score
            ScoreDisplay(score = score)

            // Timer
            if (isTimerVisible && timeLeft != null) {
                TimerDisplay(
                    timeLeft = timeLeft,
                    isLowTime = timeLeft <= lowTimeThreshold
                )
            }
        }

        // Progress Bar
        if (showProgress) {
            ProgressBar(
                progress = progress,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun ScoreDisplay(
    score: Int,
    modifier: Modifier = Modifier
) {
    Text(
        text = stringResource(R.string.score_format, score),
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        modifier = modifier
    )
}

@Composable
private fun TimerDisplay(
    timeLeft: Int,
    isLowTime: Boolean = false,
    modifier: Modifier = Modifier
) {
    Text(
        text = stringResource(R.string.time_format, timeLeft),
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = if (isLowTime) Red else Color.Unspecified,
        modifier = modifier
    )
}

@Composable
private fun ProgressBar(
    progress: Float,
    modifier: Modifier = Modifier
) {
    LinearProgressIndicator(
        progress = { progress },
        modifier = modifier.height(6.dp),
        color = MaterialTheme.colorScheme.primary,
        trackColor = ProgressIndicatorDefaults.linearTrackColor,
        strokeCap = ProgressIndicatorDefaults.CircularIndeterminateStrokeCap
    )
}