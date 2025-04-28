package com.example.domain.use_case

import com.example.data.response.NewsResponse
import com.example.domain.repo.GetNewsRepo
import javax.inject.Inject

class GetAllNewsUseCase @Inject constructor(
    private val repository: GetNewsRepo
) {
    suspend operator fun invoke(query: String): NewsResponse {
        return repository.getAllNews(query)
    }
}