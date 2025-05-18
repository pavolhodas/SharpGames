package fri.uniza.sk.sharpgames.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import fri.uniza.sk.sharpgames.ui.components.GameTopBar
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun ReactionsScreen(navController: NavController) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    
    // Calculate dynamic sizes based on screen width
    val circleSize = (screenWidth * 0.2f).coerceAtMost(80f)
    val fontSize = (screenWidth * 0.05f).coerceAtMost(24f)
    val smallFontSize = (screenWidth * 0.04f).coerceAtMost(18f)
    
    var gameState by remember { mutableStateOf(GameState.WAITING) }
    var score by remember { mutableStateOf(0) }
    var reactionTime by remember { mutableStateOf(0L) }
    var startTime by remember { mutableStateOf(0L) }
    var circleVisible by remember { mutableStateOf(false) }
    var currentRound by remember { mutableStateOf(1) }
    var bestTime by remember { mutableStateOf(Long.MAX_VALUE) }
    var currentColor by remember { mutableStateOf(Color.Green) }
    var circlePosition by remember { mutableStateOf(Offset(0f, 0f)) }
    
    val colors = listOf(
        Color.Green,
        Color.Red,
        Color.Blue,
        Color.Yellow,
        Color.Magenta,
        Color.Cyan
    )

    fun generateRandomPosition(boxWidth: Float, boxHeight: Float): Offset {
        val padding = circleSize / 2
        val x = Random.nextFloat() * (boxWidth - circleSize) + padding
        val y = Random.nextFloat() * (boxHeight - circleSize) + padding
        return Offset(x, y)
    }

    fun startNewRound() {
        gameState = GameState.WAITING
        circleVisible = false
        currentColor = colors.random()
    }

    LaunchedEffect(gameState) {
        when (gameState) {
            GameState.WAITING -> {
                delay(Random.nextLong(1000, 3000))
                if (gameState == GameState.WAITING) {
                    gameState = GameState.ACTIVE
                    circleVisible = true
                    startTime = System.currentTimeMillis()
                    // Use fixed dimensions for now, we'll update this when we have the actual box size
                    circlePosition = generateRandomPosition(300f, 300f)
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
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Game info
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Score: $score",
                    fontSize = fontSize.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "Round: $currentRound",
                    fontSize = fontSize.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.End,
                    modifier = Modifier.weight(1f)
                )
            }

            // Game area
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .background(Color.LightGray)
                    .padding(8.dp)
            ) {
                if (circleVisible) {
                    Button(
                        onClick = {
                            if (gameState == GameState.ACTIVE) {
                                reactionTime = System.currentTimeMillis() - startTime
                                score += (1000 - reactionTime).coerceAtLeast(0).toInt()
                                if (reactionTime < bestTime) {
                                    bestTime = reactionTime
                                }
                                gameState = GameState.FINISHED
                                circleVisible = false
                            }
                        },
                        modifier = Modifier
                            .size(circleSize.dp)
                            .offset(
                                x = circlePosition.x.dp,
                                y = circlePosition.y.dp
                            ),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = currentColor
                        ),
                        shape = CircleShape
                    ) {}
                }
            }

            // Results display
            Column(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFf5f5f5)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = if (gameState == GameState.ACTIVE) "Current Reaction Time" else "Your Reaction Time",
                            fontSize = smallFontSize.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF5c9464)
                        )
                        Text(
                            text = if (gameState == GameState.ACTIVE) "Waiting..." else "${reactionTime}ms",
                            fontSize = fontSize.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                        if (gameState == GameState.FINISHED && reactionTime < bestTime) {
                            Text(
                                text = "New Best Time! 🎉",
                                fontSize = smallFontSize.sp,
                                color = Color(0xFF4CAF50),
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFf5f5f5)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Best Time",
                            fontSize = smallFontSize.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF5c9464)
                        )
                        Text(
                            text = if (bestTime == Long.MAX_VALUE) "N/A" else "${bestTime}ms",
                            fontSize = fontSize.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                }
            }

            // Home button
            Button(
                onClick = { 
                    navController.navigate("menu") {
                        popUpTo("menu") { inclusive = true }
                    }
                },
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .size(width = (screenWidth * 0.5f).coerceAtMost(200f).dp, height = 50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF5c9464)
                )
            ) {
                Text(
                    text = "Back to Menu",
                    fontSize = smallFontSize.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
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
