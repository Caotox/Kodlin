package com.example.firebase.navigation

import android.util.Log
import androidx.compose.runtime.*
import androidx.navigation.compose.rememberNavController
import com.example.firebase.ui.auth.AuthNavHost
import com.example.firebase.ui.main.HomeScreen
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AuthNavigator() {
    val auth = FirebaseAuth.getInstance()

    var currentUser by remember { mutableStateOf(auth.currentUser) }

    // Effect to listen for authentication state changes
    DisposableEffect(auth) {
        val authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            currentUser = firebaseAuth.currentUser
            Log.d("AuthNavigator", "Auth state changed, user: ${currentUser?.uid}")
        }

        auth.addAuthStateListener(authStateListener)

        onDispose {
            auth.removeAuthStateListener(authStateListener)
        }
    }

    // Display the appropriate screen based on the authentication state
    if (currentUser == null) {
        // User not logged in -> Show authentication screens
        val navController = rememberNavController()
        AuthNavHost(
            navController = navController,
            auth = auth
        )
    } else {
        // User logged in -> Show the home screen
        HomeScreen(
            user = currentUser!!,
            onLogout = {
                Log.d("AuthNavigator", "Logout requested")
                auth.signOut()
            }
        )
    }
}