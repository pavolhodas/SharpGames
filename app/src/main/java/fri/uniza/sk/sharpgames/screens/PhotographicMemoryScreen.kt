package fri.uniza.sk.sharpgames.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import fri.uniza.sk.sharpgames.ui.components.GameTopBar

// Stav hry
data class PhotographicMemoryState(
    val score: Int = 0,
    val level: Int = 1,
    val isShowingPattern: Boolean = false,
    val isGameActive: Boolean = false,
    val pattern: List<Int> = emptyList(),  // indexy tlačidiel v poradí
    val playerSequence: List<Int> = emptyList()  // sekvencia, ktorú hráč stlačil
)

@Composable
fun PhotographicMemoryScreen(navController: NavController) {
    // mutableStateOf- automaticke prekreslovanie pri zmene stavu
    // remember zabrani resetovaniu stavu pri rekompozicii(otocenie obrazovky atd..)
    var gameState by remember { mutableStateOf(PhotographicMemoryState()) }

    Scaffold(
        topBar = {
            GameTopBar(
                title = "Photographic Memory",
                navController = navController
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Horná časť - skóre a level
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Score: ${gameState.score}")
                Text("Level: ${gameState.level}")
            }

            // Stredná časť - tlačidlo alebo obsah hry
            if (!gameState.isGameActive) {
                Button(
                    onClick = { 
                        gameState = gameState.copy(
                            isGameActive = true,
                            isShowingPattern = true
                        )
                    },
                    modifier = Modifier
                        .size(width = 200.dp, height = 80.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(
                        text = "Start Game",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            } else {
                Text(
                    text = if (gameState.isShowingPattern) "Memorize the pattern!" else "Repeat the pattern!",
                    modifier = Modifier.padding(vertical = 16.dp),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium
                )

                // Grid tlačidiel 3x3
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    for (row in 0..2) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            for (col in 0..2) {
                                val index = row * 3 + col
                                Button(
                                    onClick = { /* Tu bude logika pre stlačenie tlačidla */ },
                                    modifier = Modifier
                                        .weight(1f)
                                        .aspectRatio(1f),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.secondary
                                    )
                                ) {
                                    // Prázdny obsah tlačidla
                                }
                            }
                        }
                    }
                }
            }

            // Spodná časť - prázdny priestor pre budúci obsah
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
