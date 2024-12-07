package com.example.individualproject3

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


@Composable
fun GameScreen(navController: NavController) {
    val context = LocalContext.current

    var selectedGame by remember { mutableStateOf(0) }
    LaunchedEffect(Unit) {
        AudioManager.playBackgroundMusic(context)
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Educational Games")

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = { selectedGame = 1 }) {
            Text("Game 1: Maze Escape")
        }
        Spacer(modifier = Modifier.height(10.dp))
        Button(onClick = { selectedGame = 2 }) {
            Text("Game 2: Sequence Builder")
        }
        Spacer(modifier = Modifier.height(10.dp))
        Button(onClick = { selectedGame = 3 }) {
            Text("Game 3: Find the Error")
        }

        Spacer(modifier = Modifier.height(20.dp))

        when (selectedGame) {
            1 -> MazeEscapeGame(navController)
            2 -> SequenceBuilderGame(navController)
            3 -> FindTheErrorGame(navController)
        }
    }
}

@Composable
fun MazeEscapeGame(navController: NavController) {
    var robotPosition by remember { mutableStateOf(Position(0, 0)) }
    val targetPosition = Position(4, 4)
    val mazeSize = 6
    val moveStep = 1
    var blockerPath by remember { mutableStateOf("top") }
    var maze by remember { mutableStateOf(generateMaze(mazeSize, targetPosition, blockerPath)) }
    var winState by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Maze Escape")

        Spacer(modifier = Modifier.height(20.dp))

        Box(modifier = Modifier.size(300.dp)) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                for (x in 0 until mazeSize) {
                    for (y in 0 until mazeSize) {
                        val color = when {
                            Position(x, y) == robotPosition -> Color.Blue
                            Position(x, y) == targetPosition -> Color.Yellow
                            maze[x][y] == 1 -> Color.Red
                            else -> Color.Green
                        }

                        drawRect(
                            color = color,
                            size = androidx.compose.ui.geometry.Size(50f, 50f),
                            topLeft = Offset(x * 50f, y * 50f)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (robotPosition == targetPosition && !winState) {
            winState = true
        }
        if (winState) {
            Text(
                text = "You Win!",
                color = Color.Green,
                modifier = Modifier
                    .padding(16.dp)
            )

            Button(onClick = {
                robotPosition = Position(0, 0)
                winState = false

                blockerPath = when (blockerPath) {
                    "top" -> "bottom"
                    "bottom" -> "left"
                    "left" -> "right"
                    else -> "top"
                }
                maze = generateMaze(mazeSize, targetPosition, blockerPath)
            }) {
                Text("Next Level")
            }
        }
        Row(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(onClick = {
                if (!winState && robotPosition.y > 0 && maze[robotPosition.x][robotPosition.y - 1] == 0) {
                    robotPosition = robotPosition.copy(y = robotPosition.y - moveStep)
                }
            }) {
                Text("↑")
            }
        }

        Row(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(onClick = {
                if (!winState && robotPosition.x > 0 && maze[robotPosition.x - 1][robotPosition.y] == 0) {
                    robotPosition = robotPosition.copy(x = robotPosition.x - moveStep)
                }
            }) {
                Text("←")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(onClick = {
                if (!winState && robotPosition.x < mazeSize - 1 && maze[robotPosition.x + 1][robotPosition.y] == 0) {
                    robotPosition = robotPosition.copy(x = robotPosition.x + moveStep)
                }
            }) {
                Text("→")
            }
        }

        Row(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(onClick = {
                if (!winState && robotPosition.y < mazeSize - 1 && maze[robotPosition.x][robotPosition.y + 1] == 0) {
                    robotPosition = robotPosition.copy(y = robotPosition.y + moveStep)
                }
            }) {
                Text("↓")
            }
        }
    }
}
fun generateMaze(size: Int, targetPosition: Position, blockerPath: String): Array<IntArray> {
    val maze = Array(size) { IntArray(size) }

    for (i in 0 until size) {
        for (j in 0 until size) {
            if (i == 0 || j == 0 || i == size - 1 || j == size - 1) {
                maze[i][j] = 0
            } else {
                maze[i][j] = 1
            }
        }
    }

    maze[0][0] = 0
    maze[targetPosition.x][targetPosition.y] = 0

    when (blockerPath) {
        "top" -> {
            val topPathIndex = (1 until size - 1).random()
            maze[0][topPathIndex] = 1
        }
        "bottom" -> {
            val bottomPathIndex = (1 until size - 1).random()
            maze[size - 1][bottomPathIndex] = 1
        }
        "left" -> {
            val leftPathIndex = (1 until size - 1).random()
            maze[leftPathIndex][0] = 1
        }
        "right" -> {
            val rightPathIndex = (1 until size - 1).random()
            maze[rightPathIndex][size - 1] = 1
        }
    }

    return maze
}

data class Position(val x: Int, val y: Int)

@Composable
fun SequenceBuilderGame(navController: NavController) {
    var robotPosition by remember { mutableStateOf(MazePosition(0, 0)) }
    val targetPosition = MazePosition(4, 4)  // Target position
    val mazeSize = 6  // Maze size (6x6 grid)
    val moveStep = 1  // Step size for movement
    var sequence by remember { mutableStateOf(listOf<String>()) }
    val maze = remember { generateMaze(mazeSize, targetPosition) }  // Generate maze layout
    var gameMessage by remember { mutableStateOf("") }  // Outcome message
    var executingSequence by remember { mutableStateOf(false) }  // Animation flag
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Sequence Builder Game")

        Spacer(modifier = Modifier.height(16.dp))

        Box(modifier = Modifier.size(300.dp)) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                for (x in 0 until mazeSize) {
                    for (y in 0 until mazeSize) {
                        val color = when {
                            MazePosition(x, y) == robotPosition -> Color.Blue  // Robot position
                            MazePosition(x, y) == targetPosition -> Color.Yellow  // Target position
                            maze[x][y] == 1 -> Color.Red  // Wall
                            else -> Color.Green  // Path
                        }
                        drawRect(
                            color = color,
                            size = androidx.compose.ui.geometry.Size(size.width / mazeSize, size.height / mazeSize),
                            topLeft = androidx.compose.ui.geometry.Offset(
                                x * (size.width / mazeSize),
                                y * (size.height / mazeSize)
                            )
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text("Current Sequence: ${sequence.joinToString(", ")}")

        Spacer(modifier = Modifier.height(16.dp))
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row {
                Button(onClick = { sequence = sequence + "↑" }, enabled = !executingSequence) {
                    Text("Move Up")
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(onClick = { sequence = sequence + "↓" }, enabled = !executingSequence) {
                    Text("Move Down")
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                Button(onClick = { sequence = sequence + "←" }, enabled = !executingSequence) {
                    Text("Move Left")
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(onClick = { sequence = sequence + "→" }, enabled = !executingSequence) {
                    Text("Move Right")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Row {
            Button(
                onClick = {
                    coroutineScope.launch {
                        executingSequence = true
                        for (command in sequence) {
                            delay(500)
                            robotPosition = moveRobot(command, robotPosition, maze, mazeSize, moveStep)
                            if (robotPosition == targetPosition) {
                                gameMessage = "You Win! Reached the target."
                                executingSequence = false
                                return@launch
                            }
                        }
                        if (robotPosition != targetPosition) {
                            gameMessage = "Sequence complete! Try again."
                        }
                        executingSequence = false
                    }
                },
                enabled = !executingSequence
            ) {
                Text("Execute")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(onClick = {
                sequence = listOf()
                gameMessage = ""
                robotPosition = MazePosition(0, 0)
            }, enabled = !executingSequence) {
                Text("Reset")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        if (gameMessage.isNotEmpty()) {
            Text(gameMessage, color = if (gameMessage.startsWith("You Win")) Color.Green else Color.Red)
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.navigate("gameSelection") }) {
            Text("Back to Game Selection")
        }
    }
}

fun moveRobot(
    command: String,
    robotPosition: MazePosition,
    maze: Array<IntArray>,
    mazeSize: Int,
    moveStep: Int
): MazePosition {
    return when (command) {
        "↑" -> if (robotPosition.y > 0 && maze[robotPosition.x][robotPosition.y - 1] == 0) {
            robotPosition.copy(y = robotPosition.y - moveStep)
        } else robotPosition
        "↓" -> if (robotPosition.y < mazeSize - 1 && maze[robotPosition.x][robotPosition.y + 1] == 0) {
            robotPosition.copy(y = robotPosition.y + moveStep)
        } else robotPosition
        "←" -> if (robotPosition.x > 0 && maze[robotPosition.x - 1][robotPosition.y] == 0) {
            robotPosition.copy(x = robotPosition.x - moveStep)
        } else robotPosition
        "→" -> if (robotPosition.x < mazeSize - 1 && maze[robotPosition.x + 1][robotPosition.y] == 0) {
            robotPosition.copy(x = robotPosition.x + moveStep)
        } else robotPosition
        else -> robotPosition
    }
}

fun generateMaze(size: Int, targetPosition: MazePosition): Array<IntArray> {
    return Array(size) { IntArray(size) { 0 } }.apply {
        this[targetPosition.x][targetPosition.y] = 0
        this[2][2] = 1
        this[3][3] = 1
    }
}

data class MazePosition(val x: Int, val y: Int)

@Composable
fun FindTheErrorGame(navController: NavController) {
    val correctSequence = listOf("Move Up", "Move Down", "Move Left", "Move Up")
    var shuffledSequence by remember { mutableStateOf(correctSequence.shuffled()) }
    var gameMessage by remember { mutableStateOf("") }  // Outcome message
    var executingSequence by remember { mutableStateOf(false) }  // Animation flag

    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text("Correct Sequence: ${correctSequence.joinToString(", ")}")

        Spacer(modifier = Modifier.height(20.dp))
        LazyColumn {
            itemsIndexed(shuffledSequence) { index, item ->
                DraggableCommand(
                    command = item,
                    onDragEnd = { from, to ->
                        val newList = shuffledSequence.toMutableList()
                        newList[from] = shuffledSequence[to]
                        newList[to] = item
                        shuffledSequence = newList
                    },
                    index = index
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = {
                if (!executingSequence) {
                    executingSequence = true
                    if (shuffledSequence == correctSequence) {
                        gameMessage = "You Win! Sequence is correct."
                    } else {
                        gameMessage = "Try Again! The sequence is incorrect."
                    }
                    executingSequence = false
                }
            }
        ) {
            Text("Check Sequence")
        }

        Spacer(modifier = Modifier.height(20.dp))
        if (gameMessage.isNotEmpty()) {
            Text(gameMessage, color = if (gameMessage.startsWith("You Win")) Color.Green else Color.Red)
        }

        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = {
            shuffledSequence = correctSequence.shuffled()
            gameMessage = ""
        }) {
            Text("Reset")
        }

        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = { navController.navigate("gameSelection") }) {
            Text("Back to Game Selection")
        }
    }
}

@Composable
fun DraggableCommand(command: String, onDragEnd: (Int, Int) -> Unit, index: Int) {
    var isDragging by remember { mutableStateOf(false) }
    var offset by remember { mutableStateOf(Offset(0f, 0f)) }
    var initialOffset by remember { mutableStateOf(Offset(0f, 0f)) }
    val dragModifier = Modifier
        .padding(8.dp)
        .background(Color.Gray, shape = RoundedCornerShape(8.dp))
        .fillMaxWidth()
        .height(50.dp)
        .offset { IntOffset(offset.x.roundToInt(), offset.y.roundToInt()) }
        .draggable(
            state = rememberDraggableState { delta ->
                if (isDragging) {
                    offset = Offset(initialOffset.x + delta, initialOffset.y)
                }
            },
            orientation = Orientation.Vertical,
            onDragStarted = {
                isDragging = true
                initialOffset = offset
            },
            onDragStopped = {
                isDragging = false
                val newPosition = calculateNewPosition(offset)
                onDragEnd(index, newPosition)
            }
        )

    AnimatedVisibility(visible = true) {
        Box(modifier = dragModifier) {
            Text(
                text = command,
                modifier = Modifier.align(Alignment.Center),
                color = Color.White
            )
        }
    }
}

fun calculateNewPosition(offset: Offset): Int {
    return (offset.y / 100).toInt()
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
}
