package com.rudraksha.trackme.repo

import com.rudraksha.trackme.model.OneCallResponse
import com.rudraksha.trackme.model.WeatherResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {
    @GET("data/2.5/weather")
    suspend fun getWeatherByCity(
        @Query("q") city: String = "London",
        @Query("appid") apiKey: String = WEATHER_API_KEY,
        @Query("units") units: String = "metric" // For Celsius
    ): WeatherResponse

    // Based on Lat and Long
    @GET("data/2.5/onecall")
    suspend fun getWeatherByLatLon(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") apiKey: String = WEATHER_API_KEY,
        @Query("units") units: String = "metric",
        @Query("exclude") exclude: String = "minutely" // Exclude unused data
    ): OneCallResponse
}

// BASE_URL + @GET_term + @Query_parameters ?q=London + &appid=API_KEY + &units=metric
// https://api.openweathermap.org/data/2.5/weather?q=[city_name]&appid=[your_api_key]&units=metric

object WeatherRepository {
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api: WeatherApi = retrofit.create(WeatherApi::class.java)
}