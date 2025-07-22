package com.example.nummatch.data

data class GameCard(
    val number: Int,
    val isRevealed: Boolean = false,
    val isMatched: Boolean = false
)
