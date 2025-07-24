package com.example.nummatch.model

data class Score(
    val id: Int = 0,
    val userName: String,
    val score: Int,
    val date: Long = System.currentTimeMillis()
)
