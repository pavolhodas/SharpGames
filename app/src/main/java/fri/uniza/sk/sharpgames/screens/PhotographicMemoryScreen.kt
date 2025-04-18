package fri.uniza.sk.sharpgames.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import fri.uniza.sk.sharpgames.ui.components.GameTopBar

@Composable
fun PhotographicMemoryScreen(navController: NavController) {
    Column(
        modifier = androidx.compose.ui.Modifier.fillMaxSize()
    ) {
        GameTopBar(
            title = "Photographic Memory",
            navController = navController
        )
        
        // Tu bude obsah hry
    }
}
