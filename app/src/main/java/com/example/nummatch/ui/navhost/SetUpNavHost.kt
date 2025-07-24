package com.example.nummatch.ui.navhost

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.nummatch.model.Score
import com.example.nummatch.ui.GameScreen
import com.example.nummatch.ui.GameSetupScreen
import com.example.nummatch.ui.MainScreen
import com.example.nummatch.ui.ScoreScreen
import com.example.nummatch.ui.SettingsScreen
import com.example.nummatch.ui.route.Screen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Main.route
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
                navArgument("difficulty") { defaultValue = "Easy" }
            )
        ) { backStackEntry ->
            val username = backStackEntry.arguments?.getString("username") ?: ""
            val difficulty = backStackEntry.arguments?.getString("difficulty") ?: "Easy"
            GameScreen(userName = username, difficulty = difficulty, navController = navController)
        }



        composable(Screen.Score.route) {
            ScoreScreen()
        }

        composable(Screen.Settings.route) {
            SettingsScreen()
        }
    }
}
