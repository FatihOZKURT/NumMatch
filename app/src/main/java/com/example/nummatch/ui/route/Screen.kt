package com.example.nummatch.ui.route

sealed class Screen(val route: String) {

    data object Main : Screen("main_screen")
    data object GameSetup : Screen("game_setup_screen")
    data object Game : Screen("game_screen/{username}/{difficulty}") {
        fun createRoute(username: String, difficulty: String): String {
            return "game_screen/$username/$difficulty"
        }
    }

    data object Score : Screen("score_screen")
    data object Settings : Screen("settings_screen")

}