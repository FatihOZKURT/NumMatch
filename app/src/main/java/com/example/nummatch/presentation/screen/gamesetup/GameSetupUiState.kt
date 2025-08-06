package com.example.nummatch.presentation.screen.gamesetup

import com.example.nummatch.util.Difficulty

data class GameSetupUiState(
    val username: String = "",
    val selectedDifficulty: Difficulty = Difficulty.EASY,
    val availableDifficulties: List<Difficulty> = Difficulty.entries.toList(),
    val isUsernameValid: Boolean = false,
    val usernameError: String? = null,
    val isNavigating: Boolean = false,
    val lastPlayedDifficulty: Difficulty? = null,
    val recentUsernames: List<String> = emptyList()
) {
    val canStartGame: Boolean
        get() = isUsernameValid && !isNavigating

    val trimmedUsername: String
        get() = username.trim()

    val isUsernameTooShort: Boolean
        get() = trimmedUsername.isNotEmpty() && trimmedUsername.length < 2

    val isUsernameTooLong: Boolean
        get() = trimmedUsername.length > 20

    val hasInvalidCharacters: Boolean
        get() = trimmedUsername.any { !it.isLetterOrDigit() && it != ' ' && it != '_' }

    val computedUsernameError: String?
        get() = when {
            isUsernameTooShort -> "Username must be at least 2 characters"
            isUsernameTooLong -> "Username must be less than 20 characters"
            hasInvalidCharacters -> "Username can only contain letters, numbers, spaces and underscores"
            else -> null
        }
}