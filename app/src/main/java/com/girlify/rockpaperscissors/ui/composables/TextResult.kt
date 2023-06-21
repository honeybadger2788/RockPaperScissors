package com.girlify.rockpaperscissors.ui.composables

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun TextResult(username: String, playerElection: String) {
    Text(
        "$username: $playerElection",
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth(),
        color = Color.White
    )
    Spacer(modifier = Modifier.height(8.dp))
}