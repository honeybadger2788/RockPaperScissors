package com.girlify.rockpaperscissors.game.ui.multiPlayer

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.girlify.rockpaperscissors.game.core.model.Options
import com.girlify.rockpaperscissors.game.data.response.GameModel
import com.girlify.rockpaperscissors.ui.composables.LoadingAnimation
import com.girlify.rockpaperscissors.ui.composables.OptionsLayout
import com.girlify.rockpaperscissors.ui.composables.RestartButton
import com.girlify.rockpaperscissors.ui.composables.ResultAnimation
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun MultiPlayerScreen(username: String, goToHome: () -> Unit, multiPlayerViewModel: MultiPlayerViewModel = hiltViewModel()) {
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
    val error: String by multiPlayerViewModel.error.observeAsState("")

    LaunchedEffect(Unit) {
        multiPlayerViewModel.setPlayer(gameId,player,username)
    }

    LaunchedEffect(gameId) {
        if (gameId.isNotEmpty()){
            multiPlayerViewModel.startGameListener(gameId)
        } else {
            goToHome()
        }
    }

    BackHandler {
        multiPlayerViewModel.onEndGame(gameId)
        goToHome()
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

        if (showCode) {
            CodeDialog(
                gameId,
                code,
                { multiPlayerViewModel.onCheckCode(it) },
                { multiPlayerViewModel.onSendCode(gameId, code, 2, username) },
                isCodeButtonEnable,
                error
            )
        }

        if (result.isNotEmpty()) {
            gameData?.let {
                ResultDialog(
                    result,
                    it,
                    { multiPlayerViewModel.onRestart(gameId) },
                    {
                        multiPlayerViewModel.onEndGame(gameId)
                        goToHome()
                    }
                )
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
    isEnable: Boolean,
    error: String
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
                maxLines = 1,
                supportingText = { Text(text = error) },
                isError = error.isNotEmpty()
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
fun ResultDialog(result: String, gameData: GameModel, restart: () -> Unit, endGame: () -> Unit) {
    Dialog(onDismissRequest = { restart() }) {
        Column(
            Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ResultAnimation(result = result)
            Text("${gameData.player1}: ${gameData.player1Choice}", color = Color.White)
            Spacer(modifier = Modifier.height(8.dp))
            Text("${gameData.player2}: ${gameData.player2Choice}", color = Color.White)
            Spacer(modifier = Modifier.height(16.dp))
            RestartButton {
                restart()
            }
            Button(onClick = { endGame() }) {
                Text(text = "Finalizar juego")
            }
        }
    }
}