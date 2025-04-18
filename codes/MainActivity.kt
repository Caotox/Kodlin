package com.example.monappli

import android.os.Bundle
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

class MainActivity : AppCompatActivity() {
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
            fragment<HomeFragment>("home")
            fragment<MinuteurFragment>("minuteur")
            fragment<CalculetteFragment>("calculette")
            fragment<ChronometreFragment>("chrono")
        }
    }
}

object ViewID {
    const val navHost = 123456 // un ID unique
}
