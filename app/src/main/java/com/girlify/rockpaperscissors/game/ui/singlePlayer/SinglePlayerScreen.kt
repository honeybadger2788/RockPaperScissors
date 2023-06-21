package com.girlify.rockpaperscissors.game.ui.singlePlayer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.girlify.rockpaperscissors.ui.composables.LoadingAnimation
import com.girlify.rockpaperscissors.ui.composables.OptionsLayout
import com.girlify.rockpaperscissors.ui.composables.RestartButton
import com.girlify.rockpaperscissors.ui.composables.ResultAnimation

@Preview
@Composable
fun SinglePlayerScreen(singlePlayerViewModel: SinglePlayerViewModel = hiltViewModel()) {
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
        ResultDialog(result,playerElection,computerElection){
            singlePlayerViewModel.onRestart()
        }
    }
}

@Composable
fun ResultDialog(result: String, playerElection: String, computerElection: String, restart: () -> Unit) {
    Dialog(onDismissRequest = { restart() }) {
        Column(
            Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ResultAnimation(result = result)
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
