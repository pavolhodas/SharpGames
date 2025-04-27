package fri.uniza.sk.sharpgames.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import fri.uniza.sk.sharpgames.ui.components.GameTopBar

@Composable
fun ReactionsScreen(navController: NavController) {
    Column(
        modifier = androidx.compose.ui.Modifier.fillMaxSize()
    ) {
        GameTopBar(
            title = "Reactions",
            navController = navController
        )
        
    }
} 
