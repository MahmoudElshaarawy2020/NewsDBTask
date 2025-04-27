package com.example.domain.repo

import com.example.data.response.NewsResponse
import com.example.constants.Result

interface GetNewsRepo {
    suspend fun getAllNews(query: String): Result<NewsResponse>
}