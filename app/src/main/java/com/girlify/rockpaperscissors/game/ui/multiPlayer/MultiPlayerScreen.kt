package com.girlify.rockpaperscissors.game.ui.multiPlayer

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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.girlify.rockpaperscissors.R
import com.girlify.rockpaperscissors.game.core.model.Options

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VsPlayerGame(multiPlayerViewModel: MultiPlayerViewModel) {
    val uiState by produceState<UiState>(initialValue = UiState.Loading) {
        multiPlayerViewModel.uiState.collect { value = it }
    }

    val options = listOf(Options.ROCK, Options.PAPER, Options.SCISSORS)
    val showAnimation: Boolean by multiPlayerViewModel.showAnimation.observeAsState(false)
    val showCode: Boolean by multiPlayerViewModel.showCode.observeAsState(true)
    val isEnable: Boolean by multiPlayerViewModel.isEnable.observeAsState(false)
    val isCodeButtonEnable: Boolean by multiPlayerViewModel.isCodeButtonEnable.observeAsState(false)
    val playerElection: String by multiPlayerViewModel.playerElection.observeAsState("")
    val opponentElection: String by multiPlayerViewModel.opponentElection.observeAsState("")
    val result: String by multiPlayerViewModel.result.observeAsState("")
    val gameId: String by multiPlayerViewModel.gameId.observeAsState("")
    val code: String by multiPlayerViewModel.code.observeAsState("")
    val player: Int by multiPlayerViewModel.player.observeAsState(1)


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
        when(uiState){
            UiState.Error -> TODO()
            UiState.Loading -> CircularProgressIndicator()
            is UiState.Success -> {
                Text("Elige tu jugada Multi Player")
                Spacer(modifier = Modifier.height(16.dp))

                LazyRow(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                    items(options) {
                        BotonJugada(it, isEnable ) {
                            multiPlayerViewModel.onPlay(gameId,player,it)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                if (showCode) {
                    Text("Dile a tu amigo que ingrese este código")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(gameId, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("o ingresa el suyo")
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(value = code, onValueChange = { multiPlayerViewModel.onCheckCode(it) })
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = { multiPlayerViewModel.onSendCode(code) }, enabled = isCodeButtonEnable) {
                        Text(text = "Jugar")
                    }
                }

                if (result.isNotEmpty()) {
                    Text("Vos elegiste: $playerElection")
                    Text("Tu amigo eligió: $opponentElection")
                    Text("Resultado: $result")
                    Spacer(modifier = Modifier.height(16.dp))
                    BotonReiniciar {
                        multiPlayerViewModel.onRestart()
                    }
                }
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
            val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.game))
            val progress by animateLottieCompositionAsState(
                composition,
                iterations = LottieConstants.IterateForever
            )
            Text(text = "Esperando jugada")
            LottieAnimation(
                composition = composition,
                progress = progress,
            )
        }
    }
}