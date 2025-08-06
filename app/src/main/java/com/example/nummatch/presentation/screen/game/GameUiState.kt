package com.example.nummatch.presentation.screen.game

import com.example.nummatch.model.GameCard
import com.example.nummatch.model.GameResult
import com.example.nummatch.util.Difficulty

data class GameUiState(
    val score: Int = 0,
    val timeLeft: Int = 60,
    val cards: List<GameCard> = emptyList(),
    val gameResult: GameResult = GameResult.None,
    val isTimerVisible: Boolean = true,
    val isGameInitialized: Boolean = false,
    val difficulty: Difficulty = Difficulty.EASY,
    val revealedCards: List<Int> = emptyList(),
    val isBusy: Boolean = false,
    val isTimerRunning: Boolean = false
) {

    val matchedPairs: Int
        get() = cards.count { it.isMatched } / 2

    val totalPairs: Int
        get() = cards.size / 2

    val progress: Float
        get() = if (totalPairs > 0) matchedPairs.toFloat() / totalPairs else 0f
}