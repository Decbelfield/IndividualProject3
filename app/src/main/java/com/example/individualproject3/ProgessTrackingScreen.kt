package com.example.individualproject3

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

fun getWinCount(context: Context, gameName: String): Int {
    val sharedPreferences = context.getSharedPreferences("game_progress", Context.MODE_PRIVATE)
    return sharedPreferences.getInt(gameName, 0)
}
fun updateWinCount(context: Context, gameName: String, newCount: Int) {
    val sharedPreferences = context.getSharedPreferences("game_progress", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putInt(gameName, newCount)
    editor.apply()
}

@Composable
fun ProgressTrackingScreen(navController: NavController) {
    val context = LocalContext.current // Get the context
    var mazeEscapeWins by remember { mutableStateOf(getWinCount(context, "MazeEscape")) }
    var sequenceBuilderWins by remember {
        mutableStateOf(getWinCount(context, "SequenceBuilder"))
    }
}
