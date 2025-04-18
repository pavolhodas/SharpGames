package fri.uniza.sk.sharpgames.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import fri.uniza.sk.sharpgames.screens.MenuScreen
import fri.uniza.sk.sharpgames.screens.PhotographicMemoryScreen
import fri.uniza.sk.sharpgames.screens.LogicalThinkingScreen
import fri.uniza.sk.sharpgames.screens.ReactionsScreen
import fri.uniza.sk.sharpgames.screens.AbstractThinkingScreen

sealed class Screen(val route: String) {
    object Menu : Screen("menu")
    object PhotographicMemory : Screen("photographic_memory")
    object LogicalThinking : Screen("logical_thinking")
    object Reactions : Screen("reactions")
    object AbstractThinking : Screen("abstract_thinking")
}

@Composable
fun Navigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Menu.route) {
        composable(Screen.Menu.route) {
            MenuScreen(navController)
        }

        composable(Screen.PhotographicMemory.route) {
            PhotographicMemoryScreen(navController)
        }

        composable(Screen.LogicalThinking.route) {
            LogicalThinkingScreen(navController)
        }

        composable(Screen.Reactions.route) {
            ReactionsScreen(navController)
        }

        composable(Screen.AbstractThinking.route) {
            AbstractThinkingScreen(navController)
        }
    }
}
