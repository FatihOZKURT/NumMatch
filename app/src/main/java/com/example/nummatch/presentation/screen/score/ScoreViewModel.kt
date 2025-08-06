package com.example.nummatch.presentation.screen.score

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nummatch.repo.GameRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScoreViewModel @Inject constructor(
    private val gameRepository: GameRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ScoreUiState())
    val uiState: StateFlow<ScoreUiState> = _uiState.asStateFlow()

    init {
        loadScores()
    }

    private fun loadScores() {
        viewModelScope.launch {
            updateUiState { it.copy(isLoading = true, error = null) }

            try {
                gameRepository.getAllScores()
                    .catch { exception ->
                        updateUiState {
                            it.copy(
                                isLoading = false,
                                error = exception.message ?: "Unknown error occurred"
                            )
                        }
                    }
                    .collect { scores ->
                        updateUiState {
                            it.copy(
                                scores = scores,
                                isLoading = false,
                                error = null
                            )
                        }
                    }
            } catch (e: Exception) {
                updateUiState {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to load scores"
                    )
                }
            }
        }
    }


    fun refreshScores() {
        loadScores()
    }

    fun clearError() {
        updateUiState { it.copy(error = null) }
    }

    private fun updateUiState(update: (ScoreUiState) -> ScoreUiState) {
        _uiState.value = update(_uiState.value)
    }
}