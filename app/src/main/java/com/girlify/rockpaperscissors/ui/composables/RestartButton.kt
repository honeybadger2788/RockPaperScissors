package com.girlify.rockpaperscissors.ui.composables

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun RestartButton(onClick: () -> Unit) {
    Button(onClick = { onClick() }) {
        Text(text = "Reiniciar")
    }
}