package com.girlify.rockpaperscissors.game.ui.multiPlayer

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.girlify.rockpaperscissors.game.core.model.Options
import com.girlify.rockpaperscissors.game.data.network.GameService
import com.girlify.rockpaperscissors.game.data.response.GameModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
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

    private val _isCodeButtonEnable = MutableLiveData<Boolean>()
    val isCodeButtonEnable: LiveData<Boolean> = _isCodeButtonEnable

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private lateinit var username: String

    private val inactivityTimeout = 60000L // 60 segundos de inactividad
    private val inactivityHandler = Handler(Looper.getMainLooper())
    private val inactivityRunnable: Runnable = Runnable {
        stopGame()
    }

    init {
        val gameId = generateRandomString()
        repository.setGame(gameId)
        _player.value = 1
        _gameId.value = gameId
        startHandler()
    }

    private fun startHandler(){
        inactivityHandler.postDelayed(inactivityRunnable, inactivityTimeout)
    }

    private fun stopHandler(){
        inactivityHandler.removeCallbacks(inactivityRunnable)
    }

    fun startGameListener(gameId: String) {
        viewModelScope.launch {
            repository.gameListener(gameId).collect { gameModel ->
                if (gameModel != null) {
                    if (!gameModel.endGame) {
                        stopHandler()
                        if (gameModel.player2.isNotEmpty()) {
                            _showCode.value = false
                            _gameData.value = gameModel
                            if (gameModel.player1Choice.isNotEmpty() && gameModel.player2Choice.isNotEmpty()) {
                                _showAnimation.value = false
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
                        startHandler()
                    } else {
                        if (gameModel.player2.isEmpty() || gameModel.player1Choice.isEmpty() || gameModel.player2Choice.isEmpty()) {
                            repository.deleteGame(gameId)
                        }
                        resetStates()
                        stopHandler()
                    }
                }
            }
        }
    }

    private fun stopGame() {
        viewModelScope.launch {
            _gameId.value?.let { repository.endGame(it) }
        }
    }

    private fun resetStates() {
        _result.value = ""
        _gameId.value = ""
        _showAnimation.value = false
        _gameData.value = GameModel()
        _player.value = 0
        _error.value = ""
        _message.value = ""
        _code.value = ""
        _showCode.value = true
        _isCodeButtonEnable.value = false
    }

    fun onSendCode(gameId: String, code: String, player: Int, username: String) {
        viewModelScope.launch {
            val isValidCode = repository.getGame(code)
            if (isValidCode && gameId != code) {
                repository.deleteGame(gameId)
                _gameId.value = code
                _player.value = player
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
            _showAnimation.value = true
            repository.makeMove(gameId, player, playerElection)
        }
    }

    fun onRestart(gameId: String) {
        viewModelScope.launch {
            _result.value = ""
            repository.restartGame(gameId)
        }
    }

    fun onEndGame(gameId: String) {
        viewModelScope.launch {
            repository.endGame(gameId)
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
        viewModelScope.launch {
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