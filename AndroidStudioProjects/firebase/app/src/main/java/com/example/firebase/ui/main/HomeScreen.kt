package com.example.firebase.ui.main

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

private const val URL_RTDB = "https://fir-appforproject-default-rtdb.europe-west1.firebasedatabase.app/"

@Composable
fun HomeScreen(user: FirebaseUser, onLogout: () -> Unit) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") { HomeMenuScreen(navController = navController) }
        composable("minuteur") { MinuteurScreen(navController = navController) }
        composable("calculette") { CalculetteScreen(navController) }
        composable("chrono") { ChronoScreen(navController = navController) }
        composable("bloc") { BlocNoteScreen(navController = navController) }
    }

}
