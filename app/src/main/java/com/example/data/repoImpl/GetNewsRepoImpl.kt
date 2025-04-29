package com.example.data.repoImpl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.constants.Constants.API_KEY
import com.example.data.paging.NewsPagingSource
import com.example.data.remote.ApiService
import com.example.data.response.ArticlesItem
import com.example.data.response.NewsResponse
import com.example.domain.repo.GetNewsRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNewsRepoImpl @Inject constructor(
    private val apiService: ApiService
) : GetNewsRepo {
    override suspend fun getAllNews(
        query: String,
        pageSize: Int,
        page: Int
    ): Flow<PagingData<ArticlesItem>> {  // Change to ArticlesItem
        return Pager(
            config = PagingConfig(
                pageSize = pageSize,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { NewsPagingSource(apiService, query) }
        ).flow
    }
}
