package com.example.data.remote

import com.example.constants.Result
import com.example.data.response.NewsResponse
import com.example.data.response.SourcesResponse
import com.example.data.response.SourcesResponseList
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("everything")
    suspend fun getNews(
        @Query("q") query: String,
        @Query("apiKey") apiKey: String,
        ): NewsResponse

    @GET("top-headlines/sources")
    suspend fun getSources(
        @Query("apiKey") apiKey: String,
    ): SourcesResponseList
}