package com.example.nummatch.presentation.screen.gamesetup

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.nummatch.R
import com.example.nummatch.presentation.component.button.AnimatedButton
import com.example.nummatch.presentation.component.input.DifficultySelector
import com.example.nummatch.presentation.component.input.UsernameInput
import com.example.nummatch.presentation.navigation.Screen
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
        UsernameInput(
            username = uiState.username,
            onUsernameChange = viewModel::onUsernameChange,
            usernameError = uiState.usernameError ?: uiState.computedUsernameError,
            onClearError = viewModel::clearUsernameError,
            recentUsernames = uiState.recentUsernames,
            onRecentUsernameClick = viewModel::selectRecentUsername
        )

        Spacer(modifier = Modifier.height(32.dp))

        DifficultySelector(
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
private fun StartGameButton(
    canStart: Boolean,
    isNavigating: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedButton(
        text = stringResource(R.string.start),
        icon = Icons.Default.PlayArrow,
        onClick = onClick,
        enabled = canStart,
        isLoading = isNavigating,
        loadingText = stringResource(R.string.starting),
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .height(56.dp),
        shape = shapes.large
    )
}

