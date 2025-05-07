package com.example.domain.use_case

import com.example.data.response.ArticlesItem
import com.example.domain.repo.GetNewsRepo
import javax.inject.Inject

class GetAllNewsUseCase @Inject constructor(
    private val repository: GetNewsRepo
) {
    suspend operator fun invoke(query: String, pageSize: Int, page: Int): List<ArticlesItem> {
        return repository.getAllNews(query = query, pageSize = pageSize, page = page)
    }
}
