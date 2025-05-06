package com.example.data.repoImpl


import com.example.constants.Constants.API_KEY
import com.example.data.remote.ApiService
import com.example.data.response.ArticlesItem
import com.example.domain.repo.GetNewsRepo
import javax.inject.Inject

class GetNewsRepoImpl @Inject constructor(
    private val apiService: ApiService
) : GetNewsRepo {
    override suspend fun getAllNews(
        query: String,
        pageSize: Int,
        page: Int
    ): List<ArticlesItem> {
        val response = apiService.getPagingNews(
            query = query,
            page = page,
            pageSize = pageSize,
            apiKey = API_KEY
        )
        return response.articles?.filterNotNull() ?: emptyList()
    }
}
