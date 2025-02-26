package com.rudraksha.rone

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.rudraksha.rone.ui.NavigationManager
import com.rudraksha.rone.ui.theme.TrackMeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TrackMeTheme {
//                TempScreen()
                val navController = rememberNavController()
                NavigationManager(
                    navController = navController,
                    context = this,
                )
            }
        }
    }
}

