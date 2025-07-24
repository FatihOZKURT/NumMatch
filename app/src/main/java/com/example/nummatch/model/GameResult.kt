package com.example.nummatch.model

sealed class GameResult {
    object Win : GameResult()
    object Lose : GameResult()
    object None : GameResult()
}