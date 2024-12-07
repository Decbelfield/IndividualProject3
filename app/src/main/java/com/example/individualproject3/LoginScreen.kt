package com.example.individualproject3

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.example.individualproject3.R
import com.example.individualproject3.Database
import com.example.individualproject3.ui.theme.IndividualProject3Theme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// Flag to temporarily bypass login verification
var bypassLoginVerification = true

@Composable
fun LoginScreen(navController: NavController, modifier: Modifier = Modifier) {
    var email by remember { mutableStateOf(TextFieldValue("")) }
    var password by remember { mutableStateOf(TextFieldValue("")) }
    var showErrorDialog by remember { mutableStateOf(false) }
    val errorMessage = "Invalid Input."

    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") }
        )
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
        )

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically) {
            Button(onClick = {
                // Bypass verification for now
                if (bypassLoginVerification) {
                    navController.navigate("GameScreen")
                } else {
                    // Comment out the following lines to disable bypassing
                    loginParent(email.text, password.text) { success ->
                        if (success) {
                            navController.navigate("GameScreen")
                        } else {
                            showErrorDialog = true
                        }
                    }
                }
            }) {
                Text("Login")
            }
        }

        if (showErrorDialog) {
            AlertDialog(
                onDismissRequest = { showErrorDialog = false },
                title = { Text("Login Failed") },
                text = { Text(errorMessage) },
                confirmButton = { Button(onClick = { showErrorDialog = false }) { Text("OK") } }
            )
        }
    }
}

fun loginParent(email: String, password: String, callback: (Boolean) -> Unit) {
    CoroutineScope(Dispatchers.IO).launch {
        val success = true
        callback(success)
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview(navController: NavController = NavController(LocalContext.current)) {
    IndividualProject3Theme {
        LoginScreen(navController)
    }
}