package com.example.data.repoImpl

import com.example.constants.Constants.API_KEY
import com.example.data.remote.ApiService
import com.example.data.response.NewsResponse
import com.example.domain.repo.GetNewsRepo

class GetNewsRepoImpl(private val apiService: ApiService) : GetNewsRepo {
    override suspend fun getAllNews(): NewsResponse {
        return apiService.getNews(API_KEY)
    }
}