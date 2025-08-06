package com.example.nummatch.presentation.screen.gamesetup

import android.net.Uri
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.nummatch.R
import com.example.nummatch.presentation.navigation.Screen
import com.example.nummatch.presentation.theme.DisableButtonBackground
import com.example.nummatch.presentation.theme.EnableButtonBackground
import com.example.nummatch.presentation.theme.SecondaryColor
import com.example.nummatch.util.Difficulty
import kotlinx.coroutines.delay

@Composable
fun GameSetupScreen(
    modifier: Modifier = Modifier,
    viewModel: GameSetupViewModel = hiltViewModel(),
    navController: NavHostController
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.isNavigating) {
        if (uiState.isNavigating) {
            delay(100)
            viewModel.setNavigating(false)
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Welcome Text
        WelcomeSection()

        Spacer(modifier = Modifier.height(32.dp))

        // Username Input Section
        UsernameInputSection(
            username = uiState.username,
            usernameError = uiState.usernameError ?: uiState.computedUsernameError,
            recentUsernames = uiState.recentUsernames,
            onUsernameChange = viewModel::onUsernameChange,
            onRecentUsernameClick = viewModel::selectRecentUsername,
            onClearError = viewModel::clearUsernameError
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Difficulty Selection
        DifficultySelectionSection(
            difficulties = uiState.availableDifficulties,
            selectedDifficulty = uiState.selectedDifficulty,
            onDifficultyChange = viewModel::onDifficultyChange
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Start Game Button
        StartGameButton(
            canStart = uiState.canStartGame,
            isNavigating = uiState.isNavigating,
            onClick = {
                viewModel.onUsernameSubmit()
                viewModel.setNavigating(true)

                val encodedUsername = Uri.encode(uiState.trimmedUsername)
                navController.navigate(
                    Screen.Game.toGame(
                        encodedUsername,
                        uiState.selectedDifficulty.name
                    )
                )
            }
        )
    }
}

@Composable
private fun WelcomeSection(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.match_and_win),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun UsernameInputSection(
    username: String,
    usernameError: String?,
    recentUsernames: List<String>,
    onUsernameChange: (String) -> Unit,
    onRecentUsernameClick: (String) -> Unit,
    onClearError: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        OutlinedTextField(
            value = username,
            onValueChange = {
                onUsernameChange(it)
                if (usernameError != null) onClearError()
            },
            label = { Text(stringResource(R.string.enter_username)) },
            singleLine = true,
            isError = usernameError != null,
            supportingText = if (usernameError != null) {
                {
                    Text(
                        text = usernameError,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            } else null,
            trailingIcon = if (username.isNotEmpty()) {
                {
                    IconButton(onClick = { onUsernameChange("") }) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = stringResource(R.string.clear_username)
                        )
                    }
                }
            } else null,
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large
        )

        if (recentUsernames.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.recent_players),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(recentUsernames) { recentUsername ->
                    SuggestionChip(
                        onClick = { onRecentUsernameClick(recentUsername) },
                        label = { Text(recentUsername) }
                    )
                }
            }
        }
    }
}

@Composable
private fun DifficultySelectionSection(
    difficulties: List<Difficulty>,
    selectedDifficulty: Difficulty,
    onDifficultyChange: (Difficulty) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.select_difficulty),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
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
}

@Composable
private fun DifficultyButton(
    difficulty: Difficulty,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val animatedColor by animateColorAsState(
        targetValue = if (isSelected) EnableButtonBackground else DisableButtonBackground,
        label = stringResource(R.string.button_color)
    )

    val animatedScale by animateFloatAsState(
        targetValue = if (isSelected) 1.05f else 1f,
        label = stringResource(R.string.button_scale)
    )

    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = animatedColor,
            contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
        ),
        modifier = modifier
            .scale(animatedScale)
            .height(56.dp),
        shape = shapes.medium
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = difficulty.labelResId),
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
            )

            Text(
                text = when (difficulty) {
                    Difficulty.EASY -> stringResource(R.string._8_pairs)
                    Difficulty.HARD -> stringResource(R.string._12_pairs)
                },
                style = MaterialTheme.typography.bodySmall,
                color = if (isSelected) MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}

@Composable
private fun StartGameButton(
    canStart: Boolean,
    isNavigating: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        enabled = canStart && !isNavigating,
        colors = ButtonDefaults.buttonColors(
            containerColor = EnableButtonBackground,
            disabledContainerColor = DisableButtonBackground,
            disabledContentColor = SecondaryColor
        ),
        modifier = modifier
            .fillMaxWidth(0.8f)
            .height(56.dp),
        shape = shapes.large
    ) {
        if (isNavigating) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                color = MaterialTheme.colorScheme.onPrimary,
                strokeWidth = 2.dp
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(stringResource(R.string.starting))
        } else {
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.start),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}