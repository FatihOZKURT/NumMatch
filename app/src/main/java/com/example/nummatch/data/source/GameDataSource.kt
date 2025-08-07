package com.example.nummatch.data.source

import com.example.nummatch.data.datastore.DataStoreManager
import com.example.nummatch.data.local.ScoreDao
import com.example.nummatch.data.local.ScoreEntity
import com.example.nummatch.util.Difficulty
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GameDataSource @Inject constructor(
    private val scoreDao: ScoreDao,
    private val dataStoreManager: DataStoreManager
) {

    suspend fun insertScore(score: ScoreEntity) = scoreDao.insert(score)

    suspend fun deleteAllScores() = scoreDao.deleteAllScores()

    fun getAllScores(): Flow<List<ScoreEntity>> = scoreDao.getAllScores()

    suspend fun getLastPlayedDifficulty(): Difficulty? = dataStoreManager.getLastPlayedDifficulty()

    suspend fun getRecentUsernames(): List<String> = dataStoreManager.getRecentUsernames()

    suspend fun saveRecentUsernames(usernames: List<String>) = dataStoreManager.saveRecentUsernames(usernames)

    fun isDarkThemeFlow(): Flow<Boolean> = dataStoreManager.isDarkThemeFlow

    suspend fun setDarkTheme(isDark: Boolean) = dataStoreManager.setDarkTheme(isDark)

    fun isTimerVisibleFlow(): Flow<Boolean> = dataStoreManager.isTimerVisibleFlow

    suspend fun setTimerVisible(isVisible: Boolean) = dataStoreManager.setShowTimer(isVisible)
}