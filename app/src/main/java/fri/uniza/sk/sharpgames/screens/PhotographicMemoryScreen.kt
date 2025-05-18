package fri.uniza.sk.sharpgames.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import fri.uniza.sk.sharpgames.game.GameState
import fri.uniza.sk.sharpgames.ui.components.GameTopBar
import kotlinx.coroutines.delay
import fri.uniza.sk.sharpgames.ui.theme.RedPhotographicSc
import fri.uniza.sk.sharpgames.data.ScoreViewModel
import fri.uniza.sk.sharpgames.data.ScoreViewModelFactory

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
fun PhotographicMemoryScreen(
    navController: NavController,
    viewModel: ScoreViewModel = androidx.lifecycle.viewmodel.compose.viewModel(
        factory = ScoreViewModelFactory()
    )
) {
    // mutableStateOf- automaticke prekreslovanie pri zmene stavu
    // remember zabrani resetovaniu stavu pri rekompozicii(otocenie obrazovky atd..)
    var gameState by remember { mutableStateOf(GameState.INITIAL) }
    var score by remember { mutableStateOf(0) }
    var level by remember { mutableStateOf(1) }
    var currentHighlightedCell by remember { mutableStateOf<Int?>(null) }
    var pattern by remember { mutableStateOf(listOf<Int>()) }
    var playerSelection by remember { mutableStateOf(listOf<Int>()) }
    var gridSize by remember { mutableStateOf(3) }
    var currentPatternIndex by remember { mutableStateOf(0) }
    var startNextRound by remember { mutableStateOf(false)}
    var textMessage by remember {mutableStateOf("")}
    var highestScore by remember { mutableStateOf(0) }
    
    // Load highest score in korutine
    LaunchedEffect(Unit) {
      highestScore = viewModel.getHighestScore("Photographic Memory")
    }

// Handle showing pattern cells one by one
  LaunchedEffect(key1 = gameState, key2 = currentPatternIndex) {
    if (gameState == GameState.SHOWING_PATTERN) {
      if(currentPatternIndex == 0) {
        delay(500)
      }
      if (currentPatternIndex < pattern.size) {
        // Show the current cell
        currentHighlightedCell = pattern[currentPatternIndex]
        delay(1000) // Show each cell for 1 second
        currentHighlightedCell = null
        delay(300) // Short pause between cells
        currentPatternIndex++
      } else {
        // All cells have been shown
        delay(300) // Short pause before player can select
        gameState = GameState.PLAYER_TURN
        textMessage = "Repeat the sequence"
        currentPatternIndex = 0
      }
    }
  }

  LaunchedEffect(key1 = startNextRound) {
    if(startNextRound) {
        delay(1500)
        playerSelection = listOf()
        pattern = generatePattern(gridSize * gridSize, level)
        gameState = GameState.SHOWING_PATTERN
        textMessage = "Memorize the pattern!"
        startNextRound = false
        currentPatternIndex = 0
        currentHighlightedCell = null
    }
  }

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
                Text("Score: ${score}")
                Text("Top score: ${highestScore}")
                Text("Level: ${level}")
            }

            Text(
                text = textMessage,
                fontSize = 20.sp,
                color = if (textMessage.contains("Correct")) Color.Green else Color.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .alpha(1f)
            )

            // Stredná časť - tlačidlo alebo obsah hry
            if (gameState == GameState.INITIAL) {
                Button(
                    onClick = {
                        gameState = GameState.SHOWING_PATTERN
                        pattern = generatePattern(gridSize * gridSize, level)
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
            } else if (gameState == GameState.SHOWING_PATTERN) {
              MemoryGrid(
                gridSize = gridSize,
                selectedCells = listOf(),
                highlightedCell = currentHighlightedCell,
                onCellClick = {

                }
              )
            } else if(gameState == GameState.PLAYER_TURN) {
              MemoryGrid(
                gridSize = gridSize,
                selectedCells = playerSelection,
                highlightedCell = null,
                onCellClick = { index ->
                  if (playerSelection.size < pattern.size) {
                    playerSelection = playerSelection + index

                    // Check if player has completed their selection
                    if (playerSelection.size == pattern.size) {
                      // Compare the lists for equality
                      val isCorrect = playerSelection == pattern
                      //message = if (isCorrect) "Correct! Well done!" else "Incorrect. Try again!

                      if (isCorrect) {
                        score += 10
                        level++
                        if (level > 5) {
                          gridSize = 4
                        }
                      }
                      textMessage = if (isCorrect) "Correct! Well done!" else "Incorrect. Try again!"
                      startNextRound = true
                    }
                  }
                },
              )
            }

          // Spodná časť - tlačidlo pre navigáciu domov
          if (gameState != GameState.INITIAL) {
            Button(
                onClick = {
                    // Save score before navigating
                    if (score > 0) {
                        viewModel.addScore("Photographic Memory", score)
                    }
                    navController.navigate("menu") {
                        popUpTo("menu") { inclusive = true }
                    }
                },
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .size(width = 200.dp, height = 50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = RedPhotographicSc
                )
            ) {
                Text(
                    text = "Back to Menu",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
          } else {
            Spacer(modifier = Modifier.height(16.dp))
          }
        }
    }
  }

@Composable
fun MemoryGrid(
  gridSize: Int,
  selectedCells: List<Int>,
  highlightedCell: Int?,
  onCellClick: (Int) -> Unit,
) {
  LazyVerticalGrid(
    columns = GridCells.Fixed(gridSize),
    modifier = Modifier
      .fillMaxWidth()
      .aspectRatio(1f)
      .padding(8.dp)
  ) {
    items(gridSize * gridSize) { index ->
      Box(
        modifier = Modifier
          .aspectRatio(1f)
          .padding(4.dp)
          .background(
            when {
              highlightedCell == index -> RedPhotographicSc
              selectedCells.contains(index) -> RedPhotographicSc
              else -> Color.LightGray
            }
          )
          .border(1.dp, Color.DarkGray)
          .alpha(1f)
          .clickable { onCellClick(index) }
      )
    }
  }
}

fun generatePattern(size: Int, level: Int): List<Int> {
  val count = minOf(level + 2, size) // Increase pattern size with level, but don't exceed grid size
  val numList = List(size) { it }
  return numList.shuffled().take(count)
}
