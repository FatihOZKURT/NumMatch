package com.example.nummatch.repo

import com.example.nummatch.datasource.GameDataSource
import com.example.nummatch.model.Score
import com.example.nummatch.room.ScoreEntity
import com.example.nummatch.room.toScore
import com.example.nummatch.util.Difficulty
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GameRepository @Inject constructor(
    private val gameDataSource: GameDataSource
) {
    suspend fun insertScore(score: ScoreEntity) = gameDataSource.insertScore(score)

    suspend fun deleteAllScores() = gameDataSource.deleteAllScores()

    fun getAllScores(): Flow<List<Score>> {
        return gameDataSource.getAllScores().map { list ->
            list.map { it.toScore() }
        }
    }

    suspend fun getLastPlayedDifficulty(): Difficulty? = gameDataSource.getLastPlayedDifficulty()

    suspend fun getRecentUsernames(): List<String> = gameDataSource.getRecentUsernames()

    suspend fun saveRecentUsernames(usernames: List<String>) = gameDataSource.saveRecentUsernames(usernames)

    // Theme settings
    fun isDarkThemeFlow(): Flow<Boolean> = gameDataSource.isDarkThemeFlow()
    suspend fun setDarkTheme(isDark: Boolean) = gameDataSource.setDarkTheme(isDark)

    // Timer settings
    fun isTimerVisibleFlow(): Flow<Boolean> = gameDataSource.isTimerVisibleFlow()
    suspend fun setTimerVisible(isVisible: Boolean) = gameDataSource.setTimerVisible(isVisible)

}