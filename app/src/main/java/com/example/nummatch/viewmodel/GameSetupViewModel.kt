package com.example.nummatch.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GameSetupViewModel @Inject constructor(
) : ViewModel() {

    var username by mutableStateOf("")
        private set

    var selectedDifficulty by mutableStateOf("Easy")
        private set

    fun onUsernameChange(newUsername: String) {
        username = newUsername
    }

    fun onDifficultyChange(difficulty: String) {
        selectedDifficulty = difficulty
    }



}