package com.example.nummatch.ui

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.nummatch.R
import com.example.nummatch.ui.route.Screen
import com.example.nummatch.ui.theme.NumMatchTheme
import com.example.nummatch.viewmodel.GameSetupViewModel

@Composable
fun GameSetupScreen(
    modifier: Modifier = Modifier,
    viewModel: GameSetupViewModel = hiltViewModel(),
    onStartClick: (username: String, difficulty: String) -> Unit = { _, _ -> },
    navController: NavHostController
) {
    val username = viewModel.username
    val selectedDifficulty = viewModel.selectedDifficulty

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = username,
            shape = shapes.large,
            onValueChange = { viewModel.onUsernameChange(it) },
            label = { Text(stringResource(R.string.enter_username)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            val difficulties = listOf(stringResource(R.string.easy), stringResource(R.string.hard))
            difficulties.forEach { difficulty ->
                Button(
                    onClick = { viewModel.onDifficultyChange(difficulty) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedDifficulty == difficulty) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                        contentColor = if (selectedDifficulty == difficulty) Color.White else MaterialTheme.colorScheme.onSurface
                    )
                ) {
                    Text(difficulty)
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                if (username.isNotBlank() && selectedDifficulty.isNotBlank()) {
                    val encodedUsername = Uri.encode(username)
                    navController.navigate(
                        Screen.Game.createRoute(
                            encodedUsername,
                            selectedDifficulty
                        )
                    )
                }
            },
            enabled = username.isNotBlank()
        ) {
            Text(stringResource(R.string.start))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun GameSetupScreenPreview() {
    NumMatchTheme {
        GameSetupScreen(navController = NavHostController(LocalContext.current))
    }
}