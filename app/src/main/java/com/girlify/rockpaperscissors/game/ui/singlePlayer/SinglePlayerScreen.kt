package com.girlify.rockpaperscissors.game.ui.singlePlayer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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


@Composable
fun GameScreen(singlePlayerViewModel: SinglePlayerViewModel) {
    val options = listOf(Options.ROCK, Options.PAPER, Options.SCISSORS)
    val showLoadingAnimation: Boolean by singlePlayerViewModel.showLoadingAnimation.observeAsState(
        false
    )
    val isEnable: Boolean by singlePlayerViewModel.isEnable.observeAsState(true)
    val playerElection: String by singlePlayerViewModel.playerElection.observeAsState("")
    val computerElection: String by singlePlayerViewModel.computerElection.observeAsState("")
    val result: String by singlePlayerViewModel.result.observeAsState("")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (showLoadingAnimation) {
            LoadingAnimation()
        }

        Text("Elegí tu jugada")
        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(options) {
                PlayButton(it, isEnable ) {
                    singlePlayerViewModel.onClick(it, options.random())
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        if (result.isNotEmpty()) {
            ResultAnimation(result,playerElection,computerElection){
                singlePlayerViewModel.onRestart()
            }
        }
    }
}
@Composable
fun ResultAnimation(result: String, playerElection: String, computerElection: String, restart: () -> Unit) {
    Dialog(onDismissRequest = { restart() }) {
        Column(
            Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val res: Int = when(result){
                Options.WIN -> R.raw.win
                Options.LOST -> R.raw.lost
                else -> R.raw.draw
            }
            val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(res))
            val progress by animateLottieCompositionAsState(
                composition,
                iterations = LottieConstants.IterateForever
            )
            Text(
                text = result,
                fontSize = 54.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            LottieAnimation(
                composition = composition,
                progress = progress,
                modifier = Modifier.size(256.dp)
            )
            Text("Vos elegiste: $playerElection",color = Color.White)
            Spacer(modifier = Modifier.height(8.dp))
            Text("Computadora: $computerElection",color = Color.White)
            Spacer(modifier = Modifier.height(16.dp))
            RestartButton {
                restart()
            }
        }
    }
}


@Composable
fun RestartButton(onClick: () -> Unit) {
    Button(onClick = { onClick() }) {
        Text(text = "Reiniciar")
    }
}

@Composable
fun PlayButton(text: String, buttonState: Boolean, onClick: () -> Unit) {
    FilledIconButton(
        onClick = { onClick() },
        enabled = buttonState,
        modifier = Modifier.size(154.dp)
    ) {
        Icon(
            painter = when(text){
                Options.ROCK -> painterResource(id = R.drawable.rock)
                Options.PAPER -> painterResource(id = R.drawable.paper)
                else -> painterResource(id = R.drawable.scissors)
            },
            contentDescription = text
        )
    }
}

@Composable
fun LoadingAnimation() {
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
            LottieAnimation(
                composition = composition,
                progress = progress,
            )
        }
    }
}