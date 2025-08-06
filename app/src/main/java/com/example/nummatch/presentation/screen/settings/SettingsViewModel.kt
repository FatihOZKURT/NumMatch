package com.example.nummatch.presentation.screen.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nummatch.repo.GameRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val gameRepository: GameRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = combine(
        gameRepository.isDarkThemeFlow(),
        gameRepository.isTimerVisibleFlow(),
        _uiState
    ) { isDarkTheme, isTimerVisible, currentState ->
        currentState.copy(
            isDarkTheme = isDarkTheme,
            isTimerVisible = isTimerVisible
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SettingsUiState()
    )

    fun onEvent(event: SettingsEvent) {
        when (event) {
            is SettingsEvent.ToggleDarkTheme -> toggleDarkTheme()
            is SettingsEvent.ToggleTimer -> toggleTimer()
            is SettingsEvent.ShowDeleteDialog -> showDeleteDialog()
            is SettingsEvent.HideDeleteDialog -> hideDeleteDialog()
            is SettingsEvent.DeleteScores -> deleteScores()
            is SettingsEvent.ClearDeleteResult -> clearDeleteResult()
        }
    }

    private fun toggleDarkTheme() {
        viewModelScope.launch {
            gameRepository.setDarkTheme(!uiState.value.isDarkTheme)
        }
    }

    private fun toggleTimer() {
        val currentValue = _uiState.value.isTimerVisible
        val newValue = !currentValue

        _uiState.value = _uiState.value.copy(isTimerVisible = newValue)

        viewModelScope.launch {
            try {
                gameRepository.setTimerVisible(newValue)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isTimerVisible = currentValue)
            }
        }
    }

    private fun showDeleteDialog() {
        _uiState.value = _uiState.value.copy(showDeleteDialog = true)
    }

    private fun hideDeleteDialog() {
        _uiState.value = _uiState.value.copy(showDeleteDialog = false)
    }

    private fun deleteScores() {
        _uiState.value = _uiState.value.copy(
            isLoading = true,
            showDeleteDialog = false
        )

        viewModelScope.launch {
            try {
                gameRepository.deleteAllScores()
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    deleteScoresResult = DeleteScoresResult.Success
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    deleteScoresResult = DeleteScoresResult.Error(
                        e.message ?: "Unknown error occurred"
                    )
                )
            }
        }
    }

    private fun clearDeleteResult() {
        _uiState.value = _uiState.value.copy(deleteScoresResult = null)
    }
}