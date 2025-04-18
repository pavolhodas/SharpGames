package fri.uniza.sk.sharpgames.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import fri.uniza.sk.sharpgames.screens.MenuScreen
import fri.uniza.sk.sharpgames.screens.PhotographicMemoryScreen

sealed class Screen(val route: String) {
    object Menu : Screen("menu")
    object PhotographicMemory : Screen("photographic_memory")
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
    }
}
