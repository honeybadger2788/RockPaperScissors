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
import kotlinx.coroutines.launch
import kotlin.random.Random

class MultiPlayerViewModel: ViewModel() {

    private val repository = FirebaseClient()

    private val _gameData= MutableStateFlow(GameModel())
    val gameData: StateFlow<GameModel?> = _gameData

    private val _gameId = MutableLiveData<String>()
    val gameId: LiveData<String> = _gameId

    private val _player = MutableLiveData<Int>()
    val player: LiveData<Int> = _player

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

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    init {
        val gameId = generateRandomString()
        repository.setGame(gameId)
        _player.value = 1
        _gameId.value = gameId
        Log.i("NOE", "Ejecutando init")
    }

    fun startGameListener(gameId: String){
        viewModelScope.launch{
            repository.gameListener(gameId).collect{
                if (it != null) {
                    if (it.player2.isNotEmpty()){
                        _isEnable.value = true
                        _showCode.value = false
                        _gameData.value = it
                        if (it.player1Choice.isNotEmpty() && it.player2Choice.isNotEmpty()) {
                            _showAnimation.value = false
                            _result.value = play(it)
                        } else if (it.player1Choice.isEmpty() && it.player2Choice.isNotEmpty()){
                            _message.value = "Esperando jugada de Jugador 1..."
                        } else if (it.player2Choice.isEmpty() && it.player1Choice.isNotEmpty()){
                            _message.value = "Esperando jugada de Jugador 2..."
                        } else {
                            _message.value = ""
                        }
                    } /*else {
                        repository.deleteGame(gameId)
                    }*/
                }
            }
        }
    }

    fun onSendCode(gameId: String, player: Int, username: String) {
        viewModelScope.launch {
            _gameId.value = gameId
            _player.value = player
            _isEnable.value = true
            _showCode.value = false
            setPlayer(gameId,player,username)
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
            _showAnimation.value = true
            repository.makeMove(gameId,player,playerElection)
        }
    }

    // TODO restart playersChoice
    fun onRestart() {
        viewModelScope.launch {
            _result.value = ""
            _isEnable.value = true
        }
    }
    private fun generateRandomString(): String {
        val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        val random = Random.Default
        return (1..6)
            .map { random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("")
    }

    private fun play(gameModel: GameModel): String {
        val (player1, player1Choice, player2, player2Choice) = gameModel
        return when {
            player1Choice == player2Choice -> Options.DRAW
            player1Choice == Options.ROCK && player2Choice == Options.SCISSORS ||
                    player1Choice == Options.PAPER && player2Choice == Options.ROCK ||
                    player1Choice == Options.SCISSORS && player2Choice == Options.PAPER -> player1
            else -> player2
        }
    }

    fun setPlayer(gameId: String, player: Int, username: String) {
        repository.updateGame(gameId,player,username)
    }
}