package com.example.nummatch.presentation.screen.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nummatch.domain.model.GameCard
import com.example.nummatch.domain.model.GameResult
import com.example.nummatch.domain.repository.GameRepository
import com.example.nummatch.data.local.ScoreEntity
import com.example.nummatch.util.Difficulty
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val repository: GameRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    private var timerJob: Job? = null

    val score: Int get() = _uiState.value.score

    fun initializeGame(difficulty: Difficulty, isTimerVisible: Boolean) {
        if (!_uiState.value.isGameInitialized) {
            setupGame(difficulty)
            updateUiState {
                it.copy(
                    isGameInitialized = true,
                    difficulty = difficulty
                )
            }
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
        val gameCards = pairedNumbers.map { GameCard(it) }

        updateUiState { currentState ->
            currentState.copy(
                cards = gameCards,
                timeLeft = 60,
                score = 0,
                gameResult = GameResult.None,
                revealedCards = emptyList(),
                isBusy = false,
                difficulty = difficulty
            )
        }
    }

    fun onCardClick(index: Int) {
        val currentState = _uiState.value
        if (currentState.isBusy) return
        if (currentState.revealedCards.contains(index)) return
        if (currentState.cards.getOrNull(index)?.isMatched == true) return

        val updatedCards = currentState.cards.toMutableList()
        updatedCards[index] = updatedCards[index].copy(isRevealed = true)
        val updatedRevealedCards = currentState.revealedCards + index

        updateUiState {
            it.copy(
                cards = updatedCards,
                revealedCards = updatedRevealedCards
            )
        }

        if (updatedRevealedCards.size == 2) {
            val i1 = updatedRevealedCards[0]
            val i2 = updatedRevealedCards[1]
            val card1 = updatedCards.getOrNull(i1)
            val card2 = updatedCards.getOrNull(i2)

            if (card1 != null && card2 != null) {
                updateUiState { it.copy(isBusy = true) }

                if (card1.number == card2.number) {
                    handleMatch(i1, i2, updatedCards)
                } else {
                    handleMismatch(i1, i2)
                }
            }
        }
    }

    private fun handleMatch(i1: Int, i2: Int, cards: MutableList<GameCard>) {
        val updated = cards.apply {
            this[i1] = this[i1].copy(isMatched = true)
            this[i2] = this[i2].copy(isMatched = true)
        }

        val newScore = _uiState.value.score + 10
        val isGameWon = updated.all { it.isMatched }

        updateUiState { currentState ->
            currentState.copy(
                cards = updated,
                score = newScore,
                revealedCards = emptyList(),
                isBusy = false,
                gameResult = if (isGameWon) GameResult.Win else GameResult.None
            )
        }

        if (isGameWon) {
            handleGameWin()
        }
    }

    private fun handleMismatch(i1: Int, i2: Int) {
        viewModelScope.launch {
            try {
                val delay = when(_uiState.value.difficulty) {
                    Difficulty.EASY -> 1000L
                    Difficulty.HARD -> 800L
                }
                delay(delay)
                updateUiState { currentState ->
                    val updatedCards = currentState.cards.toMutableList()

                    if (i1 < updatedCards.size && i2 < updatedCards.size) {
                        updatedCards[i1] = updatedCards[i1].copy(isRevealed = false)
                        updatedCards[i2] = updatedCards[i2].copy(isRevealed = false)
                    }

                    currentState.copy(
                        cards = updatedCards,
                        revealedCards = emptyList(),
                        isBusy = false
                    )
                }
            } catch (e: Exception) {
                updateUiState { it.copy(isBusy = false, revealedCards = emptyList()) }
            }
        }
    }

    private fun handleGameWin() {
        val currentState = _uiState.value
        if (currentState.isTimerVisible && timerJob != null) {
            updateUiState {
                it.copy(score = it.score + it.timeLeft)
            }
        }
        stopTimer()
    }

    fun saveScore(playerName: String, score: Int) {
        viewModelScope.launch {
            repository.insertScore(ScoreEntity(userName = playerName, score = score))
        }
    }

    fun startTimer() {
        stopTimer()
        updateUiState { it.copy(isTimerRunning = true) }

        timerJob = viewModelScope.launch {
            while (_uiState.value.timeLeft > 0) {
                delay(1000)
                updateUiState { currentState ->
                    currentState.copy(timeLeft = currentState.timeLeft - 1)
                }
            }

            if (_uiState.value.gameResult == GameResult.None) {
                updateUiState {
                    it.copy(
                        gameResult = GameResult.Lose,
                        isTimerRunning = false
                    )
                }
            }
        }
    }

    fun stopTimer() {
        timerJob?.cancel()
        timerJob = null
        updateUiState { it.copy(isTimerRunning = false) }
    }

    fun resetResult() {
        updateUiState { it.copy(gameResult = GameResult.None) }
    }

    fun setTimerVisibility(visible: Boolean) {
        updateUiState { it.copy(isTimerVisible = visible) }
    }

    fun restartGame(difficulty: Difficulty) {
        updateUiState {
            it.copy(
                gameResult = GameResult.None,
                score = 0,
                revealedCards = emptyList(),
                isBusy = false
            )
        }
        setupGame(difficulty)
        if (_uiState.value.isTimerVisible) {
            startTimer()
        }
    }

    private fun updateUiState(update: (GameUiState) -> GameUiState) {
        _uiState.value = update(_uiState.value)
    }

    override fun onCleared() {
        super.onCleared()
        stopTimer()
    }
}