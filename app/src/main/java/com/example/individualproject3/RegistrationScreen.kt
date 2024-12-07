package com.example.individualproject3

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.individualproject3.ui.theme.IndividualProject3Theme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun RegistrationScreen(navController: NavController) {
    var name by remember { mutableStateOf(TextFieldValue("")) }
    var email by remember { mutableStateOf(TextFieldValue("")) }
    var password by remember { mutableStateOf(TextFieldValue("")) }
    var isParent by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("Invalid Input") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Register as Parent:")
            Checkbox(
                checked = isParent,
                onCheckedChange = { isParent = it }
            )
        }

        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            isError = name.text.length <= 1 && isParent
        )
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            isError = !isValidEmail(email.text)
        )
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            isError = !isValidPassword(password.text)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(onClick = {
                if (isValidName(name.text) && isValidEmail(email.text) && isValidPassword(password.text)) {
                    if (isParent) {
                        // Register as Parent
                        CoroutineScope(Dispatchers.IO).launch {
                            val result = Database.registerUser(
                                name.text,
                                email.text,
                                password.text.hashCode().toString(), // Replace with a secure hash function
                                isParent
                            )
                            if (result) {
                                navController.navigate("login")
                            } else {
                                errorMessage = "Registration failed. Please try again."
                                showErrorDialog = true
                            }
                        }
                    } else {
                        // Register as Adult (if applicable, similar logic for adult registration)
                        CoroutineScope(Dispatchers.IO).launch {
                            val result = Database.registerUser(
                                name.text,
                                email.text,
                                password.text.hashCode().toString(), // Replace with a secure hash function
                                isParent
                            )
                            if (result) {
                                navController.navigate("login")
                            } else {
                                errorMessage = "Registration failed. Please try again."
                                showErrorDialog = true
                            }
                        }
                    }
                } else {
                    errorMessage = "Invalid Input. Check all fields."
                    showErrorDialog = true
                }
            }) {
                Text("Register")
            }
            Button(onClick = {
                name = TextFieldValue("")
                email = TextFieldValue("")
                password = TextFieldValue("")
            }) {
                Text("Clear")
            }
            Button(onClick = { navController.popBackStack() }) {
                Text("Main Menu")
            }
        }

        if (showErrorDialog) {
            AlertDialog(
                onDismissRequest = { showErrorDialog = false },
                title = { Text("Error") },
                text = { Text(errorMessage) },
                confirmButton = { Button(onClick = { showErrorDialog = false }) { Text("OK") } }
            )
        }
    }
}

fun isValidPassword(password: String): Boolean {
    return password.length >= 8 && password.any { it.isDigit() } && password.any { it.isLetter() }
}

fun isValidName(name: String): Boolean {
    return name.length > 1
}

fun isValidEmail(email: String): Boolean {
    val emailRegex = "[^@]+@[^@]+\\.[^@]+".toRegex()
    return emailRegex.matches(email)
}
