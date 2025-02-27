package com.rudraksha.rone.ui

import android.Manifest
import android.content.Context
import android.os.Build
import android.util.Log
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
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.rudraksha.rone.model.Article
import com.rudraksha.rone.ui.screens.FacebookScreen
import com.rudraksha.rone.ui.screens.HomeScreen
import com.rudraksha.rone.ui.screens.InstagramScreen
import com.rudraksha.rone.ui.screens.LocationScreen
import com.rudraksha.rone.ui.screens.NewsDescScreen
import com.rudraksha.rone.ui.screens.NewsScreen
import com.rudraksha.rone.ui.screens.ProfileScreen
import com.rudraksha.rone.ui.screens.WeatherScreen
import com.rudraksha.rone.ui.screens.XScreen
import com.rudraksha.rone.viewmodel.MapViewModel
import com.rudraksha.rone.viewmodel.NewsViewModel
import com.rudraksha.rone.viewmodel.WeatherViewModel
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

// Define routes as a sealed class
sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Location : Screen("location")
    data object Weather : Screen("weather")
    data object News : Screen("news")
    data object NewsDesc : Screen("news_desc")
    data object Profile : Screen("profile")
    data object Facebook : Screen("facebook")
    data object FacebookPost : Screen("facebook_post")
    data object X : Screen("twitter")
    data object XPost : Screen("twitter_post")
    data object Instagram : Screen("instagram")
    data object InstagramPost : Screen("instagram_post")
}

@Composable
fun NavigationManager(
    navController: NavHostController,
    context: Context
) {
    val mapViewModel: MapViewModel = viewModel(
        factory = MapViewModel.Factory(context.applicationContext)
    )
    val mapUiState = mapViewModel.uiState.collectAsState().value
    val fitnessData = mapUiState.mapData

    val weatherViewModel: WeatherViewModel = viewModel()

    val newsViewModel: NewsViewModel = viewModel<NewsViewModel>()
    val newsUiState = newsViewModel.newsState.collectAsState().value
//    Log.d("NAAMAN", newsUiState.news.keys.toString())

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
            WeatherScreen(
                weatherViewModel = weatherViewModel
            )
        }

        composable(route = Screen.Location.route) {
            LocationScreen(
                context = context,
                fitnessData = fitnessData,
                startTracking = { mapViewModel.startTracking() },
                stopTracking = { mapViewModel.stopTracking() }
            )
        }

        composable(route = Screen.News.route) {
            NewsScreen(
                fetchNews = newsViewModel::fetchNews,
                newsUiState = newsUiState,
                onItemClick = { url ->
                    val encodedUrl = URLEncoder.encode(url, StandardCharsets.UTF_8.toString())
                    navController.navigate("${Screen.NewsDesc.route}/${encodedUrl}")
                }
            )
        }
        composable(
            route = "${Screen.NewsDesc.route}/{url}",
            arguments = listOf(navArgument("url") { type = NavType.StringType },)
        ) { navStack ->
            val encodedUrl = navStack.arguments?.getString("url") ?: "default"
            val url = URLDecoder.decode(encodedUrl, StandardCharsets.UTF_8.toString())
            Log.d("url", url)

            val article = newsUiState.news.values.flatMap { it }.find { it.url == url }

            Log.d("Nav:Article", article?.title ?: "title cdcd")
            NewsDescScreen(
                article = article ?: Article()
            )
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