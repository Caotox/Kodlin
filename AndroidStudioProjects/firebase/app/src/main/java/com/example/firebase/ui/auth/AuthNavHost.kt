package com.example.firebase.ui.auth

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.firebase.ui.main.HomeScreen
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AuthNavHost(
    navController: NavHostController,
    auth: FirebaseAuth
) {
    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(
                auth = auth,
                onAuthComplete = { user ->
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onNavigateToRegister = { navController.navigate("register") },
                onNavigateToRecoverPassword = { navController.navigate("recover_password") }
            )
        }
        composable("register") {
            RegisterScreen(
                auth = auth,
                onAuthComplete = { user ->
                    navController.navigate("home") {
                        popUpTo("register") { inclusive = true }
                    }
                },
                onNavigateToLogin = { navController.navigate("login") }
            )
        }
        composable("home") {
            val user = auth.currentUser
            if (user != null) {
                Log.d("AuthNavHost", "Navigating to HomeScreen with user: ${user.email}")
                HomeScreen(
                    user = user,
                    onLogout = {
                        auth.signOut()
                        // Navigate back to login only if not already there
                        if (navController.currentDestination?.route != "login") {
                            navController.navigate("login") {
                                popUpTo("home") { inclusive = true }
                            }
                        }
                    }
                )
            } else {
                Log.e("AuthNavHost", "User is null, navigating back to login.")
                // Navigate to login only if not already there
                if (navController.currentDestination?.route != "login") {
                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            }
        }
        composable("recover_password") {
            RecoverPasswordScreen(
                auth = auth,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}