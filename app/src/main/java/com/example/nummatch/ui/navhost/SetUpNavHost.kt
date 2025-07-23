package com.example.nummatch.ui.navhost

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.nummatch.data.Score
import com.example.nummatch.ui.GameScreen
import com.example.nummatch.ui.GameSetupScreen
import com.example.nummatch.ui.MainScreen
import com.example.nummatch.ui.ScoreScreen
import com.example.nummatch.ui.SettingsScreen
import com.example.nummatch.ui.route.Screen

@Composable
fun SetupNavHost(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Main.route
    ) {
        composable(Screen.Main.route) {
            MainScreen(navController)
        }

        composable(Screen.GameSetup.route) {
            GameSetupScreen()
        }

        composable(Screen.Game.route) {
            GameScreen()
        }

        composable(Screen.Score.route) {
            ScoreScreen(emptyList<Score>())
        }

        composable(Screen.Settings.route) {
            SettingsScreen()
        }
    }
}
