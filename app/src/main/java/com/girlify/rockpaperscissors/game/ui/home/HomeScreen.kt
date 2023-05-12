package com.girlify.rockpaperscissors.game.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(navigateToPVC: () -> Unit, navigateToPVP: () -> Unit) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Elige el modo de juego")
        Spacer(modifier = Modifier.size(16.dp))
        Button(onClick = { navigateToPVC() }) {
            Text(text = "Player Vs Computer")
        }
        Button(onClick = { navigateToPVP() }) {
            Text(text = "Player Vs Player")
        }
    }
}