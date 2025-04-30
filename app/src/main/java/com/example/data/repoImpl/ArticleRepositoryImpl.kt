package com.example.data.repoImpl

import com.example.data.local.dao.ArticleDao
import com.example.data.local.model.FavoriteArticle
import com.example.data.response.ArticlesItem
import com.example.domain.repo.ArticleRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ArticleRepositoryImpl @Inject constructor(
    private val articleDao: ArticleDao,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ArticleRepository {

    override suspend fun addToFavorites(article: ArticlesItem): Unit = withContext(dispatcher) {
        article.toFavoriteArticle().let {
            articleDao.insertFavorite(it)
        }
    }

    override suspend fun removeFromFavorites(article: ArticlesItem): Unit = withContext(dispatcher) {
        article.toFavoriteArticle().let {
            articleDao.deleteFavorite(it)
        }
    }

    override fun getFavorites(): Flow<List<FavoriteArticle>> = articleDao.getAllFavorites()

    override suspend fun isFavorite(url: String): Boolean = withContext(dispatcher) {
        articleDao.getFavoriteCount(url) > 0
    }
}

//to convert the ArticlesItem to FavoriteArticle
fun ArticlesItem.toFavoriteArticle(): FavoriteArticle {
    return FavoriteArticle(
        url = this.url ?: "",
        title = this.title ?: "",
        description = this.description,
        urlToImage = this.urlToImage,
        publishedAt = this.publishedAt,
        sourceName = this.source?.name
    )
}