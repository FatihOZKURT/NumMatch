package com.example.nummatch.domain.model

sealed class GameResult {
    object Win : GameResult()
    object Lose : GameResult()
    object None : GameResult()
}