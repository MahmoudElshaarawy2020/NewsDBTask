package com.example.domain.use_case

import androidx.paging.PagingData
import com.example.data.response.ArticlesItem
import com.example.domain.repo.GetNewsRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllNewsUseCase @Inject constructor(
    private val repository: GetNewsRepo
) {
    suspend operator fun invoke(query: String, pageSize: Int, page: Int): List<ArticlesItem> {
        return repository.getAllNews(query = query, pageSize = pageSize, page = page)
    }
}
