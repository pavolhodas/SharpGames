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
    var currentRound by remember { mutableStateOf(1) }
    var bestTime by remember { mutableStateOf(Long.MAX_VALUE) }
    var currentColor by remember { mutableStateOf(Color.Green) }
    
    // List of possible colors
    val colors = listOf(
        Color.Green,
        Color.Red,
        Color.Blue,
        Color.Yellow,
        Color.Magenta,
        Color.Cyan
    )

    // Function to start a new round
    fun startNewRound() {
        gameState = GameState.WAITING
        circleVisible = false
        currentColor = colors.random()
    }

    // Handle waiting state and circle appearance
    LaunchedEffect(gameState) {
        when (gameState) {
            GameState.WAITING -> {
                delay(Random.nextLong(1000, 3000))
                if (gameState == GameState.WAITING) {
                    gameState = GameState.ACTIVE
                    circleVisible = true
                    startTime = System.currentTimeMillis()
                }
            }
            GameState.FINISHED -> {
                delay(1000)
                currentRound++
                startNewRound()
            }
            else -> {}
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
            // Game info
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Score: $score",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Round: $currentRound",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }

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
                                if (reactionTime < bestTime) {
                                    bestTime = reactionTime
                                }
                                gameState = GameState.FINISHED
                                circleVisible = false
                            }
                        }
                ) {
                    if (circleVisible) {
                        // Draw the target circle
                        drawCircle(
                            color = currentColor,
                            radius = 50f,
                            center = Offset(size.width / 2, size.height / 2)
                        )
                    }
                }
            }

            // Results display
            Column(
                modifier = Modifier.padding(top = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (gameState == GameState.FINISHED) {
                    Text(
                        text = "Reaction time: ${reactionTime}ms",
                        fontSize = 20.sp
                    )
                    Text(
                        text = "Best time: ${if (bestTime == Long.MAX_VALUE) "N/A" else "${bestTime}ms"}",
                        fontSize = 20.sp,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
    }
}

enum class GameState {
    WAITING,    // Waiting for the circle to appear
    ACTIVE,     // Circle is visible, player can click
    FINISHED    // Round is finished, showing results
} 
