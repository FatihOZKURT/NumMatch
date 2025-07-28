package com.example.nummatch.repo

import com.example.nummatch.datasource.ScoreDataSource
import com.example.nummatch.model.Score
import com.example.nummatch.room.ScoreEntity
import com.example.nummatch.room.toScore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ScoreRepository @Inject constructor(
    private val dataSource: ScoreDataSource
) {
    suspend fun insertScore(score: ScoreEntity) = dataSource.insertScore(score)

    suspend fun deleteAllScores() = dataSource.deleteAllScores()

    fun getAllScores(): Flow<List<Score>> {
        return dataSource.getAllScores().map { list ->
            list.map { it.toScore() }
        }
    }
}
