package com.girlify.rockpaperscissors.game.core.model

sealed class Routes(val route: String){
    object Home:Routes("home")
    object SinglePlayer:Routes("single")
    object MultiPlayer:Routes("multi/{username}"){
        fun createRoute(username: String) = "multi/$username"
    }
}