package com.girlify.rockpaperscissors.game.ui.pvp

import com.girlify.rockpaperscissors.game.data.response.GameModel

sealed class UiState{
    object Loading: UiState()
    object Error: UiState()
    data class Success(val game: GameModel): UiState()
}