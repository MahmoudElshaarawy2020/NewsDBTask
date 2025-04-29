package com.example.data.remote

import com.example.data.response.NewsResponse
import com.example.data.response.SourcesResponseList
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("everything")
    suspend fun getPagingNews(
        @Query("q") query: String,
        @Query("apiKey") apiKey: String,
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int
        ): NewsResponse

    @GET("top-headlines/sources")
    suspend fun getSources(
        @Query("apiKey") apiKey: String,
    ): SourcesResponseList
}