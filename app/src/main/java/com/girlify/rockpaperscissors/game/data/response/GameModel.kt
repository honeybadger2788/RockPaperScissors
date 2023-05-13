package com.girlify.rockpaperscissors.game.data.response

data class GameModel(
    val id: String,
    val playerMove: String,
    val opponentMove: String,
    val result: String
)