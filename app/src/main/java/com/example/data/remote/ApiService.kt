package com.example.data.remote

import com.example.data.response.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("everything")
    suspend fun getNews(
        @Query("apiKey") apiKey: String,
        ): NewsResponse
}