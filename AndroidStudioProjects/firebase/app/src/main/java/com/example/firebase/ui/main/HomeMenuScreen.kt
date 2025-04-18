package com.example.firebase.ui.main

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun HomeMenuScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { navController.navigate("minuteur") }) {
            Text("Minuteur")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.navigate("calculette") }) {
            Text("Calculette")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.navigate("chrono") }) {
            Text("Chronomètre")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.navigate("bloc") }) {
            Text("Bloc Notes")
        }
    }
}
