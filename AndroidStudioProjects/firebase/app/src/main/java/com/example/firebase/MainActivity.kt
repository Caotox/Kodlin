package com.example.firebase

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.firebase.navigation.AuthNavigator
import com.example.firebase.ui.theme.FirebaseTheme
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.fragment
import androidx.navigation.navOptions
import androidx.navigation.plusAssign
import androidx.navigation.createGraph
import androidx.navigation.findNavController
import androidx.fragment.app.FragmentContainerView
import android.view.ViewGroup.LayoutParams.MATCH_PARENT

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FirebaseTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AuthNavigator()
                }
            }
        }
    }
}
class MainActivity : AppCompatActivity() {
    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val navHostFragment = FragmentContainerView(this).apply {
            id = ViewID.navHost
            layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT)
        }
        setContentView(navHostFragment)

        // Création du NavController
        val navHost = NavHostFragment.create(R.navigation.nav_graph)
        supportFragmentManager.beginTransaction()
            .replace(ViewID.navHost, navHost)
            .setPrimaryNavigationFragment(navHost)
            .commitNow()

        navController = navHost.navController
        navController.graph = navController.createGraph(startDestination = "home") {
            fragment<HomeFragment>("home")
            fragment<MinuteurFragment>("minuteur")
            fragment<CalculetteFragment>("calculette")
            fragment<ChronometreFragment>("chrono")
        }
    }
}

object ViewID {
    const val navHost = 123456
}