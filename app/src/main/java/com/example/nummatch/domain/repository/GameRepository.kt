package com.example.nummatch.domain.repository

import com.example.nummatch.data.local.ScoreEntity
import com.example.nummatch.domain.model.Score
import com.example.nummatch.util.Difficulty
import kotlinx.coroutines.flow.Flow

interface GameRepository {

    suspend fun insertScore(score: ScoreEntity)

    suspend fun deleteAllScores()

    fun getAllScores(): Flow<List<Score>>

    suspend fun getLastPlayedDifficulty(): Difficulty?

    suspend fun getRecentUsernames(): List<String>

    suspend fun saveRecentUsernames(usernames: List<String>)

    // Theme settings
    fun isDarkThemeFlow(): Flow<Boolean>
    suspend fun setDarkTheme(isDark: Boolean)

    // Timer settings
    fun isTimerVisibleFlow(): Flow<Boolean>
    suspend fun setTimerVisible(isVisible: Boolean)
}