package com.rudraksha.trackme.ui

import android.Manifest
import android.content.Context
import android.os.Build
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.unit.IntOffset
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.rudraksha.trackme.ui.screens.FacebookScreen
import com.rudraksha.trackme.ui.screens.HomeScreen
import com.rudraksha.trackme.ui.screens.InstagramScreen
import com.rudraksha.trackme.ui.screens.LocationScreen
import com.rudraksha.trackme.ui.screens.NewsScreen
import com.rudraksha.trackme.ui.screens.ProfileScreen
import com.rudraksha.trackme.ui.screens.WeatherScreen
import com.rudraksha.trackme.ui.screens.XScreen
import com.rudraksha.trackme.viewmodel.MapViewModel

// Define routes as a sealed class
sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Location : Screen("location")
    data object Weather : Screen("weather")
    data object News : Screen("news")
    data object Profile : Screen("profile")
    data object Facebook : Screen("facebook")
    data object X : Screen("twitter")
    data object Instagram : Screen("instagram")
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun NavigationManager(
    navController: NavHostController,
    context: Context
) {
    val mapViewModel: MapViewModel = viewModel(
        factory = MapViewModel.Factory(context.applicationContext)
    )
    val mapUiState = mapViewModel.uiState.collectAsState().value
    val fitnessData = mapUiState.fitnessData

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(route = Screen.Home.route) {
            HomeScreen(navController = navController)
        }

        composable(
            route = Screen.Profile.route,
            enterTransition = {
                slideIn (
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioHighBouncy,
                        stiffness = Spring.StiffnessLow
                    ),
                    initialOffset = { size ->
                        IntOffset(x = -size.height, y = -size.width)
                    }
                )
            },
            exitTransition = {
                slideOut(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioHighBouncy,
                        stiffness = Spring.StiffnessLow
                    ),
                    targetOffset = { size ->
                        IntOffset(x = size.height, y = size.width)
                    }
                )
            }
        ) {
            ProfileScreen()
        }

        composable(route = Screen.Weather.route) {
            WeatherScreen()
        }

        composable(route = Screen.Location.route) {
            // Create multiple permission states
            val permissionList = mutableListOf(
                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
            )
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                permissionList.add(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
            }
            val permissionStates = rememberMultiplePermissionsState( permissions = permissionList.toList() )

            LaunchedEffect(permissionStates.permissions) {
                mapViewModel.checkPermissions(permissionStates)
            }
            val permissionGranted = mapUiState.permissionsGranted

            fun checkAndRequestPermission(): Boolean {
                mapViewModel.checkPermissions(permissionStates)
                return mapUiState.permissionsGranted
            }

            LocationScreen(
                context = context,
                permissionGranted = permissionGranted,
                checkPermission = { checkAndRequestPermission() },
                fitnessData = fitnessData,
                startTracking = { mapViewModel.startTracking() },
                stopTracking = { mapViewModel.stopTracking() }
            )
        }

        composable(route = Screen.News.route) {
            NewsScreen()
        }

        composable(route = Screen.Facebook.route) {
            FacebookScreen()
        }

        composable(route = Screen.X.route) {
            XScreen()
        }

        composable(route = Screen.Instagram.route) {
            InstagramScreen()
        }
    }
}