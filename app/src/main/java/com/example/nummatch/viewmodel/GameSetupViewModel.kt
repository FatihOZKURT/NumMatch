package com.example.nummatch.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.nummatch.util.Difficulty
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GameSetupViewModel @Inject constructor(
) : ViewModel() {

    var username by mutableStateOf("")
        private set
    var selectedDifficulty by mutableStateOf(Difficulty.EASY)
        private set

    fun onDifficultyChange(difficulty: Difficulty) {
        selectedDifficulty = difficulty
    }

    fun onUsernameChange(newUsername: String) {
        username = newUsername
    }

}