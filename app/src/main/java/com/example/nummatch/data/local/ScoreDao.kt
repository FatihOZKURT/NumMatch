package com.example.nummatch.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ScoreDao {

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insert(score: ScoreEntity)

    @Query("DELETE FROM score_table")
    suspend fun deleteAllScores()

    @Query("SELECT * FROM score_table ORDER BY score DESC")
    fun getAllScores(): Flow<List<ScoreEntity>>


}