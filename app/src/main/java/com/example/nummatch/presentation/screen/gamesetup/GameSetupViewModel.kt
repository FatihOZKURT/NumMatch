package com.example.nummatch.presentation.screen.gamesetup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nummatch.repo.GameRepository
import com.example.nummatch.util.Difficulty
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameSetupViewModel @Inject constructor(
    private val gameRepository: GameRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(GameSetupUiState())
    val uiState: StateFlow<GameSetupUiState> = _uiState.asStateFlow()

    val username: String get() = _uiState.value.username

    init {
        loadUserPreferences()
    }

    fun onUsernameChange(newUsername: String) {
        if (newUsername.length <= 20) {
            updateUiState { currentState ->
                val isValid = validateUsername(newUsername.trim())
                currentState.copy(
                    username = newUsername,
                    isUsernameValid = isValid,
                    usernameError = if (isValid) null else currentState.computedUsernameError
                )
            }
        }
    }

    fun onDifficultyChange(difficulty: Difficulty) {
        updateUiState {
            it.copy(selectedDifficulty = difficulty)
        }
    }

    fun onUsernameSubmit() {
        val currentState = _uiState.value
        if (currentState.canStartGame) {
            addToRecentUsernames(currentState.trimmedUsername)
        }
    }

    fun selectRecentUsername(username: String) {
        onUsernameChange(username)
    }

    fun clearUsernameError() {
        updateUiState { it.copy(usernameError = null) }
    }

    fun setNavigating(isNavigating: Boolean) {
        updateUiState { it.copy(isNavigating = isNavigating) }
    }


    private fun validateUsername(username: String): Boolean {
        return when {
            username.isBlank() -> false
            username.length < 2 -> false
            username.length > 20 -> false
            username.any { !it.isLetterOrDigit() && it != ' ' && it != '_' } -> false
            else -> true
        }
    }

    private fun loadUserPreferences() {
        viewModelScope.launch {
            try {
                val lastDifficulty = gameRepository.getLastPlayedDifficulty()
                val recentUsernames = gameRepository.getRecentUsernames()

                updateUiState { currentState ->
                    currentState.copy(
                        selectedDifficulty = lastDifficulty ?: Difficulty.EASY,
                        lastPlayedDifficulty = lastDifficulty,
                        recentUsernames = recentUsernames
                    )
                }
            } catch (e: Exception) {

            }
        }
    }



    private fun addToRecentUsernames(username: String) {
        viewModelScope.launch {
            try {
                val currentRecent = _uiState.value.recentUsernames.toMutableList()
                currentRecent.remove(username)
                currentRecent.add(0, username)
                val updatedRecent = currentRecent.take(5)
                updateUiState {
                    it.copy(recentUsernames = updatedRecent)
                }

                gameRepository.saveRecentUsernames(updatedRecent)
            } catch (e: Exception) {
            }
        }
    }

    private fun updateUiState(update: (GameSetupUiState) -> GameSetupUiState) {
        _uiState.value = update(_uiState.value)
    }
}