package com.girlify.rockpaperscissors.game.ui.pvp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.girlify.rockpaperscissors.game.data.network.FirebaseClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

class VsPlayerViewModel: ViewModel() {

    private val repository = FirebaseClient()

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState

    private val _gameId = MutableLiveData<String>()
    val gameId: LiveData<String> = _gameId

    private val _player = MutableLiveData<Int>()
    val player: LiveData<Int> = _player

    private val _playerElection = MutableLiveData<String>()
    val playerElection: LiveData<String> = _playerElection

    private val _computerElection = MutableLiveData<String>()
    val computerElection: LiveData<String> = _computerElection

    private val _result = MutableLiveData<String>()
    val result: LiveData<String> = _result

    private val _code = MutableLiveData<String>()
    val code: LiveData<String> = _code

    private val _showAnimation = MutableLiveData<Boolean>()
    val showAnimation: LiveData<Boolean> = _showAnimation

    private val _showCode = MutableLiveData<Boolean>()
    val showCode: LiveData<Boolean> = _showCode

    private val _isEnable = MutableLiveData<Boolean>()
    val isEnable: LiveData<Boolean> = _isEnable

    private val _isCodeButtonEnable = MutableLiveData<Boolean>()
    val isCodeButtonEnable: LiveData<Boolean> = _isCodeButtonEnable

    init {
        generateRandomString()
    }

    fun onSendCode(gameId: String) {
        viewModelScope.launch {
            repository.gameListener(gameId)
            _isEnable.value = true
            _showCode.value = false
            _player.value = 2
        }
    }

    fun onCheck(code: String) {
        viewModelScope.launch {
            _isCodeButtonEnable.value = code.length >= 6
        }
    }

    fun onPlay(gameId: String, player: Int, playerElection: String) {
        viewModelScope.launch {
            _showAnimation.value = true
            _isEnable.value = false
            repository.makeMove(gameId,player,playerElection)
            _playerElection.value = playerElection
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

    private fun generateRandomString() {
        val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        val random = Random.Default
        _gameId.value = (1..6)
            .map { random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("")
    }

//    private fun play(player: String, computer: String): String {
//        return when {
//            player == computer -> Options.DRAW
//            player == Options.ROCK && computer == Options.SCISSORS ||
//                    player == Options.PAPER && computer == Options.ROCK ||
//                    player == Options.SCISSORS && computer == Options.PAPER -> Options.WIN
//            else -> Options.LOST
//        }
//    }
}