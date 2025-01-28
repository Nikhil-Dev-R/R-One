package com.rudraksha.trackme.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.WbTwilight
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.ui.graphics.vector.ImageVector

fun getWeatherIcon(iconCode: String): ImageVector {
    return when (iconCode) {
        "01d" -> Icons.Filled.WbSunny // Clear sky (day)
        "01n" -> Icons.Filled.WbTwilight // Clear sky (night)
        "02d", "03d", "04d" -> Icons.Filled.Cloud // Few clouds, scattered clouds, broken clouds (day)
        "02n", "03n", "04n" -> Icons.Filled.Cloud // Few clouds, scattered clouds, broken clouds (night)
        "09d", "10d", "11d" -> Icons.Filled.WaterDrop // Shower rain, rain, thunderstorm (day)
        "09n", "10n", "11n" -> Icons.Filled.WaterDrop // Shower rain, rain, thunderstorm (night)
        "13d", "13n" -> Icons.Filled.WaterDrop // Snow (day and night)
        "50d", "50n" -> Icons.Filled.Visibility // Mist (day and night)
        "wind" -> Icons.Filled.Air // Wind
        "pressureUp" -> Icons.AutoMirrored.Filled.TrendingUp // Pressure up
        "pressureDown" -> Icons.AutoMirrored.Filled.TrendingDown // Pressure down
        else -> Icons.Filled.WbSunny // Default to clear sky (day) if unknown
    }
}