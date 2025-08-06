package com.example.nummatch.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.nummatch.presentation.screen.game.GameScreen
import com.example.nummatch.presentation.screen.gamesetup.GameSetupScreen
import com.example.nummatch.presentation.screen.main.MainScreen
import com.example.nummatch.presentation.screen.score.ScoreScreen
import com.example.nummatch.presentation.screen.settings.SettingsScreen
import com.example.nummatch.util.Difficulty

@Composable
fun NumMatchNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Main.route,
        modifier = modifier
    ) {
        composable(Screen.Main.route) {
            MainScreen(navController)
        }

        composable(Screen.GameSetup.route) {
            GameSetupScreen(navController = navController)
        }

        composable(
            route = "game_screen/{username}/{difficulty}",
            arguments = listOf(
                navArgument("username") { defaultValue = "" },
                navArgument("difficulty") { defaultValue = Difficulty.EASY.name }
            )
        ) { backStackEntry ->
            val username = backStackEntry.arguments?.getString("username") ?: ""
            val difficulty = backStackEntry.arguments?.getString("difficulty") ?: "Easy"
            GameScreen(
                userName = username,
                difficulty = Difficulty.valueOf(difficulty),
                navController = navController
            )
        }

        composable(Screen.Score.route) {
            ScoreScreen(onBackClick = {
                navController.navigate(Screen.Main.route) {
                    popUpTo(Screen.Main.route) { inclusive = false }
                    launchSingleTop = true
                }
            })
        }

        composable(Screen.Settings.route) {
            SettingsScreen(onBackClick = {
                navController.navigate(Screen.Main.route) {
                    popUpTo(Screen.Main.route) { inclusive = false }
                    launchSingleTop = true
                }
            })
        }
    }
}