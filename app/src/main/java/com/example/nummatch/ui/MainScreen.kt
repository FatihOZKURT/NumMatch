package com.example.nummatch.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.nummatch.R
import com.example.nummatch.ui.route.Screen

@Composable
fun MainScreen(navController: NavHostController, modifier: Modifier = Modifier) {
    Scaffold(modifier = modifier) { innerPadding ->
        Box(
            modifier = modifier.fillMaxSize().padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                val buttonModifier = Modifier
                    .width(150.dp)
                    .height(40.dp)

                Button(
                    onClick = {
                        navController.navigate(Screen.GameSetup.route)
                    },
                    modifier = buttonModifier
                ) {
                    Text(text = stringResource(R.string.create_game))
                }

                Button(
                    onClick = {
                        navController.navigate(Screen.Score.route)
                    },
                    modifier = buttonModifier
                ) {
                    Text(text = stringResource(R.string.scores))
                }

                Button(
                    onClick = {
                        navController.navigate(Screen.Settings.route)
                    },
                    modifier = buttonModifier
                ) {
                    Text(text = stringResource(R.string.settings))
                }
            }
        }
    }
}

