package com.girlify.rockpaperscissors

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.girlify.rockpaperscissors.game.core.model.Routes
import com.girlify.rockpaperscissors.game.ui.home.HomeScreen
import com.girlify.rockpaperscissors.game.ui.multiPlayer.MultiPlayerViewModel
import com.girlify.rockpaperscissors.game.ui.singlePlayer.GameScreen
import com.girlify.rockpaperscissors.game.ui.singlePlayer.SinglePlayerViewModel
import com.girlify.rockpaperscissors.game.ui.multiPlayer.VsPlayerGame
import com.girlify.rockpaperscissors.ui.theme.RockPaperScissorsTheme

class MainActivity : ComponentActivity() {
    private val vsPlayerViewModel: MultiPlayerViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RockPaperScissorsTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navigationController: NavHostController = rememberNavController()

                    NavHost(
                        navController = navigationController,
                        startDestination = Routes.Home.route
                    ) {
                        composable(Routes.Home.route) {
                            HomeScreen(
                                {
                                    navigationController.navigate(Routes.VersusComputer.route) {
                                        popUpToId
                                    }
                                },
                                {
                                    navigationController.navigate(Routes.VersusPlayer.route) {
                                        popUpToId
                                    }
                                }
                            )
                        }
                        composable(Routes.VersusComputer.route) {
                            GameScreen()
                        }
                        composable(Routes.VersusPlayer.route) {
                            VsPlayerGame(vsPlayerViewModel)
                        }
                    }
                }
            }
        }
    }
}