package com.example.nummatch.presentation.component.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.example.nummatch.R
import com.example.nummatch.domain.model.GameResult
import com.example.nummatch.presentation.theme.EnableButtonBackground
import com.example.nummatch.util.Difficulty

@Composable
fun GameResultDialog(
    gameResult: GameResult,
    score: Int,
    userName: String,
    difficulty: Difficulty,
    onSaveScore: (String, Int) -> Unit,
    onResetResult: () -> Unit,
    onRestartGame: (Difficulty) -> Unit,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    when (gameResult) {
        GameResult.Win -> {
            WinDialog(
                score = score,
                userName = userName,
                onSaveScore = onSaveScore,
                onResetResult = onResetResult,
                onNavigateToScores = {
                    navController.navigate("score_screen") {
                        popUpTo("main_screen") { inclusive = false }
                    }
                },
                onNavigateBack = {
                    navController.navigate("main_screen") {
                        popUpTo("main_screen") { inclusive = true }
                    }
                },
                modifier = modifier
            )
        }

        GameResult.Lose -> {
            LoseDialog(
                difficulty = difficulty,
                onRestartGame = onRestartGame,
                onResetResult = onResetResult,
                onNavigateBack = {
                    navController.navigate("main_screen") {
                        popUpTo("main_screen") { inclusive = true }
                    }
                },
                modifier = modifier
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
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
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
        },
        modifier = modifier
    )
}

@Composable
private fun LoseDialog(
    difficulty: Difficulty,
    onRestartGame: (Difficulty) -> Unit,
    onResetResult: () -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
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
        },
        modifier = modifier
    )
}