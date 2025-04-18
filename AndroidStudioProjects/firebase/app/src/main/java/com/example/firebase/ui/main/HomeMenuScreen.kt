package com.example.firebase.ui.main

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavController

@Composable
fun HomeMenuScreen(navController: NavHostController, onLogout: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = { navController.navigate("minuteur") }) {
            Text("Chronomètre")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { navController.navigate("calculette") }) {
            Text("Calculette")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { navController.navigate("chrono") }) {
            Text("Minuteur")
        }
        /*
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.navigate("bloc") }) {
            Text("Bloc Notes")
        }
         */
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = { onLogout() },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
        ) {
            Text("Déconnexion")
        }
    }
}