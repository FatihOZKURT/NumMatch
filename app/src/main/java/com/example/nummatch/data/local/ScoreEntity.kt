package com.example.nummatch.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.nummatch.domain.model.Score

@Entity(tableName = "score_table")
data class ScoreEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userName: String,
    val score: Int,
    val date: Long = System.currentTimeMillis()
)

fun ScoreEntity.toScore(): Score {
    return Score(
        id = this.id,
        userName = this.userName,
        score = this.score,
        date = this.date
    )
}



