package com.example.data.repoImpl

import com.example.constants.Constants.API_KEY
import com.example.constants.Result
import com.example.data.remote.ApiService
import com.example.data.response.NewsResponse
import com.example.domain.repo.GetNewsRepo
import javax.inject.Inject

class GetNewsRepoImpl @Inject constructor(
    private val apiService: ApiService
) : GetNewsRepo {
    override suspend fun getAllNews(query: String): Result<NewsResponse> {
        return apiService.getNews(query, API_KEY)
    }
}