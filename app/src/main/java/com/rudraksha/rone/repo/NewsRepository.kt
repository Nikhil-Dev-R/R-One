package com.rudraksha.rone.repo

import com.rudraksha.rone.model.NewsResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

const val NEWS_API_KEY = "7e9f7afbfb604b3daf97b884d38a4035"

interface NewsApi {
    @GET("v2/top-headlines")
    suspend fun getNewsByCategory(
        @Query("category") category: String,
        @Query("apiKey") apiKey: String = NEWS_API_KEY,
        @Query("pageSize") pageSize: Int = 20
    ): NewsResponse
}

object NewsRepository {
    private const val BASE_URL = "https://newsapi.org/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    val api: NewsApi = retrofit.create(NewsApi::class.java)
}
