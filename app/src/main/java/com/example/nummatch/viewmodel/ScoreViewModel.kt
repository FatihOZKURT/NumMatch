package com.example.nummatch.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nummatch.model.Score
import com.example.nummatch.repo.ScoreRepository
import com.example.nummatch.room.ScoreEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScoreViewModel @Inject constructor(
    private val scoreRepository: ScoreRepository
) : ViewModel() {

    private val _scoreList = MutableStateFlow<List<Score>>(emptyList())
    val scoreList: StateFlow<List<Score>> = _scoreList.asStateFlow()

    init {
        getScores()
    }

    private fun getScores() {
        viewModelScope.launch {
            scoreRepository.getAllScores()
                .collect { scores ->
                    _scoreList.value = scores.sortedByDescending { it.score }
                }
        }
    }



}