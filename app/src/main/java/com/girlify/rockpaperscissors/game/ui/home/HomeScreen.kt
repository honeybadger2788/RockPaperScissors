package com.girlify.rockpaperscissors.game.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun HomeScreen(
    navigateToSinglePlayer: () -> Unit,
    navigateToMultiPlayer: (String) -> Unit,
    homeViewModel: HomeViewModel = viewModel()
) {
    val showDialog: Boolean by homeViewModel.showDialog.observeAsState(false)
    val isButtonEnable: Boolean by homeViewModel.isButtonEnable.observeAsState(false)
    val username: String by homeViewModel.username.observeAsState("")

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Elige el modo de juego")
        Spacer(modifier = Modifier.size(16.dp))
        Button(onClick = { navigateToSinglePlayer() }) {
            Text(text = "Single Player")
        }
        Button(onClick = { homeViewModel.onClick() }) {
            Text(text = "Multi Player")
        }
        if (showDialog){
            DialogPlayerName(
                { navigateToMultiPlayer(it) },
                username,
                isButtonEnable,
                { homeViewModel.onDismiss() },
                { homeViewModel.onCheck(it) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogPlayerName(
    navigateToMultiPlayer: (String) -> Unit,
    username: String,
    isButtonEnable: Boolean,
    onDismiss: () -> Unit,
    onCheck: (String) -> Unit
) {
    Dialog(onDismissRequest = { onDismiss() }) {
        Column(
            Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Ingresa nombre de jugador")
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = username,
                onValueChange = { onCheck(it) },
                singleLine = true,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { navigateToMultiPlayer(username) }, enabled = isButtonEnable) {
                Text(text = "Enviar")
            }
        }
    }
}
