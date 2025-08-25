package com.example.nummatch.presentation.screen.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.nummatch.R
import com.example.nummatch.presentation.component.dialog.DeleteConfirmDialog
import com.example.nummatch.presentation.component.header.ScreenHeader
import com.example.nummatch.presentation.component.state.SmallLoadingIndicator
import com.example.nummatch.presentation.theme.NumMatchTheme
import com.example.nummatch.presentation.theme.RowBackground
import com.example.nummatch.util.showShortToast

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(uiState.deleteScoresResult) {
        uiState.deleteScoresResult?.let { result ->
            when (result) {
                is DeleteScoresResult.Success -> {
                    context.showShortToast(context.getString(R.string.scores_deleted))
                }

                is DeleteScoresResult.Error -> {
                    context.showShortToast(result.message)
                }
            }
            viewModel.onEvent(SettingsEvent.ClearDeleteResult)
        }
    }

    if (uiState.showDeleteDialog) {
        DeleteConfirmDialog(
            itemName = "scores",
            onConfirm = { viewModel.onEvent(SettingsEvent.DeleteScores) },
            onDismiss = { viewModel.onEvent(SettingsEvent.HideDeleteDialog) }
        )
    }

    SettingsContent(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onBackClick = onBackClick,
        modifier = modifier
    )
}

@Composable
private fun SettingsContent(
    uiState: SettingsUiState,
    onEvent: (SettingsEvent) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(horizontal = 16.dp)
    ) {
        // Header
        ScreenHeader(
            title = stringResource(R.string.settings),
            onBackClick = onBackClick
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Settings Options
        SettingsOption(
            title = stringResource(R.string.light_dark_theme),
            checked = uiState.isDarkTheme,
            onCheckedChange = { onEvent(SettingsEvent.ToggleDarkTheme) }
        )

        SettingsOption(
            title = stringResource(R.string.show_timer),
            checked = uiState.isTimerVisible,
            onCheckedChange = { onEvent(SettingsEvent.ToggleTimer) }
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Delete Scores Button
        Button(
            onClick = { onEvent(SettingsEvent.ShowDeleteDialog) },
            enabled = !uiState.isLoading,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            if (uiState.isLoading) {
                SmallLoadingIndicator()
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = if (uiState.isLoading) {
                    stringResource(R.string.loading)
                } else {
                    stringResource(R.string.clear_scores)
                }
            )
        }
    }
}

@Composable
private fun SettingsOption(
    title: String,
    checked: Boolean,
    onCheckedChange: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(RowBackground)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            modifier = Modifier.weight(1f)
        )
        Switch(
            checked = checked,
            onCheckedChange = { onCheckedChange() }
        )
    }
}

