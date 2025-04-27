package com.example.domain.repo

import com.example.data.response.NewsResponse

interface GetNewsRepo {
    suspend fun getAllNews(): NewsResponse
}