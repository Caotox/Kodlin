package com.example.firebase.ui.auth

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseUser // Import added
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    auth: FirebaseAuth,
    onAuthComplete: (FirebaseUser) -> Unit,
    onNavigateToRegister: () -> Unit,
    onNavigateToRecoverPassword: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Connexion", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Mot de passe") },
            visualTransformation = PasswordVisualTransformation(),
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

        if (isLoading) {
            CircularProgressIndicator()
        } else {
            Button(onClick = {
                if (email.isBlank() || password.isBlank()) {
                    errorMessage = "Email et mot de passe requis."
                    return@Button
                }
                isLoading = true
                errorMessage = null
                coroutineScope.launch {
                    try {
                        val result = auth.signInWithEmailAndPassword(email, password).await()
                        Log.d("LoginScreen", "Login Success: ${result.user?.email}")
                        onAuthComplete(result.user!!)
                    } catch (e: FirebaseAuthInvalidCredentialsException) {
                        Log.w("LoginScreen", "Login Failed: Invalid Credentials", e)
                        errorMessage = "Email ou mot de passe incorrect."
                    } catch (e: Exception) {
                        Log.w("LoginScreen", "Login Failed", e)
                        errorMessage = "Échec connexion: ${e.localizedMessage}"
                    } finally {
                        isLoading = false
                    }
                }
            }) { Text("Connexion") }

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(onClick = onNavigateToRegister) {
                Text("Créer un compte")
            }

            Button(
                onClick = { onNavigateToRecoverPassword() },
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text("Mot de passe oublié ?")
            }
        }
    }
}