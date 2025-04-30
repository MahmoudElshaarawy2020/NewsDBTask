package com.example.domain.use_case

import com.example.data.local.model.FavoriteArticle
import com.example.domain.repo.ArticleRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoritesUseCase @Inject constructor(
    private val repository: ArticleRepository
) {
    operator fun invoke(): Flow<List<FavoriteArticle>> = repository.getFavorites()
}