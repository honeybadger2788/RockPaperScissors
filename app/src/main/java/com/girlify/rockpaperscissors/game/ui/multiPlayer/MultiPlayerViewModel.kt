package com.girlify.rockpaperscissors.game.ui.multiPlayer

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.girlify.rockpaperscissors.game.core.model.Options
import com.girlify.rockpaperscissors.game.data.network.FirebaseClient
import com.girlify.rockpaperscissors.game.data.response.GameModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.random.Random

class MultiPlayerViewModel: ViewModel() {

    private val repository = FirebaseClient()

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState
    /*private val _gameData= MutableStateFlow(GameModel())
    val gameData: StateFlow<GameModel?> = _gameData*/

    private val _gameId = MutableLiveData<String>()
    val gameId: LiveData<String> = _gameId

    private val _player = MutableLiveData<Int>()
    val player: LiveData<Int> = _player
/*
    private val _playerElection = MutableLiveData<String>()
    val playerElection: LiveData<String> = _playerElection

    private val _opponentElection = MutableLiveData<String>()
    val opponentElection: LiveData<String> = _opponentElection*/

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
        val gameId = generateRandomString()
        repository.setGame(gameId,"NOE")
        _player.value = 1
        /*generateRandomString()
        _uiState.value = UiState.Success(GameModel())*/
        gameListener(gameId)
        Log.i("NOE", "EJECUTANDO INIT")
    }

    private fun gameListener(gameId: String){
        viewModelScope.launch{
            repository.gameListener(gameId).collect{
                if (it != null) {
                    if (it.player2.isNotEmpty()){
                        _isEnable.value = true
                        _showCode.value = false
                        _uiState.value = UiState.Success(it)
                    } else {
                        repository.deleteGame(gameId)
                    }
                }
            }
        }
    }

    fun onSendCode(gameId: String) {
        viewModelScope.launch {
            //repository.gameListener(gameId)
            repository.updateGame(gameId,"LALALA")/*
            _isEnable.value = true
            _showCode.value = false*/
            _player.value = 2
        }
    }

    fun onCheckCode(code: String) {
        viewModelScope.launch {
            _code.value = code
            _isCodeButtonEnable.value = code.length == 6
        }
    }

    fun onPlay(gameId: String, player: Int, playerElection: String) {
        viewModelScope.launch {
            _isEnable.value = false
            repository.makeMove(gameId,player,playerElection)
            /*_playerElection.value = playerElection
            repository.gameListener(gameId).collect{
                if (it.opponentMove.isEmpty()){
                    _showAnimation.value = true
                } else {
                    _opponentElection.value = it.opponentMove
                    _showAnimation.value = false
                    _result.value = play(playerElection, it.opponentMove)
                }
            }*/
        }
    }

    fun onRestart() {
        viewModelScope.launch {
            /*_playerElection.value = ""
            _opponentElection.value = ""*/
            _result.value = ""
            _isEnable.value = true
        }
    }

    /*private fun generateRandomString() {
        val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        val random = Random.Default
        _gameId.value = (1..6)
            .map { random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("")
    }*/

    private fun generateRandomString(): String {
        val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        val random = Random.Default
        val gameId = (1..6)
            .map { random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("")
        _gameId.value = gameId
        return gameId
    }

    private fun play(player: String, opponent: String): String {
        return when {
            player == opponent -> Options.DRAW
            player == Options.ROCK && opponent == Options.SCISSORS ||
                    player == Options.PAPER && opponent == Options.ROCK ||
                    player == Options.SCISSORS && opponent == Options.PAPER -> Options.WIN
            else -> Options.LOST
        }
    }
}