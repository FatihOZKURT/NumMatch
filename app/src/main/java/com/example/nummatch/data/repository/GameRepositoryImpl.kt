package com.example.nummatch.data.repository

import com.example.nummatch.data.source.GameDataSource
import com.example.nummatch.domain.model.Score
import com.example.nummatch.domain.repository.GameRepository
import com.example.nummatch.data.local.ScoreEntity
import com.example.nummatch.data.local.toScore
import com.example.nummatch.util.Difficulty
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GameRepositoryImpl @Inject constructor(
    private val gameDataSource: GameDataSource
) : GameRepository {

    override suspend fun insertScore(score: ScoreEntity) = gameDataSource.insertScore(score)

    override suspend fun deleteAllScores() = gameDataSource.deleteAllScores()

    override fun getAllScores(): Flow<List<Score>> {
        return gameDataSource.getAllScores().map { list ->
            list.map { it.toScore() }
        }
    }

    override suspend fun getLastPlayedDifficulty(): Difficulty? = gameDataSource.getLastPlayedDifficulty()

    override suspend fun getRecentUsernames(): List<String> = gameDataSource.getRecentUsernames()

    override suspend fun saveRecentUsernames(usernames: List<String>) = gameDataSource.saveRecentUsernames(usernames)

    // Theme settings
    override fun isDarkThemeFlow(): Flow<Boolean> = gameDataSource.isDarkThemeFlow()
    override suspend fun setDarkTheme(isDark: Boolean) = gameDataSource.setDarkTheme(isDark)

    // Timer settings
    override fun isTimerVisibleFlow(): Flow<Boolean> = gameDataSource.isTimerVisibleFlow()
    override suspend fun setTimerVisible(isVisible: Boolean) = gameDataSource.setTimerVisible(isVisible)
}