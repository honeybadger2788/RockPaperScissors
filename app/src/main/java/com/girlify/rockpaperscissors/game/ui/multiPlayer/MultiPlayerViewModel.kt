package com.girlify.rockpaperscissors.game.ui.multiPlayer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.girlify.rockpaperscissors.game.core.model.Options
import com.girlify.rockpaperscissors.game.data.network.GameService
import com.girlify.rockpaperscissors.game.data.response.GameModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class MultiPlayerViewModel @Inject constructor(
    private val repository: GameService
) : ViewModel() {

    private val _gameData = MutableStateFlow(GameModel())
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

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private lateinit var username: String

    init {
        val gameId = generateRandomString()
        repository.setGame(gameId)
        _player.value = 1
        _gameId.value = gameId
    }

    fun startGameListener(gameId: String) {
        viewModelScope.launch {
            repository.gameListener(gameId).collect { gameModel ->
                if (gameModel != null) {
                    if (gameModel.player2.isNotEmpty()) {
                        _isEnable.value = true
                        _showCode.value = false
                        _gameData.value = gameModel
                        if (gameModel.player1Choice.isNotEmpty() && gameModel.player2Choice.isNotEmpty()) {
                            _showAnimation.value = false
                            _isEnable.value = false
                            _result.value = getResult(gameModel)
                        } else if (gameModel.player1Choice.isEmpty() && gameModel.player2Choice.isNotEmpty()) {
                            _message.value = "Esperando jugada de Jugador 1..."
                        } else if (gameModel.player2Choice.isEmpty() && gameModel.player1Choice.isNotEmpty()) {
                            _message.value = "Esperando jugada de Jugador 2..."
                        } else {
                            _message.value = ""
                            _result.value = ""
                        }
                    }
                }
            }
        }
    }

    fun onSendCode(gameId: String, code: String, player: Int, username: String) {
        viewModelScope.launch {
            val isValidCode = withContext(Dispatchers.IO) {
                repository.getGame(code)
            }
            if (isValidCode && gameId != code) {
                repository.deleteGame(gameId)
                _gameId.value = code
                _player.value = player
                _isEnable.value = true
                _showCode.value = false
                setPlayer(code, player, username)
            } else {
                _error.value = "Error en el código ingresado"
            }
        }
    }

    fun onCheckCode(code: String) {
        _code.value = code
        _isCodeButtonEnable.value = code.length == 6
    }

    fun onPlay(gameId: String, player: Int, playerElection: String) {
        viewModelScope.launch {
            _isEnable.value = false
            _showAnimation.value = true
            withContext(Dispatchers.IO) {
                repository.makeMove(gameId, player, playerElection)
            }
        }
    }

    fun onRestart(gameId: String) {
        viewModelScope.launch {
            _result.value = ""
            _isEnable.value = true
            withContext(Dispatchers.IO) {
                repository.restartGame(gameId)
            }
        }
    }

    private fun getResult(gameModel: GameModel): String {
        val (player1, player1Choice, player2, player2Choice) = gameModel
        val result =  when {
            player1Choice == player2Choice -> Options.DRAW_MESSAGE
            player1Choice == Options.ROCK && player2Choice == Options.SCISSORS ||
                    player1Choice == Options.PAPER && player2Choice == Options.ROCK ||
                    player1Choice == Options.SCISSORS && player2Choice == Options.PAPER -> player1
            else -> player2
        }
        return when(result){
            username -> Options.WIN_MESSAGE
            Options.DRAW_MESSAGE -> Options.DRAW_MESSAGE
            else -> Options.LOST_MESSAGE
        }
    }

    fun setPlayer(gameId: String, player: Int, username: String) {
        this.username = username
        viewModelScope.launch(Dispatchers.IO) {
            repository.setPlayer(gameId, player, username)
        }
    }
}

fun generateRandomString(length: Int = 6): String {
    val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
    val random = Random.Default
    return (1..length)
        .map { random.nextInt(0, charPool.size) }
        .map(charPool::get)
        .joinToString("")
}
