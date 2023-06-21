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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.girlify.rockpaperscissors.R

@Composable
fun HomeScreen(
    navigateToSinglePlayer: () -> Unit,
    navigateToMultiPlayer: (String) -> Unit,
    homeViewModel: HomeViewModel = hiltViewModel()
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
        Text(text = stringResource(R.string.home_title))
        Spacer(modifier = Modifier.size(16.dp))
        Button(onClick = { navigateToSinglePlayer() }) {
            Text(text = stringResource(R.string.single_player_button))
        }
        Button(onClick = { homeViewModel.onClick() }) {
            Text(text = stringResource(R.string.multi_player_button))
        }
        if (showDialog){
            DialogUsername(
                {
                    homeViewModel.resetStates()
                    navigateToMultiPlayer(it) },
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
fun DialogUsername(
    goToMultiplayer: (String) -> Unit,
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
            Text(stringResource(R.string.username_dialog_title))
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = username,
                onValueChange = { onCheck(it) },
                singleLine = true,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = {
                goToMultiplayer(username)
            }, enabled = isButtonEnable) {
                Text(text = stringResource(R.string.send_button))
            }
        }
    }
}
