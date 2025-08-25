package com.example.nummatch.presentation.component.input

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.nummatch.R
import com.example.nummatch.presentation.component.button.AnimatedButton
import com.example.nummatch.presentation.component.button.AnimatedButtonDefaults
import com.example.nummatch.util.Difficulty

@Composable
fun DifficultySelector(
    modifier: Modifier = Modifier,
    difficulties: List<Difficulty>,
    selectedDifficulty: Difficulty,
    onDifficultyChange: (Difficulty) -> Unit,
    title: String = stringResource(R.string.select_difficulty),
    showTitle: Boolean = true,
    layout: DifficultySelectorLayout = DifficultySelectorLayout.Row
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Title
        if (showTitle) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Difficulty Options
        when (layout) {
            DifficultySelectorLayout.Row -> {
                DifficultyRow(
                    difficulties = difficulties,
                    selectedDifficulty = selectedDifficulty,
                    onDifficultyChange = onDifficultyChange
                )
            }
            DifficultySelectorLayout.Column -> {
                DifficultyColumn(
                    difficulties = difficulties,
                    selectedDifficulty = selectedDifficulty,
                    onDifficultyChange = onDifficultyChange
                )
            }
            DifficultySelectorLayout.LazyRow -> {
                DifficultyLazyRow(
                    difficulties = difficulties,
                    selectedDifficulty = selectedDifficulty,
                    onDifficultyChange = onDifficultyChange
                )
            }
        }
    }
}

@Composable
private fun DifficultyRow(
    difficulties: List<Difficulty>,
    selectedDifficulty: Difficulty,
    onDifficultyChange: (Difficulty) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()
    ) {
        difficulties.forEach { difficulty ->
            DifficultyButton(
                difficulty = difficulty,
                isSelected = selectedDifficulty == difficulty,
                onClick = { onDifficultyChange(difficulty) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun DifficultyColumn(
    difficulties: List<Difficulty>,
    selectedDifficulty: Difficulty,
    onDifficultyChange: (Difficulty) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth()
    ) {
        difficulties.forEach { difficulty ->
            DifficultyButton(
                difficulty = difficulty,
                isSelected = selectedDifficulty == difficulty,
                onClick = { onDifficultyChange(difficulty) },
                modifier = Modifier.fillMaxWidth(0.8f)
            )
        }
    }
}

@Composable
private fun DifficultyLazyRow(
    difficulties: List<Difficulty>,
    selectedDifficulty: Difficulty,
    onDifficultyChange: (Difficulty) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
    ) {
        items(difficulties) { difficulty ->
            DifficultyButton(
                difficulty = difficulty,
                isSelected = selectedDifficulty == difficulty,
                onClick = { onDifficultyChange(difficulty) },
                modifier = Modifier.height(56.dp)
            )
        }
    }
}

@Composable
private fun DifficultyButton(
    difficulty: Difficulty,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val secondaryText = when (difficulty) {
        Difficulty.EASY -> stringResource(R.string._8_pairs)
        Difficulty.HARD -> stringResource(R.string._12_pairs)
    }

    AnimatedButton(
        text = stringResource(id = difficulty.labelResId),
        secondaryText = secondaryText,
        onClick = onClick,
        isSelected = isSelected,
        colors = AnimatedButtonDefaults.difficultyColors(),
        modifier = modifier.height(56.dp)
    )
}

enum class DifficultySelectorLayout {
    Row,
    Column,
    LazyRow
}