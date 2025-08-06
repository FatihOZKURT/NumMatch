package com.example.nummatch.presentation.screen.score

import com.example.nummatch.model.Score
import kotlin.collections.filter
import kotlin.collections.isNotEmpty
import kotlin.collections.map
import kotlin.collections.sortedBy
import kotlin.collections.sortedByDescending
import kotlin.collections.take

data class ScoreUiState(
    val scores: List<Score> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val sortOrder: SortOrder = SortOrder.HIGHEST_FIRST,
    val filterBy: ScoreFilter = ScoreFilter.ALL,
    val searchQuery: String = ""
) {
    val sortedScores: List<Score>
        get() = when (sortOrder) {
            SortOrder.HIGHEST_FIRST -> scores.sortedByDescending { it.score }
            SortOrder.LOWEST_FIRST -> scores.sortedBy { it.score }
            SortOrder.NEWEST_FIRST -> scores.sortedByDescending { it.id }
            SortOrder.OLDEST_FIRST -> scores.sortedBy { it.id }
            SortOrder.ALPHABETICAL -> scores.sortedBy { it.userName.lowercase() }
        }

    val filteredScores: List<Score>
        get() {
            var filtered = sortedScores
            if (searchQuery.isNotBlank()) {
                filtered = filtered.filter {
                    it.userName.contains(searchQuery, ignoreCase = true)
                }
            }

            filtered = when (filterBy) {
                ScoreFilter.ALL -> filtered
                ScoreFilter.HIGH_SCORES -> filtered.filter { it.score >= 100 }
                ScoreFilter.MEDIUM_SCORES -> filtered.filter { it.score in 50..99 }
                ScoreFilter.LOW_SCORES -> filtered.filter { it.score < 50 }
                ScoreFilter.TOP_10 -> filtered.take(10)
            }

            return filtered
        }

    val isEmpty: Boolean
        get() = scores.isEmpty()

    val hasNoResults: Boolean
        get() = scores.isNotEmpty() && filteredScores.isEmpty()

    val totalScores: Int
        get() = scores.size

    val averageScore: Double
        get() = if (scores.isNotEmpty()) scores.map { it.score }.average() else 0.0

    val highestScore: Int
        get() = scores.maxOfOrNull { it.score } ?: 0

}

enum class SortOrder {
    HIGHEST_FIRST,
    LOWEST_FIRST,
    NEWEST_FIRST,
    OLDEST_FIRST,
    ALPHABETICAL
}

enum class ScoreFilter {
    ALL,
    HIGH_SCORES,
    MEDIUM_SCORES,
    LOW_SCORES,
    TOP_10
}
