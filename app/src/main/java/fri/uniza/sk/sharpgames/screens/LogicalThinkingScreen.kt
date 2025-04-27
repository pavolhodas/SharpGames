package fri.uniza.sk.sharpgames.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import fri.uniza.sk.sharpgames.ui.components.GameTopBar
import kotlinx.coroutines.delay

data class Point(val x: Int, val y: Int)
data class Flow(val color: Color, val path: Path, val points: List<Point>)

@Composable
fun LogicalThinkingScreen(navController: NavController) {
    var flows by remember { mutableStateOf(listOf<Flow>()) }
    var currentFlow by remember { mutableStateOf<Flow?>(null) }
    var startPoint by remember { mutableStateOf<Point?>(null) }
    var gameWon by remember { mutableStateOf(false) }
    
    // Function to generate new points
    fun generateNewPoints(): List<Pair<Point, Color>> {
        val colors = listOf(Color.Red, Color.Blue, Color.Green, Color.Yellow, Color.Magenta)
        val points = mutableListOf<Pair<Point, Color>>()
        val grid = Array(5) { Array(5) { false } } // false means cell is empty
        
        // Helper function to find all possible paths between two points
        fun findPossiblePaths(start: Point, end: Point): List<List<Point>> {
            val paths = mutableListOf<List<Point>>()
            
            // Direct path (horizontal or vertical)
            if (start.x == end.x || start.y == end.y) {
                val path = mutableListOf<Point>()
                if (start.x == end.x) {
                    for (y in minOf(start.y, end.y)..maxOf(start.y, end.y)) {
                        path.add(Point(start.x, y))
                    }
                } else {
                    for (x in minOf(start.x, end.x)..maxOf(start.x, end.x)) {
                        path.add(Point(x, start.y))
                    }
                }
                paths.add(path)
            }
            
            // Path with one turn
            val turnPoints = listOf(
                Point(start.x, end.y),
                Point(end.x, start.y)
            )
            
            turnPoints.forEach { turn ->
                val path = mutableListOf<Point>()
                // First segment
                if (start.x == turn.x) {
                    for (y in minOf(start.y, turn.y)..maxOf(start.y, turn.y)) {
                        path.add(Point(start.x, y))
                    }
                } else {
                    for (x in minOf(start.x, turn.x)..maxOf(start.x, turn.x)) {
                        path.add(Point(x, start.y))
                    }
                }
                // Second segment
                if (turn.x == end.x) {
                    for (y in minOf(turn.y, end.y)..maxOf(turn.y, end.y)) {
                        path.add(Point(turn.x, y))
                    }
                } else {
                    for (x in minOf(turn.x, end.x)..maxOf(turn.x, end.x)) {
                        path.add(Point(x, turn.y))
                    }
                }
                paths.add(path)
            }
            
            return paths
        }
        
        // Helper function to check if path is valid (doesn't cross existing paths)
        fun isPathValid(path: List<Point>): Boolean {
            return path.all { point ->
                !grid[point.y][point.x]
            }
        }
        
        // Helper function to mark path as used
        fun markPath(path: List<Point>) {
            path.forEach { point ->
                grid[point.y][point.x] = true
            }
        }
        
        // Generate valid paths and points for each color
        colors.forEach { color ->
            var attempts = 0
            var validPathFound = false
            
            while (!validPathFound && attempts < 100) {
                attempts++
                
                // Try to find two random points that can be connected
                val availablePoints = mutableListOf<Point>()
                for (x in 0 until 5) {
                    for (y in 0 until 5) {
                        if (!grid[y][x]) {
                            availablePoints.add(Point(x, y))
                        }
                    }
                }
                
                if (availablePoints.size >= 2) {
                    availablePoints.shuffle()
                    
                    // Try to find a valid path between two points
                    for (i in availablePoints.indices) {
                        for (j in i + 1 until availablePoints.size) {
                            val start = availablePoints[i]
                            val end = availablePoints[j]
                            
                            // Find all possible paths
                            val possiblePaths = findPossiblePaths(start, end)
                            
                            // Try each path
                            for (path in possiblePaths) {
                                if (isPathValid(path)) {
                                    // Found a valid path
                                    points.add(start to color)
                                    points.add(end to color)
                                    markPath(path)
                                    validPathFound = true
                                    break
                                }
                            }
                            if (validPathFound) break
                        }
                        if (validPathFound) break
                    }
                }
            }
        }
        
        points.shuffle() // Shuffle final points to randomize the order
        return points
    }
    
    // State for points that can be regenerated
    var coloredPoints by remember { mutableStateOf(generateNewPoints()) }
    
    Scaffold(
        topBar = {
            GameTopBar(
                title = "Logical Thinking",
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
            Text(
                text = if (gameWon) "Congratulations! You won!" else "Flow Free",
                fontSize = 24.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Canvas(
                modifier = Modifier
                    .size(300.dp)
                    .background(Color.White)
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDragStart = { offset ->
                                val gridX = (offset.x / (size.width / 5f)).toInt()
                                val gridY = (offset.y / (size.height / 5f)).toInt()
                                
                                // Check if we're within the game field
                                if (gridX in 0..4 && gridY in 0..4) {
                                    val point = Point(gridX, gridY)
                                    
                                    // Find if we're starting from a colored point
                                    coloredPoints.find { it.first == point }?.let { (start, color) ->
                                        // Check if this color already has a completed flow
                                        if (flows.none { it.color == color }) {
                                            startPoint = start
                                            currentFlow = Flow(color, Path().apply {
                                                moveTo(
                                                    (start.x * size.width / 5f) + size.width / 10f,
                                                    (start.y * size.height / 5f) + size.height / 10f
                                                )
                                            }, listOf(start))
                                        }
                                    }
                                }
                            },
                            onDrag = { change, _ ->
                                val gridX = (change.position.x / (size.width / 5f)).toInt()
                                val gridY = (change.position.y / (size.height / 5f)).toInt()
                                
                                // Check if we're within the game field
                                if (gridX in 0..4 && gridY in 0..4) {
                                    val point = Point(gridX, gridY)

                                    // Check if flow isn't null and if we aren't staying on the same place which was already added
                                    if (currentFlow != null && point != currentFlow!!.points.last()) {
                                        // Check if we're crossing any existing flow
                                        val isCrossing = flows.any { flow ->
                                            flow.points.any { it == point }
                                        }
                                        
                                        if (!isCrossing) {
                                            // Check if we're backtracking along our own path
                                            val existingPointIndex = currentFlow!!.points.indexOf(point)
                                            if (existingPointIndex != -1) {
                                                // We're backtracking - erase everything after this point
                                                val newPath = Path().apply {
                                                    // Take first n+1 points
                                                    val points = currentFlow!!.points.take(existingPointIndex + 1)
                                                    if (points.isNotEmpty()) {
                                                        moveTo(
                                                            (points.first().x * size.width / 5f) + size.width / 10f,
                                                            (points.first().y * size.height / 5f) + size.height / 10f
                                                        )
                                                        // Write line to each point
                                                        points.drop(1).forEach { p ->
                                                            lineTo(
                                                                (p.x * size.width / 5f) + size.width / 10f,
                                                                (p.y * size.height / 5f) + size.height / 10f
                                                            )
                                                        }
                                                    }
                                                }
                                                currentFlow = currentFlow!!.copy(
                                                    path = newPath,
                                                    points = currentFlow!!.points.take(existingPointIndex + 1)
                                                )
                                            } else {
                                                // Normal path extension
                                                currentFlow = currentFlow!!.copy(
                                                    path = currentFlow!!.path.apply {
                                                        lineTo(
                                                            (point.x * size.width / 5f) + size.width / 10f,
                                                            (point.y * size.height / 5f) + size.height / 10f
                                                        )
                                                    },
                                                    points = currentFlow!!.points + point
                                                )
                                            }
                                        }
                                    }
                                }
                            },
                            onDragEnd = {
                                if (currentFlow != null) {
                                    // Check if we ended at a matching colored point
                                    val endPoint = currentFlow!!.points.last()
                                    coloredPoints.find { it.first == endPoint && it.second == currentFlow!!.color }?.let {
                                        // Valid flow completed
                                        flows = flows + currentFlow!!
                                        
                                        // Check if game is won
                                        if (flows.size == coloredPoints.size / 2) {
                                            gameWon = true
                                        }
                                    }
                                }
                                currentFlow = null
                                startPoint = null
                            }
                        )
                    }
            ) {
                // Draw grid
                for (i in 0..5) {
                    drawLine(
                        color = Color.Gray,
                        start = Offset(i * size.width / 5f, 0f),
                        end = Offset(i * size.width / 5f, size.height),
                        strokeWidth = 3f
                    )
                    drawLine(
                        color = Color.Gray,
                        start = Offset(0f, i * size.height / 5f),
                        end = Offset(size.width, i * size.height / 5f),
                        strokeWidth = 3f
                    )
                }

                // Draw colored points
                coloredPoints.forEach { (point, color) ->
                    drawCircle(
                        color = color,
                        radius = 20f,
                        center = Offset(
                            (point.x * size.width / 5f) + size.width / 10f,
                            (point.y * size.height / 5f) + size.height / 10f
                        )
                    )
                }

                // Draw completed flows
                flows.forEach { flow ->
                    drawPath(
                        path = flow.path,
                        color = flow.color,
                        style = Stroke(width = 25f)
                    )
                }

                // Draw current flow
                currentFlow?.let { flow ->
                    drawPath(
                        path = flow.path,
                        color = flow.color,
                        style = Stroke(width = 25f)
                    )
                }
            }

            Button(
                onClick = {
                  navController.navigate("menu") {
                    popUpTo("menu") { inclusive = true }
                  }
                },
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .size(width = 200.dp, height = 50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFd4d36a)
                )
            ) {
                Text(
                    text = "Go back Home",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }

  LaunchedEffect(key1 = gameWon) {
    if(gameWon) {
      delay(1500)
      flows = listOf()
      currentFlow = null
      startPoint = null
      gameWon = false
      coloredPoints = generateNewPoints()
    }
  }
}
