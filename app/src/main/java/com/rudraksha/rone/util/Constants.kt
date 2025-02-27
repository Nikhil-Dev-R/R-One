package com.rudraksha.rone.util

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/*
API_KEY = 0Hnr14Uvl2rshVUpVtcfU6Cgo
API_KEY_SECRET = TpN0mvgrT0fOOI00NvHWX5mgI6051nnYBqHQ1KKth08woL2NqF
*/
val categoryGradientColors = listOf(
    Color(0xABF44336),
    Color(0xAB9C27B0),
    Color(0xAB3F51B5),
)

// Sunny
val sunnyGradientColors = listOf(
    Color(0xFFf7dc6f), // bright yellow
    Color(0xFFffd700) // vibrant orange
)

// Cloudy
val cloudyGradientColors = listOf(
    Color(0xFF87ceeb), // light blue
    Color(0xFF4682b4) // grayish blue
)

// Rainy
val rainyGradientColors = listOf(
    Color(0xFF64b5f6), // blue gray
    Color(0xFF455a64) // dark gray blue
)

// Snowy
val snowyGradientColors = listOf(
    Color(0xFFadd8e6), // pale blue
    Color(0xFF87ceeb) // light blue
)

// Thunderstorm
val thunderstormGradientColors = listOf(
    Color(0xFF616161), // dark gray
    Color(0xFF212121) // charcoal
)

// Foggy
val foggyGradientColors = listOf(
    Color(0xFFd3d3d3), // light gray
    Color(0xFF808080) // medium gray
)
val defaultWeatherGradientColors = listOf(
    Color(0xFF8700FD),
    Color(0xFF2700F5),
    Color(0xFF0090FD),
    Color(0xFF00F3FD),
)

@Composable
fun getDefaultGradientColors() = listOf(
    MaterialTheme.colorScheme.primary,
    MaterialTheme.colorScheme.secondary,
    MaterialTheme.colorScheme.tertiary
)

val categoryList = listOf(
    "general",
    "business",
    "entertainment",
    "health",
    "science",
    "sports",
    "technology",
)

val ExpandedImageSize = 300.dp
val CollapsedImageSize = 64.dp

