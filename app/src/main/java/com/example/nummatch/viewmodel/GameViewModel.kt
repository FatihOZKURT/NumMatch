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
import dagger.hilt.android.lifecycle.HiltViewModel
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

    init {
        startTimer()
    }

    fun setupGame(difficulty: String) {
        val count = if (difficulty == "Easy") 8 else 12
        val numbers = (1..100).shuffled().take(count)
        val pairedNumbers = (numbers + numbers).shuffled()
        cards = pairedNumbers.map { GameCard(it) }
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
                // Eşleşme varsa: hemen matched yap
                val updated = cards.toMutableList()
                updated[i1] = updated[i1].copy(isMatched = true)
                updated[i2] = updated[i2].copy(isMatched = true)
                cards = updated
                score += 10
                revealedCards.clear()
                isBusy = false

                // 💡 Kazanma durumu kontrolü
                if (updated.all { it.isMatched }) {
                    _gameResult.value = GameResult.Win
                }

            } else {
                // Eşleşme yoksa: 1 sn bekleyip kartları kapat
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

    private fun startTimer() {
        viewModelScope.launch {
            while (timeLeft > 0) {
                delay(1000)
                timeLeft--
            }

            // 💡 Süre bittiğinde kaybettin
            if (_gameResult.value == GameResult.None) {
                _gameResult.value = GameResult.Lose
            }

        }
    }

    fun setWin() {
        _gameResult.value = GameResult.Win
    }

    fun setLose() {
        _gameResult.value = GameResult.Lose
    }

    fun resetResult() {
        _gameResult.value = GameResult.None
    }

}
