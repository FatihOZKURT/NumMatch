package com.example.nummatch.datasource

import com.example.nummatch.room.ScoreDao
import com.example.nummatch.room.ScoreEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ScoreDataSource @Inject constructor(
    private val scoreDao: ScoreDao
) {

    suspend fun insertScore(score: ScoreEntity) = scoreDao.insert(score)

    fun getAllScores(): Flow<List<ScoreEntity>> = scoreDao.getAllScores()

}
