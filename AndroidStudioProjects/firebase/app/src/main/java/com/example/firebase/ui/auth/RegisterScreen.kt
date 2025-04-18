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
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    auth: FirebaseAuth,
    onAuthComplete: (FirebaseUser) -> Unit,
    onNavigateToLogin: () -> Unit
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
        Text("Inscription", style = MaterialTheme.typography.headlineMedium)
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
                        val result = auth.createUserWithEmailAndPassword(email, password).await()
                        Log.d("RegisterScreen", "Registration Success: ${result.user?.email}")
                        onAuthComplete(result.user!!)
                    } catch (e: FirebaseAuthUserCollisionException) {
                        Log.w("RegisterScreen", "Registration Failed: Email already in use.", e)
                        errorMessage = "Cet email est déjà utilisé."
                    } catch (e: FirebaseAuthWeakPasswordException) {
                        Log.w("RegisterScreen", "Registration Failed: Weak password.", e)
                        errorMessage = "Mot de passe trop faible (6 caractères min)."
                    } catch (e: Exception) {
                        Log.w("RegisterScreen", "Registration Failed", e)
                        errorMessage = "Échec inscription: ${e.localizedMessage}"
                    } finally {
                        isLoading = false
                    }
                }
            }) { Text("S'inscrire") }

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(onClick = onNavigateToLogin) {
                Text("Déjà un compte ? Connexion")
            }
        }
    }
}