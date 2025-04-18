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
import androidx.compose.runtime.remember 
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.fragment
import androidx.navigation.createGraph
import androidx.fragment.app.FragmentContainerView
import android.view.ViewGroup.LayoutParams.MATCH_PARENT

private const val URL_RTDB = "https://fir-appforproject-default-rtdb.europe-west1.firebasedatabase.app/"
@Composable
fun HomeScreen(user: FirebaseUser, onLogout: () -> Unit) {
    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Création programmatique du FragmentContainerView
        val navHostFragment = FragmentContainerView(this).apply {
            id = ViewID.navHost
            layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT)
        }
        setContentView(navHostFragment)

        // Création du NavController avec une navGraph 100 % Kotlin
        val navHost = NavHostFragment.create(R.navigation.nav_graph)
        supportFragmentManager.beginTransaction()
            .replace(ViewID.navHost, navHost)
            .setPrimaryNavigationFragment(navHost)
            .commitNow()

        navController = navHost.navController
        navController.graph = navController.createGraph(startDestination = "home") {
            fragment<`HomeFragment.kt`>("home")
            fragment<MinuteurFragment>("minuteur")
            fragment<CalculetteFragment>("calculette")
            //fragment<ChronometreFragment>("chrono")
        }
    }
    val userMessageRef: DatabaseReference = remember(user.uid) {
        FirebaseDatabase.getInstance(URL_RTDB) // Utilise la constante URL
            .getReference("userMessages")
            .child(user.uid)
            .child("message")
    }

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
        Spacer(modifier = Modifier.height(24.dp)) // Ajusté le Spacer
        RealtimeDatabaseSection(userMessageReference = userMessageRef)

        Spacer(modifier = Modifier.weight(1f)) // Pousse le bouton en bas

        // Bouton Déconnexion
        Button(onClick = onLogout) {
            Text("Se déconnecter")
        }
    }
}
object ViewID {
    const val navHost = 123456
}
