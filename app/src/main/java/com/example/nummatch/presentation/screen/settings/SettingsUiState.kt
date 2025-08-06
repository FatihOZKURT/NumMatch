package com.example.nummatch.presentation.screen.settings

data class SettingsUiState(
    val isDarkTheme: Boolean = false,
    val isTimerVisible: Boolean = true,
    val isLoading: Boolean = false,
    val showDeleteDialog: Boolean = false,
    val deleteScoresResult: DeleteScoresResult? = null
)

sealed class SettingsEvent {
    object ToggleDarkTheme : SettingsEvent()
    object ToggleTimer : SettingsEvent()
    object ShowDeleteDialog : SettingsEvent()
    object HideDeleteDialog : SettingsEvent()
    object DeleteScores : SettingsEvent()
    object ClearDeleteResult : SettingsEvent()
}

sealed class DeleteScoresResult {
    object Success : DeleteScoresResult()
    data class Error(val message: String) : DeleteScoresResult()
}