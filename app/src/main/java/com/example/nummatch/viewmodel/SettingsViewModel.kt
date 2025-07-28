package com.example.nummatch.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nummatch.datasource.DataStoreManager
import com.example.nummatch.repo.ScoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val dataStoreManager: DataStoreManager,
    private val scoreRepository: ScoreRepository
) : ViewModel() {

    val isDarkTheme = dataStoreManager.isDarkTheme.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), false
    )

    val isTimerVisible = dataStoreManager.isTimerVisible.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000), true
    )

    fun toggleDarkTheme(enabled: Boolean) {
        viewModelScope.launch {
            dataStoreManager.setDarkTheme(enabled)
        }
    }

    fun toggleTimer(visible: Boolean) {
        viewModelScope.launch {
            dataStoreManager.setShowTimer(visible)
        }
    }

    fun deleteScores() {
        viewModelScope.launch {
            scoreRepository.deleteAllScores()
        }
    }

}