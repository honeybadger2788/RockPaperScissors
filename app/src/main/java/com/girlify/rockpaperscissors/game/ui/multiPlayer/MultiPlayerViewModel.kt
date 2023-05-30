package com.girlify.rockpaperscissors.game.ui.multiPlayer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.girlify.rockpaperscissors.game.core.model.Options
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MultiPlayerViewModel: ViewModel() {
    private val _playerElection = MutableLiveData<String>()
    val playerElection: LiveData<String> = _playerElection

    private val _computerElection = MutableLiveData<String>()
    val computerElection: LiveData<String> = _computerElection

    private val _result = MutableLiveData<String>()
    val result: LiveData<String> = _result

    private val _showAnimation = MutableLiveData<Boolean>()
    val showAnimation: LiveData<Boolean> = _showAnimation

    private val _isEnable = MutableLiveData<Boolean>()
    val isEnable: LiveData<Boolean> = _isEnable

    fun onClick(player: String, computer: String) {
        viewModelScope.launch {
            _showAnimation.value = true
            _isEnable.value = false
            delay(3000)
            _playerElection.value = player
            _computerElection.value = computer
            _result.value = play(player, computer)
            _showAnimation.value = false
        }
    }

    fun onRestart() {
        viewModelScope.launch {
            _playerElection.value = ""
            _computerElection.value = ""
            _result.value = ""
            _isEnable.value = true
        }
    }

    private fun play(player: String, computer: String): String {
        return when {
            player == computer -> Options.DRAW
            player == Options.ROCK && computer == Options.SCISSORS ||
                    player == Options.PAPER && computer == Options.ROCK ||
                    player == Options.SCISSORS && computer == Options.PAPER -> Options.WIN
            else -> Options.LOST
        }
    }
}