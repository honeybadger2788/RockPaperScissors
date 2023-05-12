package com.girlify.rockpaperscissors.game.core.model

sealed class Routes(val route: String){
    object Home:Routes("home")
    object VersusComputer:Routes("pvc")
    object VersusPlayer:Routes("pvp")
}