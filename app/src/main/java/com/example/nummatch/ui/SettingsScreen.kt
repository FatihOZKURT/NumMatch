package com.example.nummatch.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.nummatch.R
import com.example.nummatch.ui.theme.NumMatchTheme

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    isDarkTheme: Boolean = false,
    showTimer: Boolean = true,
    onBackClick: () -> Unit = {},
    onThemeToggle: (Boolean) -> Unit = {},
    onTimerToggle: (Boolean) -> Unit = {},
    onClearScoresClick: () -> Unit = {}
) {
    var darkTheme by remember { mutableStateOf(isDarkTheme) }
    var timerEnabled by remember { mutableStateOf(showTimer) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Geri Butonu ve Başlık
        Box(modifier = Modifier.fillMaxWidth()) {
            IconButton(
                onClick = { onBackClick() },
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.back)
                )
            }

            Text(
                text = stringResource(R.string.settings),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Tema Ayarı
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.LightGray)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(stringResource(R.string.dark_light_mode), modifier = Modifier.weight(1f))
            Switch(
                checked = darkTheme,
                onCheckedChange = {
                    darkTheme = it
                    onThemeToggle(it)
                }
            )
        }

        // Zamanlayıcı Ayarı
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.LightGray)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(stringResource(R.string.show_timer), modifier = Modifier.weight(1f))
            Switch(
                checked = timerEnabled,
                onCheckedChange = {
                    timerEnabled = it
                    onTimerToggle(it)
                }
            )
        }


        Spacer(modifier = Modifier.height(32.dp))

        // Skorları Temizle Butonu
        Button(
            onClick = onClearScoresClick,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(stringResource(R.string.clear_scores))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    NumMatchTheme {
        SettingsScreen()
    }
}
