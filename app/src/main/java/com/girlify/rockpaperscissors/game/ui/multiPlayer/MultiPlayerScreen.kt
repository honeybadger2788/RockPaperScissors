package com.girlify.rockpaperscissors.game.ui.multiPlayer

import androidx.compose.foundation.background
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.girlify.rockpaperscissors.R
import com.girlify.rockpaperscissors.game.core.model.Options
import com.girlify.rockpaperscissors.game.data.response.GameModel
import com.girlify.rockpaperscissors.game.ui.singlePlayer.Title

@Composable
fun MultiPlayerScreen(username: String,multiPlayerViewModel: MultiPlayerViewModel = viewModel()) {

    val showAnimation: Boolean by multiPlayerViewModel.showAnimation.observeAsState(false)
    val showCode: Boolean by multiPlayerViewModel.showCode.observeAsState(true)
    val isEnable: Boolean by multiPlayerViewModel.isEnable.observeAsState(false)
    val isCodeButtonEnable: Boolean by multiPlayerViewModel.isCodeButtonEnable.observeAsState(false)
    val result: String by multiPlayerViewModel.result.observeAsState("")
    val gameId: String by multiPlayerViewModel.gameId.observeAsState("")
    val code: String by multiPlayerViewModel.code.observeAsState("")
    val player: Int by multiPlayerViewModel.player.observeAsState(0)
    val gameData by multiPlayerViewModel.gameData.collectAsState(null)
    val message: String by multiPlayerViewModel.message.observeAsState("")

    LaunchedEffect(Unit) {
        multiPlayerViewModel.setPlayer(gameId,player,username)
    }

    LaunchedEffect(gameId) {
        multiPlayerViewModel.startGameListener(gameId)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (showAnimation) {
            Text(text = message)
            LoadingAnimation()
        }
        OptionsLayout(isEnable = isEnable){
            multiPlayerViewModel.onPlay(gameId,player,it)
        }
        Spacer(modifier = Modifier.height(16.dp))
        if (showCode) {
            CodeDialog(
                gameId,
                code,
                { multiPlayerViewModel.onCheckCode(it) },
                { multiPlayerViewModel.onSendCode(code, 2, username) },
                isCodeButtonEnable
            )
        }

        if (result.isNotEmpty()) {
            gameData?.let {
                val text = when(result){
                    username -> Options.WIN
                    Options.DRAW -> Options.DRAW
                    else -> Options.LOST
                }
                ResultAnimation(
                    text,
                    it
                ) {
                    multiPlayerViewModel.onRestart(gameId)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CodeDialog(
    gameId: String,
    code: String,
    onCheck: (String) -> Unit,
    onClick: () -> Unit,
    isEnable: Boolean
) {
    Dialog(onDismissRequest = { }) {
        Column(
            Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Dile a tu amigo que ingrese este código")
            Spacer(modifier = Modifier.height(8.dp))
            Text(gameId, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            Text("o ingresa el suyo")
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = code,
                onValueChange = { onCheck(it) },
                singleLine = true,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { onClick() },
                enabled = isEnable
            ) {
                Text(text = "Jugar")
            }
        }
    }
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
fun ResultAnimation(result: String, gameData: GameModel, restart: () -> Unit) {
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
            Text("${gameData.player1}: ${gameData.player1Choice}", color = Color.White)
            Spacer(modifier = Modifier.height(8.dp))
            Text("${gameData.player2}: ${gameData.player2Choice}", color = Color.White)
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
            Text(text = "Esperando jugada")
            LottieAnimation(
                composition = composition,
                progress = progress,
            )
        }
    }
}