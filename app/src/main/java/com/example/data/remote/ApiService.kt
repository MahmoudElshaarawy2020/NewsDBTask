package com.example.data.remote

import com.example.constants.Result
import com.example.data.response.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("everything")
    suspend fun getNews(
        @Query("q") query: String,
        @Query("apiKey") apiKey: String = "3599531d533349d3bd0eaa39a88727e5",
        ): Result<NewsResponse>
}