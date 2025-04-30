package com.example.domain.use_case

import com.example.domain.repo.ArticleRepository
import javax.inject.Inject

class IsFavoriteUseCase @Inject constructor(
    private val repository: ArticleRepository
) {
    suspend operator fun invoke(url: String): Boolean = repository.isFavorite(url)
}