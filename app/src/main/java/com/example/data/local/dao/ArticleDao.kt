package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.OnConflictStrategy
import com.example.data.local.model.FavoriteArticle
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticleDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavorite(article: FavoriteArticle): Long

    @Delete
    fun deleteFavorite(article: FavoriteArticle): Int

    @Query("SELECT * FROM favorite_articles")
    fun getAllFavorites(): Flow<List<FavoriteArticle>>

    @Query("SELECT COUNT(*) FROM favorite_articles WHERE url = :url")
    fun getFavoriteCount(url: String): Int
}