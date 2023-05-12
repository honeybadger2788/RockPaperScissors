package com.girlify.rockpaperscissors.game.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.girlify.rockpaperscissors.R
import com.girlify.rockpaperscissors.game.core.Options


@Composable
fun GameScreen(gameViewModel: GameViewModel) {
    val options = listOf(Options.ROCK, Options.PAPER, Options.SCISSORS)
    val showAnimation: Boolean by gameViewModel.showAnimation.observeAsState(false)
    val isEnable: Boolean by gameViewModel.isEnable.observeAsState(true)
    val playerElection: String by gameViewModel.playerElection.observeAsState("")
    val computerElection: String by gameViewModel.computerElection.observeAsState("")
    val result: String by gameViewModel.result.observeAsState("")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (showAnimation) {
            LottieExample()
        }

        Text("Elige tu jugada")
        Spacer(modifier = Modifier.height(16.dp))

        LazyRow(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
            items(options) {
                BotonJugada(it, isEnable ) {
                    gameViewModel.onClick(it, options.random())
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        if (result.isNotEmpty()) {
            Text("Vos elegiste: $playerElection")
            Text("Computadora: $computerElection")
            Text("Resultado: $result")
            Spacer(modifier = Modifier.height(16.dp))
            BotonReiniciar {
                gameViewModel.onRestart()
            }
        }
    }
}


@Composable
fun BotonReiniciar(onClick: () -> Unit) {
    Button(onClick = { onClick() }) {
        Text(text = "Reiniciar")
    }
}

@Composable
fun BotonJugada(s: String, buttonState: Boolean, onClick: () -> Unit) {
    Button(onClick = { onClick() }, enabled = buttonState) {
        Text(text = s)
    }
}

@Composable
fun LottieExample() {
    Dialog(onDismissRequest = { }) {
        Column(
            Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // LottieAnimation
            // Pass the composition and the progress state
            val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.game))
            val progress by animateLottieCompositionAsState(
                composition,
                iterations = LottieConstants.IterateForever
            )
            LottieAnimation(
                composition = composition,
                progress = progress,
            )
        }
    }
}