package com.example.nummatch.presentation.component.state

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
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
fun ErrorMessage(
    message: String,
    modifier: Modifier = Modifier,
    title: String? = null,
    icon: ImageVector = Icons.Default.Error,
    iconTint: Color = MaterialTheme.colorScheme.error,
    onRetry: (() -> Unit)? = null,
    onDismiss: (() -> Unit)? = null,
    retryText: String = stringResource(R.string.retry),
    dismissText: String = stringResource(R.string.dismiss)
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Error Icon
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = iconTint
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Error Title
            if (title != null) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Error Message
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            // Action Buttons
            if (onRetry != null || onDismiss != null) {
                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    onDismiss?.let { dismiss ->
                        TextButton(onClick = dismiss) {
                            Text(dismissText)
                        }
                    }

                    if (onRetry != null && onDismiss != null) {
                        Spacer(modifier = Modifier.width(8.dp))
                    }

                    onRetry?.let { retry ->
                        Button(onClick = retry) {
                            Text(retryText)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FullScreenErrorMessage(
    message: String,
    modifier: Modifier = Modifier,
    title: String = stringResource(R.string.error_occurred),
    onRetry: (() -> Unit)? = null,
    onDismiss: (() -> Unit)? = null
) {
    ErrorMessage(
        message = message,
        title = title,
        onRetry = onRetry,
        onDismiss = onDismiss,
        modifier = modifier.fillMaxSize()
    )
}