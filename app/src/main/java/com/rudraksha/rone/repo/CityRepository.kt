package com.rudraksha.rone.repo

import com.rudraksha.rone.model.CitySuggestion
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

const val WEATHER_API_KEY = "3b11da221bb0601d6da9b01013d27e36"
// Base URL for the OpenWeather API
const val BASE_URL = "https://api.openweathermap.org/"

// Retrofit API service
interface CityApi {
    @GET("geo/1.0/direct")
    suspend fun getCitySuggestions(
        @Query("q") query: String,
        @Query("limit") limit: Int = 10,
        @Query("appid") apiKey: String = WEATHER_API_KEY
    ): List<CityCoordinates>
}

object CityRepository {
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api: CityApi = retrofit.create(CityApi::class.java)
}

/*

[
    {
        "name": "London",
        "local_names": {
            "en": "London"
        },
        "lat": 51.5085,
        "lon": -0.1257,
        "country": "GB"
    }
]

*/

/*
// API Interface
interface GeocodingApi {
    @GET("geo/1.0/direct")
    suspend fun getCityCoordinates(
        @Query("q") city: String,
        @Query("limit") limit: Int = 1,
        @Query("appid") apiKey: String
    ): List<CityCoordinates>
}*/

// Data Model
data class CityCoordinates(
    val name: String = "",
    val lat: Double = 0.0,
    val lon: Double = 0.0,
    val country: String = ""
)

/*

// Retrofit Instance
object GeocodingService {
    private const val BASE_URL = "https://api.openweathermap.org/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api: GeocodingApi = retrofit.create(GeocodingApi::class.java)
}
*/

fun CityCoordinates.toCitySuggestion(): CitySuggestion {
    return CitySuggestion(
        name = this.name,
        country = this.country
    )
}