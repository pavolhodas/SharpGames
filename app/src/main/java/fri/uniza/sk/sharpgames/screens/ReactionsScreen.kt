package fri.uniza.sk.sharpgames.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import fri.uniza.sk.sharpgames.ui.components.GameTopBar
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun ReactionsScreen(navController: NavController) {
    var gameState by remember { mutableStateOf(GameState.WAITING) }
    var score by remember { mutableStateOf(0) }
    var reactionTime by remember { mutableStateOf(0L) }
    var startTime by remember { mutableStateOf(0L) }
    var circleVisible by remember { mutableStateOf(false) }
    var shouldStartNewRound by remember { mutableStateOf(false) }

    // Function to start a new round
    fun startNewRound() {
        gameState = GameState.WAITING
        circleVisible = false
    }

    // Handle waiting state and circle appearance
    LaunchedEffect(gameState) {
        if (gameState == GameState.WAITING) {
            delay(Random.nextLong(1000, 3000))
            if (gameState == GameState.WAITING) {
                gameState = GameState.ACTIVE
                circleVisible = true
                startTime = System.currentTimeMillis()
            }
        }
    }

    // Handle new round after delay
    LaunchedEffect(shouldStartNewRound) {
        if (shouldStartNewRound) {
            delay(1000)
            startNewRound()
            shouldStartNewRound = false
        }
    }

    // Start the first round when the screen is created
    LaunchedEffect(Unit) {
        startNewRound()
    }

    Scaffold(
        topBar = {
            GameTopBar(
                title = "Reactions",
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
            // Score display
            Text(
                text = "Score: $score",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Game area
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(Color.LightGray)
                    .padding(16.dp)
            ) {
                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable {
                            if (gameState == GameState.ACTIVE && circleVisible) {
                                reactionTime = System.currentTimeMillis() - startTime
                                score += (1000 - reactionTime).coerceAtLeast(0).toInt()
                                gameState = GameState.FINISHED
                                circleVisible = false
                                shouldStartNewRound = true
                            }
                        }
                ) {
                    if (circleVisible) {
                        // Draw the target circle
                        drawCircle(
                            color = Color.Green,
                            radius = 50f,
                            center = Offset(size.width / 2, size.height / 2)
                        )
                    }
                }
            }

            // Reaction time display
            if (gameState == GameState.FINISHED) {
                Text(
                    text = "Reaction time: ${reactionTime}ms",
                    fontSize = 20.sp,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }
    }
}

enum class GameState {
    WAITING,    // Waiting for the circle to appear
    ACTIVE,     // Circle is visible, player can click
    FINISHED    // Round is finished, showing results
} 
