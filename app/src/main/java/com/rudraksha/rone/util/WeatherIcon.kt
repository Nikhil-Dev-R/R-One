package com.rudraksha.rone.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.automirrored.filled.TrendingDown
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

fun getWeatherIcon(iconCode: String): WeatherIcon {
    return when (iconCode) {
        "01d" -> WeatherIcon(icon = Icons.Filled.WbSunny, tint = Color.Yellow) // Clear sky (day)
        "01n" -> WeatherIcon(icon = Icons.Filled.Star, tint = Color.White) // Clear sky (night)
        "02d", "03d", "04d" -> WeatherIcon(icon = Icons.Filled.Cloud, tint = Color.White) // Few clouds, scattered clouds, broken clouds (day)
        "02n", "03n", "04n" -> WeatherIcon(icon = Icons.Filled.Cloud, tint = Color.LightGray) // Few clouds, scattered clouds, broken clouds (night)
        "09d", "10d", "11d" -> WeatherIcon(icon = Icons.Filled.WaterDrop, tint = Color.White) // Shower rain, rain, thunderstorm (day)
        "09n", "10n", "11n" -> WeatherIcon(icon = Icons.Filled.WaterDrop, tint = Color.LightGray) // Shower rain, rain, thunderstorm (night)
        "13d", "13n" -> WeatherIcon(icon = Icons.Filled.WaterDrop, tint = Color.White) // Snow (day and night)
        "50d", "50n" -> WeatherIcon(icon = Icons.Filled.Visibility, tint = Color.White) // Mist (day and night)
        "wind" -> WeatherIcon(icon = Icons.Filled.Air, tint = Color.Gray) // Wind
        "humidity" -> WeatherIcon(icon = Icons.Filled.WaterDrop, tint = Color.White) // Humidity
        "visibility" -> WeatherIcon(icon = Icons.Filled.Visibility, tint = Color.White) // Visibility
        "pressureUp" -> WeatherIcon(icon = Icons.AutoMirrored.Filled.TrendingUp, tint = Color.White) // Pressure up
        "pressureDown" -> WeatherIcon(icon = Icons.AutoMirrored.Filled.TrendingDown, tint = Color.White) // Pressure down
        else -> WeatherIcon(icon = Icons.Filled.WbSunny, tint = Color.Yellow) // Default to clear sky (day) if unknown
    }
}

fun getWeatherGradientColors(iconCode: String): List<Color> {
    return when (iconCode) {
        "01d", "01n" -> sunnyGradientColors
        "02d", "03d", "04d", "02n", "03n", "04n" -> cloudyGradientColors
        "09d", "10d", "11d", "09n", "10n", "11n" -> rainyGradientColors
        "13d", "13n" -> snowyGradientColors
        "50d", "50n" -> foggyGradientColors

        else -> defaultWeatherGradientColors
    }
}

data class WeatherIcon(
    val icon: ImageVector,
    val tint: Color
)