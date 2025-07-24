package com.example.nummatch.model

data class GameCard(
    val number: Int,
    val isRevealed: Boolean = false,
    val isMatched: Boolean = false
)
