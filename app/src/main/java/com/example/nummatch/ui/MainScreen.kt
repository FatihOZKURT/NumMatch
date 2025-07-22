package com.example.nummatch.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.nummatch.R
import com.example.nummatch.ui.theme.NumMatchTheme

@Composable
fun MainScreen(modifier: Modifier = Modifier) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(25.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = {},
            modifier = modifier.size(width = 150.dp, height = 40.dp)
        ) {
            Text(
                text = stringResource(R.string.create_game)
            )
        }

        Button(
            onClick = {},
            modifier = modifier.size(width = 150.dp, height = 40.dp),
        ) {
            Text(
                text = stringResource(R.string.my_scores)
            )
        }

        Button(
            onClick = {},
            modifier = modifier.size(width = 150.dp, height = 40.dp),
        ) {
            Text(
                text = stringResource(R.string.settings)
            )
        }
    }

}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    NumMatchTheme {
        MainScreen()
    }
}

