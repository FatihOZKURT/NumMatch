package com.example.nummatch.presentation.component.input

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.nummatch.R
import com.example.nummatch.presentation.component.button.CustomIconButton
import com.example.nummatch.presentation.component.button.CustomIconButtonDefaults.clearIconColors
import com.example.nummatch.presentation.component.button.IconButtonStyle

@Composable
fun UsernameInput(
    username: String,
    onUsernameChange: (String) -> Unit,
    usernameError: String? = null,
    onClearError: () -> Unit = {},
    recentUsernames: List<String> = emptyList(),
    onRecentUsernameClick: (String) -> Unit = {},
    label: String = stringResource(R.string.enter_username),
    recentPlayersLabel: String = stringResource(R.string.recent_players),
    showRecentUsers: Boolean = true,
    maxRecentUsers: Int = 5,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        // Username TextField
        OutlinedTextField(
            value = username,
            onValueChange = { newValue ->
                onUsernameChange(newValue)
                if (usernameError != null) onClearError()
            },
            label = { Text(label) },
            singleLine = true,
            isError = usernameError != null,
            supportingText = if (usernameError != null) {
                {
                    Text(
                        text = usernameError,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            } else null,
            trailingIcon = if (username.isNotEmpty()) {
                {
                    ClearButton(onClear = { onUsernameChange("") })
                }
            } else null,
            modifier = Modifier.fillMaxWidth(),
            shape = shapes.large
        )

        // Recent Users Section
        if (showRecentUsers && recentUsernames.isNotEmpty()) {
            RecentUsersSection(
                recentUsernames = recentUsernames.take(maxRecentUsers),
                onRecentUsernameClick = onRecentUsernameClick,
                label = recentPlayersLabel
            )
        }
    }
}

@Composable
private fun ClearButton(
    onClear: () -> Unit,
    modifier: Modifier = Modifier
) {
    CustomIconButton(
        icon = Icons.Default.Clear,
        contentDescription = stringResource(R.string.clear_username),
        onClick = onClear,
        colors = clearIconColors(),
        size = 40.dp,
        iconSize = 20.dp,
        style = IconButtonStyle.Transparent,
        modifier = modifier
    )
}

@Composable
private fun RecentUsersSection(
    recentUsernames: List<String>,
    onRecentUsernameClick: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(recentUsernames) { recentUsername ->
                SuggestionChip(
                    onClick = { onRecentUsernameClick(recentUsername) },
                    label = { Text(recentUsername) }
                )
            }
        }
    }
}