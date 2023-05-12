package com.girlify.rockpaperscissors.game.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import kotlinx.coroutines.delay


@Composable
fun GameScreen() {
    val opciones = listOf(Options.ROCK, Options.PAPER, Options.SCISSORS)

    var eleccionJugador by remember { mutableStateOf("") }
    var eleccionComputadora by remember { mutableStateOf("") }
    var resultado by remember { mutableStateOf("") }
    var buttonState by remember { mutableStateOf(true) }
    var animationState by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LaunchedEffect(animationState) {
            if (animationState) {
                delay(3000)
                animationState = false
            }
        }

        if (animationState) {
            LottieExample()
        }

        Text("Elige tu jugada")
        Spacer(modifier = Modifier.height(16.dp))

        Row {
            BotonJugada(Options.ROCK, buttonState ) {
                animationState = true
                eleccionJugador = Options.ROCK
                eleccionComputadora = opciones.random()
                resultado = jugar(eleccionJugador, eleccionComputadora)
                buttonState = false
            }
            Spacer(modifier = Modifier.width(16.dp))
            BotonJugada(Options.PAPER, buttonState) {
                animationState = true
                eleccionJugador = Options.PAPER
                eleccionComputadora = opciones.random()
                resultado = jugar(eleccionJugador, eleccionComputadora)
                buttonState = false
            }
            Spacer(modifier = Modifier.width(16.dp))
            BotonJugada(Options.SCISSORS, buttonState) {
                animationState = true
                eleccionJugador = Options.SCISSORS
                eleccionComputadora = opciones.random()
                resultado = jugar(eleccionJugador, eleccionComputadora)
                buttonState = false
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        if (resultado.isNotEmpty()) {
            Text("Vos elegiste: $eleccionJugador")
            Text("Computadora: $eleccionComputadora")
            Text("Resultado: $resultado")
            Spacer(modifier = Modifier.height(16.dp))
            BotonReiniciar {
                buttonState = true
                eleccionJugador = ""
                eleccionComputadora = ""
                resultado = ""
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

fun jugar(jugador: String, computadora: String): String {
    return when {
        jugador == computadora -> "Empate"
        jugador == Options.ROCK && computadora == Options.SCISSORS ||
                jugador == Options.PAPER && computadora == Options.ROCK ||
                jugador == Options.SCISSORS && computadora == Options.PAPER -> "Ganaste"
        else -> "Perdiste"
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