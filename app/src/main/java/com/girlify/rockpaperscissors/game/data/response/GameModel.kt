package com.girlify.rockpaperscissors.game.data.response

data class GameModel(
    val player1: String = "",
    val player1Choice: String = "",
    val player2: String = "",
    val player2Choice: String = "",
    val endGame: Boolean = false
)