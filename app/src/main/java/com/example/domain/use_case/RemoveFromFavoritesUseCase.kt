package com.example.domain.use_case

import com.example.data.response.ArticlesItem
import com.example.domain.repo.ArticleRepository
import javax.inject.Inject

class RemoveFromFavoritesUseCase @Inject constructor(
    private val repository: ArticleRepository
) {
    suspend operator fun invoke(article: ArticlesItem) = repository.removeFromFavorites(article)
}