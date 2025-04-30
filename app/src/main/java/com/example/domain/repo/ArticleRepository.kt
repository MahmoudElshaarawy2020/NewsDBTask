package com.example.domain.repo

import com.example.data.local.model.FavoriteArticle
import com.example.data.response.ArticlesItem
import kotlinx.coroutines.flow.Flow

interface ArticleRepository {
    suspend fun addToFavorites(article: ArticlesItem)
    suspend fun removeFromFavorites(article: ArticlesItem)
    fun getFavorites(): Flow<List<FavoriteArticle>>
    suspend fun isFavorite(url: String): Boolean
}