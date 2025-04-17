package com.example.firebase.ui.main

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseUser

@Composable
fun HomeScreen(user: FirebaseUser, onLogout: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Connecté !",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Affiche email ou UID si email non dispo (ex: auth anonyme)
        Text(
            text = "Email: ${user.email ?: "Non disponible"}",
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = "UID: ${user.uid}",
            style = MaterialTheme.typography.bodySmall
        )

        Spacer(modifier = Modifier.weight(1f)) // Pousse le bouton en bas

        // Bouton Déconnexion
        Button(onClick = onLogout) {
            Text("Se déconnecter")
        }
    }
}