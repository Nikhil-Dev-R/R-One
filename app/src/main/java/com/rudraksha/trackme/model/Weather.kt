package com.rudraksha.trackme.model

data class WeatherResponse(
    val coord: Coordinates = Coordinates(0.0, 0.0),
    val weather: List<WeatherCondition> = listOf(),
    val base: String = "",
    val main: MainWeather = MainWeather(0.0, 0.0, 0.0, 0.0, 0, 0),
    val visibility: Int = 0,
    val wind: Wind = Wind(0.0, 0),
    val clouds: Clouds = Clouds(0),
    val dt: Long = 0L,
    val sys: SystemInfo = SystemInfo(0, 0, "", 0L, 0L),
    val timezone: Int = 0,
    val id: Int = 0,
    val name: String = "",
    val cod: Int = 0
)

data class Coordinates(
    val lon: Double,
    val lat: Double
)

data class MainWeather(
    val temp: Double,
    val feels_like: Double,
    val temp_min: Double,
    val temp_max: Double,
    val pressure: Int,
    val humidity: Int
)

data class Wind(
    val speed: Double,
    val deg: Int
)

data class Clouds(
    val all: Int
)

data class SystemInfo(
    val type: Int? = null,
    val id: Int? = null,
    val country: String,
    val sunrise: Long,
    val sunset: Long
)

// Data class for city suggestions
data class CitySuggestion(
    val name: String = "",
    val country: String = ""
)

/*

{
  "coord": {
    "lon": -0.1257,
    "lat": 51.5085
  },
  "weather": [
    {
      "id": 800,
      "main": "Clear",
      "description": "clear sky",
      "icon": "01d"
    }
  ],
  "base": "stations",
  "main": {
    "temp": 288.55,
    "feels_like": 287.13,
    "temp_min": 287.04,
    "temp_max": 289.82,
    "pressure": 1012,
    "humidity": 56
  },
  "visibility": 10000,
  "wind": {
    "speed": 4.63,
    "deg": 220
  },
  "clouds": {
    "all": 0
  },
  "dt": 1618317040,
  "sys": {
    "type": 1,
    "id": 1414,
    "country": "GB",
    "sunrise": 1618282134,
    "sunset": 1618333901
  },
  "timezone": 3600,
  "id": 2643743,
  "name": "London",
  "cod": 200
}


 */

data class OneCallResponse(
    val lat: Double = 0.0,
    val lon: Double = 0.0,
    val timezone: String = "",
    val timezone_offset: Int = 0,
    val current: CurrentWeather = CurrentWeather(),
    val hourly: List<HourlyWeather> = listOf(),
    val daily: List<DailyWeather> = listOf()
)

data class CurrentWeather(
    val dt: Long = 0L,
    val sunrise: Long = 0L,
    val sunset: Long = 0L,
    val temp: Double = 0.0,
    val feels_like: Double = 0.0,
    val pressure: Int = 0,
    val humidity: Int = 0,
    val dew_point: Double = 0.0,
    val uvi: Double = 0.0,
    val clouds: Int = 0,
    val visibility: Int = 0,
    val wind_speed: Double = 0.0,
    val wind_deg: Int = 0,
    val weather: List<WeatherCondition> = listOf()
)

data class HourlyWeather(
    val dt: Long,
    val temp: Double,
    val feels_like: Double? = null,
    val pressure: Int,
    val humidity: Int,
    val dew_point: Double,
    val uvi: Double? = null,
    val clouds: Int,
    val visibility: Int,
    val wind_speed: Double,
    val wind_deg: Int,
    val weather: List<WeatherCondition>,
    val pop: Double // Probability of precipitation
)

data class DailyWeather(
    val dt: Long,
    val sunrise: Long,
    val sunset: Long,
    val moonrise: Long,
    val moonset: Long,
    val moon_phase: Double,
    val temp: DailyTemperature,
    val feels_like: FeelsLikeTemperature,
    val pressure: Int,
    val humidity: Int,
    val dew_point: Double,
    val wind_speed: Double,
    val wind_deg: Int,
    val weather: List<WeatherCondition>,
    val clouds: Int,
    val pop: Double,
    val uvi: Double
)

data class DailyTemperature(
    val day: Double,
    val min: Double,
    val max: Double,
    val night: Double,
    val eve: Double,
    val morn: Double
)

data class FeelsLikeTemperature(
    val day: Double,
    val night: Double,
    val eve: Double,
    val morn: Double
)

data class WeatherCondition(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)

/*
{
  "lat": 37.7749,
  "lon": -122.4194,
  "timezone": "America/Los_Angeles",
  "timezone_offset": -28800,
  "current": {
    "dt": 1618317040,
    "sunrise": 1618282134,
    "sunset": 1618333901,
    "temp": 284.07,
    "feels_like": 282.15,
    "pressure": 1016,
    "humidity": 62,
    "dew_point": 277.93,
    "uvi": 0.89,
    "clouds": 1,
    "visibility": 10000,
    "wind_speed": 1.34,
    "wind_deg": 318,
    "weather": [
      {
        "id": 800,
        "main": "Clear",
        "description": "clear sky",
        "icon": "01d"
      }
    ]
  },
  "hourly": [
    {
      "dt": 1618315200,
      "temp": 282.58,
      "feels_like": 280.4,
      "pressure": 1017,
      "humidity": 66,
      "dew_point": 276.98,
      "uvi": 1.4,
      "clouds": 1,
      "visibility": 10000,
      "wind_speed": 1.5,
      "wind_deg": 295,
      "weather": [
        {
          "id": 800,
          "main": "Clear",
          "description": "clear sky",
          "icon": "01d"
        }
      ],
      "pop": 0
    },
    {
      "dt": 1618318800,
      "temp": 284.07,
      "feels_like": 281.86,
      "pressure": 1016,
      "humidity": 62,
      "dew_point": 277.93,
      "uvi": 0.89,
      "clouds": 1,
      "visibility": 10000,
      "wind_speed": 1.34,
      "wind_deg": 318,
      "weather": [
        {
          "id": 800,
          "main": "Clear",
          "description": "clear sky",
          "icon": "01d"
        }
      ],
      "pop": 0
    }
  ],
  "daily": [
    {
      "dt": 1618308000,
      "sunrise": 1618282134,
      "sunset": 1618333901,
      "moonrise": 1618284960,
      "moonset": 1618339740,
      "moon_phase": 0.04,
      "temp": {
        "day": 284.07,
        "min": 279.15,
        "max": 284.99,
        "night": 282.58,
        "eve": 283.74,
        "morn": 279.63
      },
      "feels_like": {
        "day": 281.86,
        "night": 280.4,
        "eve": 281.23,
        "morn": 277.56
      },
      "pressure": 1016,
      "humidity": 62,
      "dew_point": 277.93,
      "wind_speed": 1.34,
      "wind_deg": 318,
      "weather": [
        {
          "id": 800,
          "main": "Clear",
          "description": "clear sky",
          "icon": "01d"
        }
      ],
      "clouds": 1,
      "pop": 0,
      "uvi": 4.89
    }
  ]
}

 */