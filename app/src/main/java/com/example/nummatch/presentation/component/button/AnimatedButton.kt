package com.example.nummatch.presentation.component.button

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nummatch.presentation.theme.DisableButtonBackground
import com.example.nummatch.presentation.theme.EnableButtonBackground

@Composable
fun AnimatedButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    secondaryText: String? = null,
    isSelected: Boolean = false,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    loadingText: String? = null,
    animateScale: Boolean = true,
    animateColor: Boolean = true,
    buttonStyle: AnimatedButtonStyle = AnimatedButtonStyle.Filled,
    colors: AnimatedButtonColors = AnimatedButtonDefaults.defaultColors(),
    shape: Shape = MaterialTheme.shapes.medium,
    border: BorderStroke? = null,
    elevation: Dp = 0.dp,
    fontSize: TextUnit = 16.sp,
    fontWeight: FontWeight = FontWeight.Normal,
    iconSize: Dp = 20.dp
) {
    val animatedContainerColor by animateColorAsState(
        targetValue = when {
            !enabled -> colors.disabledContainerColor
            isSelected && animateColor -> colors.selectedContainerColor
            else -> colors.containerColor
        },
        label = "button_container_color"
    )

    val animatedContentColor by animateColorAsState(
        targetValue = when {
            !enabled -> colors.disabledContentColor
            isSelected -> colors.selectedContentColor
            else -> colors.contentColor
        },
        label = "button_content_color"
    )

    val animatedScale by animateFloatAsState(
        targetValue = when {
            !enabled -> 1f
            isSelected && animateScale -> 1.05f
            else -> 1f
        },
        label = "button_scale"
    )

    val buttonContent: @Composable () -> Unit = {
        if (isLoading) {
            LoadingContent(
                loadingText = loadingText ?: text,
                contentColor = animatedContentColor,
                iconSize = iconSize,
                fontSize = fontSize
            )
        } else {
            ButtonContent(
                text = text,
                secondaryText = secondaryText,
                icon = icon,
                contentColor = animatedContentColor,
                fontSize = fontSize,
                fontWeight = if (isSelected) FontWeight.Bold else fontWeight,
                iconSize = iconSize
            )
        }
    }

    when (buttonStyle) {
        AnimatedButtonStyle.Filled -> {
            Button(
                onClick = onClick,
                enabled = enabled && !isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = animatedContainerColor,
                    contentColor = animatedContentColor,
                    disabledContainerColor = colors.disabledContainerColor,
                    disabledContentColor = colors.disabledContentColor
                ),
                shape = shape,
                border = border,
                modifier = modifier.scale(animatedScale),
                content = { buttonContent() }
            )
        }
        AnimatedButtonStyle.Elevated -> {
            ElevatedButton(
                onClick = onClick,
                enabled = enabled && !isLoading,
                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = animatedContainerColor,
                    contentColor = animatedContentColor,
                    disabledContainerColor = colors.disabledContainerColor,
                    disabledContentColor = colors.disabledContentColor
                ),
                elevation = ButtonDefaults.elevatedButtonElevation(
                    defaultElevation = elevation,
                    pressedElevation = elevation
                ),
                shape = shape,
                border = border,
                modifier = modifier.scale(animatedScale),
                content = { buttonContent() }
            )
        }
    }
}

@Composable
private fun ButtonContent(
    text: String,
    secondaryText: String?,
    icon: ImageVector?,
    contentColor: Color,
    fontSize: TextUnit,
    fontWeight: FontWeight,
    iconSize: Dp
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        icon?.let {
            Icon(
                imageVector = it,
                contentDescription = null,
                modifier = Modifier.size(iconSize),
                tint = contentColor
            )
            Spacer(modifier = Modifier.width(8.dp))
        }

        if (secondaryText != null) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = text,
                    fontSize = fontSize,
                    fontWeight = fontWeight,
                    color = contentColor
                )
                Text(
                    text = secondaryText,
                    fontSize = fontSize * 0.8f,
                    color = contentColor.copy(alpha = 0.8f)
                )
            }
        } else {
            Text(
                text = text,
                fontSize = fontSize,
                fontWeight = fontWeight,
                color = contentColor
            )
        }
    }
}

@Composable
private fun LoadingContent(
    loadingText: String,
    contentColor: Color,
    iconSize: Dp,
    fontSize: TextUnit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(iconSize),
            color = contentColor,
            strokeWidth = 2.dp
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = loadingText,
            fontSize = fontSize,
            color = contentColor
        )
    }
}

enum class AnimatedButtonStyle {
    Filled,
    Elevated
}

data class AnimatedButtonColors(
    val containerColor: Color,
    val contentColor: Color,
    val selectedContainerColor: Color,
    val selectedContentColor: Color,
    val disabledContainerColor: Color,
    val disabledContentColor: Color
)

object AnimatedButtonDefaults {
    @Composable
    fun defaultColors(
        containerColor: Color = EnableButtonBackground,
        contentColor: Color = MaterialTheme.colorScheme.onPrimary,
        selectedContainerColor: Color = EnableButtonBackground,
        selectedContentColor: Color = MaterialTheme.colorScheme.onPrimary,
        disabledContainerColor: Color = DisableButtonBackground,
        disabledContentColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
    ) = AnimatedButtonColors(
        containerColor = containerColor,
        contentColor = contentColor,
        selectedContainerColor = selectedContainerColor,
        selectedContentColor = selectedContentColor,
        disabledContainerColor = disabledContainerColor,
        disabledContentColor = disabledContentColor
    )

    @Composable
    fun difficultyColors() = defaultColors(
        containerColor = DisableButtonBackground,
        contentColor = MaterialTheme.colorScheme.onSurface,
        selectedContainerColor = EnableButtonBackground,
        selectedContentColor = MaterialTheme.colorScheme.onPrimary,

    )
}