package com.example.nummatch.domain.model

data class GameCard(
    val number: Int,
    val isRevealed: Boolean = false,
    val isMatched: Boolean = false
)