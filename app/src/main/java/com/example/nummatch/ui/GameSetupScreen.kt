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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.nummatch.R
import com.example.nummatch.ui.route.Screen
import com.example.nummatch.ui.theme.DisableButtonBackground
import com.example.nummatch.ui.theme.EnableButtonBackground
import com.example.nummatch.ui.theme.SecondaryColor
import com.example.nummatch.util.Difficulty
import com.example.nummatch.viewmodel.GameSetupViewModel

@Composable
fun GameSetupScreen(
    modifier: Modifier = Modifier,
    viewModel: GameSetupViewModel = hiltViewModel(),
    navController: NavHostController
) {
    val username = viewModel.username
    val selectedDifficulty = viewModel.selectedDifficulty
    val difficulties = Difficulty.entries.toTypedArray()

    Scaffold(modifier = modifier) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(24.dp)
                .padding(innerPadding),
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

                difficulties.forEach { difficulty ->
                    Button(
                        onClick = { viewModel.onDifficultyChange(difficulty) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedDifficulty == difficulty) EnableButtonBackground else DisableButtonBackground,
                            contentColor = if (selectedDifficulty == difficulty) Color.White else MaterialTheme.colorScheme.onSurface
                        )
                    ) {
                        Text(stringResource(id = difficulty.labelResId))
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    if (username.isNotBlank() && selectedDifficulty.name.isNotBlank()) {
                        val encodedUsername = Uri.encode(username)
                        navController.navigate(
                            Screen.Game.toGame(
                                encodedUsername,
                                selectedDifficulty.name
                            )
                        )
                    }
                },
                enabled = username.isNotBlank(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = EnableButtonBackground,
                    disabledContainerColor = DisableButtonBackground,
                    disabledContentColor = SecondaryColor

                )
            ) {
                Text(stringResource(R.string.start))
            }


        }
    }
}
