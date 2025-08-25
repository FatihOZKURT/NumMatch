package com.example.nummatch.presentation.component.state

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.nummatch.R

@Composable
fun EmptyState(
    message: String,
    modifier: Modifier = Modifier,
    title: String? = null,
    icon: ImageVector = Icons.Default.Inbox,
    iconTint: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    description: String? = null,
    primaryAction: EmptyStateAction? = null,
    secondaryAction: EmptyStateAction? = null
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 32.dp)
        ) {
            // Empty State Icon
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(80.dp),
                tint = iconTint
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Title
            if (title != null) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Main Message
            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            // Description
            if (description != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }

            // Action Buttons
            if (primaryAction != null || secondaryAction != null) {
                Spacer(modifier = Modifier.height(32.dp))

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    primaryAction?.let { action ->
                        Button(onClick = action.onClick) {
                            Text(action.text)
                        }
                    }

                    secondaryAction?.let { action ->
                        TextButton(onClick = action.onClick) {
                            Text(action.text)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyScoresState(
    modifier: Modifier = Modifier,
    onStartGame: (() -> Unit)? = null
) {
    EmptyState(
        title = stringResource(R.string.no_scores_yet),
        message = stringResource(R.string.no_scores_message),
        description = stringResource(R.string.play_first_game),
        icon = Icons.Default.EmojiEvents,
        iconTint = MaterialTheme.colorScheme.primary,
        primaryAction = onStartGame?.let {
            EmptyStateAction(
                text = stringResource(R.string.start_playing),
                onClick = it
            )
        },
        modifier = modifier
    )
}

@Composable
fun NoSearchResultsState(
    searchQuery: String = "",
    modifier: Modifier = Modifier,
    onClearFilters: (() -> Unit)? = null
) {
    EmptyState(
        title = stringResource(R.string.no_results_found),
        message = if (searchQuery.isNotEmpty()) {
            stringResource(R.string.no_results_for_query, searchQuery)
        } else {
            stringResource(R.string.no_results_message)
        },
        description = stringResource(R.string.try_different_search),
        icon = Icons.Default.SearchOff,
        secondaryAction = onClearFilters?.let {
            EmptyStateAction(
                text = stringResource(R.string.clear_filters),
                onClick = it
            )
        },
        modifier = modifier
    )
}

data class EmptyStateAction(
    val text: String,
    val onClick: () -> Unit
)