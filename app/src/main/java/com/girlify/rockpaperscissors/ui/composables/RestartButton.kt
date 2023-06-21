package com.girlify.rockpaperscissors.ui.composables

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.girlify.rockpaperscissors.R

@Composable
fun RestartButton(onClick: () -> Unit) {
    Button(onClick = { onClick() }) {
        Text(text = stringResource(R.string.restart_button))
    }
}