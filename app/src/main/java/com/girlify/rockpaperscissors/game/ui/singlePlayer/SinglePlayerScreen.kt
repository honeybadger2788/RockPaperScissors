package com.girlify.rockpaperscissors.game.ui.singlePlayer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.girlify.rockpaperscissors.R
import com.girlify.rockpaperscissors.game.core.model.Options

@Preview
@Composable
fun GameScreen(singlePlayerViewModel: SinglePlayerViewModel = viewModel()) {
    val showLoadingAnimation: Boolean by singlePlayerViewModel.showLoadingAnimation.observeAsState(
        false
    )
    val isEnable: Boolean by singlePlayerViewModel.isEnable.observeAsState(true)
    val playerElection: String by singlePlayerViewModel.playerElection.observeAsState("")
    val computerElection: String by singlePlayerViewModel.computerElection.observeAsState("")
    val result: String by singlePlayerViewModel.result.observeAsState("")

    if (showLoadingAnimation) {
        LoadingAnimation()
    }

    OptionsLayout(isEnable){
        singlePlayerViewModel.onClick(it)
    }

    if (result.isNotEmpty()) {
        ResultAnimation(result,playerElection,computerElection){
            singlePlayerViewModel.onRestart()
        }
    }
}

@Composable
fun Title() {
    Text(
        "Elegí tu jugada",
        fontWeight = FontWeight.Bold,
        fontSize = 48.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun OptionsLayout(isEnable: Boolean, onClick: (String) -> Unit) {
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (boxTitle,boxRock, boxPaper, boxScissors) = createRefs()

        Box(modifier = Modifier
            .constrainAs(boxTitle){
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(boxRock.top)
            })  {
            Title()
        }

        Box(modifier = Modifier
            .constrainAs(boxRock) {
                top.linkTo(boxTitle.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                bottom.linkTo(boxPaper.top)
            }) {
            PlayButton(Options.ROCK, isEnable ) {
                onClick(Options.ROCK)
            }
        }

        Box(modifier = Modifier
            .constrainAs(boxPaper) {
                top.linkTo(boxRock.bottom)
                start.linkTo(parent.start)
                end.linkTo(boxScissors.start)
                bottom.linkTo(parent.bottom)
            }){
            PlayButton(Options.PAPER, isEnable ) {
                onClick(Options.PAPER)
            }
        }

        Box(modifier = Modifier
            .constrainAs(boxScissors) {
                top.linkTo(boxRock.bottom)
                start.linkTo(boxPaper.end)
                end.linkTo(parent.end)
                bottom.linkTo(parent.bottom)
            }){
            PlayButton(Options.SCISSORS, isEnable ) {
                onClick(Options.SCISSORS)
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