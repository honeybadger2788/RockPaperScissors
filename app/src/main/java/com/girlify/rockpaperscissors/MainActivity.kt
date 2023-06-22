package com.girlify.rockpaperscissors

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.girlify.rockpaperscissors.game.core.model.Routes
import com.girlify.rockpaperscissors.game.ui.home.HomeScreen
import com.girlify.rockpaperscissors.game.ui.multiPlayer.MultiPlayerScreen
import com.girlify.rockpaperscissors.game.ui.singlePlayer.SinglePlayerScreen
import com.girlify.rockpaperscissors.game.ui.splashScreen.SplashViewModel
import com.girlify.rockpaperscissors.ui.theme.RockPaperScissorsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val splashViewModel: SplashViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        splashScreen.setKeepOnScreenCondition{splashViewModel.isLoading.value}
        setContent {
            RockPaperScissorsTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MyNavigation()
                }
            }
        }
    }
}

@Composable
private fun MyNavigation(){
    val navigationController: NavHostController = rememberNavController()
    NavHost(
        navController = navigationController,
        startDestination = Routes.Home.route
    ) {
        composable(Routes.Home.route) {
            HomeScreen(
                {
                    navigationController.navigate(Routes.SinglePlayer.route)
                },
                {
                    navigationController.navigate(
                        Routes.MultiPlayer.createRoute(
                            username = it
                        )
                    )
                }
            )
        }

        composable(Routes.SinglePlayer.route) {
            SinglePlayerScreen()
        }

        composable(
            Routes.MultiPlayer.route,
            arguments = listOf(navArgument("username") {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            MultiPlayerScreen(
                backStackEntry.arguments?.getString("username") ?: "",
                navigationController::popBackStack
            )
        }
    }
}