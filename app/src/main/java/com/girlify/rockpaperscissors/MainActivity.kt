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
import com.girlify.rockpaperscissors.game.ui.pvc.GameScreen
import com.girlify.rockpaperscissors.game.ui.pvc.GameViewModel
import com.girlify.rockpaperscissors.game.ui.pvp.VsPlayerGame
import com.girlify.rockpaperscissors.game.ui.pvp.VsPlayerViewModel
import com.girlify.rockpaperscissors.ui.theme.RockPaperScissorsTheme

class MainActivity : ComponentActivity() {
    private val gameViewModel: GameViewModel by viewModels()
    private val vsPlayerViewModel: VsPlayerViewModel by viewModels()
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
                            GameScreen(gameViewModel)
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