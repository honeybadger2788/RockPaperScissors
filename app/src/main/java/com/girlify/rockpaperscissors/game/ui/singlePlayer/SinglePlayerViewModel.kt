package com.girlify.rockpaperscissors.game.ui.singlePlayer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.girlify.rockpaperscissors.game.core.model.Options
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SinglePlayerViewModel @Inject constructor(): ViewModel() {
    private val options = listOf(Options.ROCK, Options.PAPER, Options.SCISSORS)

    private val _playerElection = MutableLiveData<String>()
    val playerElection: LiveData<String> = _playerElection

    private val _computerElection = MutableLiveData<String>()
    val computerElection: LiveData<String> = _computerElection

    private val _result = MutableLiveData<String>()
    val result: LiveData<String> = _result

    private val _showLoadingAnimation = MutableLiveData<Boolean>()
    val showLoadingAnimation: LiveData<Boolean> = _showLoadingAnimation

    private val _isEnable = MutableLiveData<Boolean>()
    val isEnable: LiveData<Boolean> = _isEnable

    fun onClick(player: String) {
        viewModelScope.launch {
            _showLoadingAnimation.value = true
            _isEnable.value = false
            delay(3000)
            _playerElection.value = player
            _showLoadingAnimation.value = false
            _result.value = getResult(player, options.random())
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

    private fun getResult(player: String, computer: String): String {
        _computerElection.value = computer
        return when {
            player == computer -> Options.DRAW_MESSAGE
            player == Options.ROCK && computer == Options.SCISSORS ||
                    player == Options.PAPER && computer == Options.ROCK ||
                    player == Options.SCISSORS && computer == Options.PAPER -> Options.WIN_MESSAGE
            else -> Options.LOST_MESSAGE
        }
    }
}