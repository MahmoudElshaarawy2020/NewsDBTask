package com.example.domain.repo

import androidx.paging.PagingData
import com.example.data.response.ArticlesItem
import kotlinx.coroutines.flow.Flow

interface GetNewsRepo {
    suspend fun getAllNews(query: String, pageSize: Int, page: Int): List<ArticlesItem>
}
