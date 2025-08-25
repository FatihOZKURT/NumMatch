package com.example.nummatch.presentation.screen.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.EaseInOutQuad
import androidx.compose.animation.core.EaseOutCubic
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.nummatch.R
import com.example.nummatch.presentation.component.button.AnimatedButton
import com.example.nummatch.presentation.component.button.AnimatedButtonDefaults
import com.example.nummatch.presentation.component.button.AnimatedButtonStyle
import com.example.nummatch.presentation.navigation.Screen
import com.example.nummatch.presentation.theme.EnableButtonBackground
import com.example.nummatch.presentation.theme.Transparent

@Composable
fun MainScreen(navController: NavHostController, modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = stringResource(R.string.infinite))

    val animatedScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse
        ),
        label = stringResource(R.string.scale)
    )

    val animatedOffsetY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -12f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = EaseInOutQuad),
            repeatMode = RepeatMode.Reverse
        ),
        label = stringResource(R.string.bounce)
    )

    MainScreenContent(
        animatedScale = animatedScale,
        animatedOffsetY = animatedOffsetY,
        onNavigate = { route -> navController.navigate(route) },
        modifier = modifier
    )
}

@Composable
fun MainScreenContent(
    animatedScale: Float,
    animatedOffsetY: Float,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AppTitleSection(
                animatedScale = animatedScale,
                animatedOffsetY = animatedOffsetY
            )

            Spacer(modifier = Modifier.height(48.dp))

            MenuButtons(onNavigate = onNavigate)
        }
    }
}

@Composable
private fun AppTitleSection(
    animatedScale: Float,
    animatedOffsetY: Float
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // App Logo
        Card(
            modifier = Modifier
                .size(180.dp)
                .scale(animatedScale)
                .offset(y = animatedOffsetY.dp),
            shape = CircleShape,
            colors = CardDefaults.cardColors(
                containerColor = Transparent
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.app_icon2),
                    contentDescription = "App Logo",
                    modifier = Modifier.size(280.dp),
                    contentScale = ContentScale.Fit
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // App Title
        Text(
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
private fun MenuButtons(onNavigate: (String) -> Unit) {
    val menuItems = listOf(
        MenuButtonData(
            text = stringResource(R.string.create_game),
            icon = Icons.Default.PlayArrow,
            color = MaterialTheme.colorScheme.primary,
            route = Screen.GameSetup.route
        ),
        MenuButtonData(
            text = stringResource(R.string.scores),
            icon = Icons.Default.Star,
            color = MaterialTheme.colorScheme.primary,
            route = Screen.Score.route
        ),
        MenuButtonData(
            text = stringResource(R.string.settings),
            icon = Icons.Default.Settings,
            color = MaterialTheme.colorScheme.primary,
            route = Screen.Settings.route
        )
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        menuItems.forEachIndexed { index, item ->
            AnimatedMenuButton(
                item = item,
                onClick = { onNavigate(item.route) },
                delay = index * 100
            )
        }
    }
}

@Composable
private fun AnimatedMenuButton(
    item: MenuButtonData,
    onClick: () -> Unit,
    delay: Int
) {
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(delay.toLong())
        isVisible = true
    }

    AnimatedVisibility(
        visible = isVisible,
        enter = slideInHorizontally(
            initialOffsetX = { it },
            animationSpec = tween(500, easing = EaseOutCubic)
        ) + fadeIn(animationSpec = tween(500))
    ) {
        AnimatedButton(
            text = item.text,
            icon = item.icon,
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(56.dp),
            buttonStyle = AnimatedButtonStyle.Elevated,
            colors = AnimatedButtonDefaults.defaultColors(
                containerColor = EnableButtonBackground,
                contentColor = MaterialTheme.colorScheme.tertiary
            ),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, item.color.copy(alpha = 0.7f)),
            elevation = 0.dp,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            iconSize = 24.dp,
            animateScale = false,
            animateColor = false
        )
    }
}

data class MenuButtonData(
    val text: String,
    val icon: ImageVector,
    val color: Color,
    val route: String
)
