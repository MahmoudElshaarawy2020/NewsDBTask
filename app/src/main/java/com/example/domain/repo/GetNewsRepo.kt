package com.example.domain.repo

import com.example.data.response.ArticlesItem

interface GetNewsRepo {
    suspend fun getAllNews(query: String, pageSize: Int, page: Int): List<ArticlesItem>
}
