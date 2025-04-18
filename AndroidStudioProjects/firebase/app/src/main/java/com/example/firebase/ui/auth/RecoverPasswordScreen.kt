package com.example.firebase.ui.auth

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecoverPasswordScreen(auth: FirebaseAuth, onNavigateBack: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var successMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Récupérer le mot de passe", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))

        if (errorMessage != null) {
            Text(
                text = errorMessage!!,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        if (successMessage != null) {
            Text(
                text = successMessage!!,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        if (isLoading) {
            CircularProgressIndicator()
        } else {
            Button(onClick = {
                if (email.isBlank()) {
                    errorMessage = "Veuillez entrer un email."
                    return@Button
                }
                isLoading = true
                errorMessage = null
                successMessage = null

                // Firebase password reset logic
                auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener { task ->
                        isLoading = false
                        if (task.isSuccessful) {
                            successMessage = "Un email de réinitialisation a été envoyé."
                            Log.d("RecoverPasswordScreen", "Email sent to $email")
                        } else {
                            errorMessage = "Erreur: ${task.exception?.localizedMessage}"
                            Log.e("RecoverPasswordScreen", "Failed to send email", task.exception)
                        }
                    }
            }) {
                Text("Envoyer un email de réinitialisation")
            }

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(onClick = onNavigateBack) {
                Text("Retour")
            }
        }
    }
}