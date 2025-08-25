package com.example.nummatch.presentation.component.button

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun CustomIconButton(
    icon: ImageVector,
    contentDescription: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    style: IconButtonStyle = IconButtonStyle.Standard,
    colors: IconButtonColors = CustomIconButtonDefaults.defaultColors(),
    size: Dp = 48.dp,
    iconSize: Dp = 24.dp,
    shape: Shape = CircleShape
) {
    when (style) {
        IconButtonStyle.Standard -> {
            IconButton(
                onClick = onClick,
                enabled = enabled,
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = colors.containerColor,
                    contentColor = colors.contentColor,
                    disabledContainerColor = colors.disabledContainerColor,
                    disabledContentColor = colors.disabledContentColor
                ),
                modifier = modifier.size(size)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = contentDescription,
                    modifier = Modifier.size(iconSize),
                    tint = if (enabled) colors.contentColor else colors.disabledContentColor
                )
            }
        }

        IconButtonStyle.Custom -> {
            Box(
                modifier = modifier
                    .size(size)
                    .clip(shape)
                    .background(
                        if (enabled) colors.containerColor else colors.disabledContainerColor
                    )
                    .clickable(enabled = enabled) { onClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = contentDescription,
                    modifier = Modifier.size(iconSize),
                    tint = if (enabled) colors.contentColor else colors.disabledContentColor
                )
            }
        }

        IconButtonStyle.Transparent -> {
            Box(
                modifier = modifier
                    .size(size)
                    .clip(shape)
                    .clickable(enabled = enabled) { onClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = contentDescription,
                    modifier = Modifier.size(iconSize),
                    tint = if (enabled) colors.contentColor else colors.disabledContentColor
                )
            }
        }
    }
}

enum class IconButtonStyle {
    Standard,
    Custom,
    Transparent
}

data class IconButtonColors(
    val containerColor: Color,
    val contentColor: Color,
    val disabledContainerColor: Color,
    val disabledContentColor: Color
)

object CustomIconButtonDefaults {
    @Composable
    fun defaultColors(
        containerColor: Color = Color.Transparent,
        contentColor: Color = MaterialTheme.colorScheme.tertiary,
        disabledContainerColor: Color = Color.Transparent,
        disabledContentColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
    ) = IconButtonColors(
        containerColor = containerColor,
        contentColor = contentColor,
        disabledContainerColor = disabledContainerColor,
        disabledContentColor = disabledContentColor
    )

    @Composable
    fun primaryColors() = defaultColors(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f),
        disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
    )

    @Composable
    fun surfaceColors() = defaultColors(
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface,
        disabledContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.12f),
        disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
    )

    @Composable
    fun clearIconColors() = defaultColors(
        containerColor = Color.Transparent,
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        disabledContainerColor = Color.Transparent,
        disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.38f)
    )
}