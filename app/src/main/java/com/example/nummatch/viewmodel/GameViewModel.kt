package com.example.nummatch.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nummatch.model.GameCard
import com.example.nummatch.model.GameResult
import com.example.nummatch.repo.ScoreRepository
import com.example.nummatch.room.ScoreEntity
import com.example.nummatch.util.Difficulty
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val repository: ScoreRepository
) : ViewModel() {

    var cards by mutableStateOf(listOf<GameCard>())
        private set
    var score by mutableIntStateOf(0)
        private set
    var timeLeft by mutableIntStateOf(60)
        private set
    private val _gameResult = mutableStateOf<GameResult>(GameResult.None)
    val gameResult: State<GameResult> = _gameResult
    private var revealedCards = mutableListOf<Int>()
    private var isBusy = false
    private var timerJob: Job? = null
    private var isTimerVisible: Boolean = true
    private var isGameInitialized = false

    fun initializeGame(difficulty: Difficulty, isTimerVisible: Boolean) {
        if (!isGameInitialized) {
            setupGame(difficulty)
            isGameInitialized = true
        }
        setTimerVisibility(isTimerVisible)
        if (isTimerVisible) {
            startTimer()
        } else {
            stopTimer()
        }
    }

    fun setupGame(difficulty: Difficulty) {
        val count = when (difficulty) {
            Difficulty.EASY -> 8
            Difficulty.HARD -> 12
        }
        val numbers = (1..100).shuffled().take(count)
        val pairedNumbers = (numbers + numbers).shuffled()
        cards = pairedNumbers.map { GameCard(it) }
        timeLeft = 60
    }

    fun onCardClick(index: Int) {
        if (isBusy) return
        if (revealedCards.contains(index) || cards[index].isMatched) return

        val updatedCards = cards.toMutableList()
        updatedCards[index] = updatedCards[index].copy(isRevealed = true)
        cards = updatedCards
        revealedCards.add(index)

        if (revealedCards.size == 2) {
            val i1 = revealedCards[0]
            val i2 = revealedCards[1]
            val number1 = cards[i1].number
            val number2 = cards[i2].number

            isBusy = true

            if (number1 == number2) {
                val updated = cards.toMutableList()
                updated[i1] = updated[i1].copy(isMatched = true)
                updated[i2] = updated[i2].copy(isMatched = true)
                cards = updated
                score += 10
                revealedCards.clear()
                isBusy = false
                if (updated.all { it.isMatched }) {
                    if (isTimerVisible && timerJob != null) {
                        score += timeLeft
                    }
                    _gameResult.value = GameResult.Win
                    stopTimer()
                }

            } else {
                viewModelScope.launch {
                    delay(1000)
                    val updated = cards.toMutableList()
                    updated[i1] = updated[i1].copy(isRevealed = false)
                    updated[i2] = updated[i2].copy(isRevealed = false)
                    cards = updated
                    revealedCards.clear()
                    isBusy = false
                }
            }
        }
    }

    fun saveScore(playerName: String, score: Int) {
        viewModelScope.launch {
            repository.insertScore(ScoreEntity(userName = playerName, score = score))
        }
    }

    fun startTimer() {
        stopTimer()
        timerJob = viewModelScope.launch {
            while (timeLeft > 0) {
                delay(1000)
                timeLeft--
            }

            if (_gameResult.value == GameResult.None) {
                _gameResult.value = GameResult.Lose
            }
        }
    }

    fun stopTimer() {
        timerJob?.cancel()
        timerJob = null
    }

    fun resetResult() {
        _gameResult.value = GameResult.None
    }

    fun setTimerVisibility(visible: Boolean) {
        isTimerVisible = visible
    }

    fun restartGame(difficulty: Difficulty) {
        _gameResult.value = GameResult.None
        score = 0
        revealedCards.clear()
        isBusy = false
        setupGame(difficulty)
        if (isTimerVisible) {
            startTimer()
        }
    }

    override fun onCleared() {
        super.onCleared()
        stopTimer()
    }
}