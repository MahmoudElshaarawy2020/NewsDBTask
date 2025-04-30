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
    // For insert - return Long (rowId) instead of suspend
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavorite(article: FavoriteArticle): Long

    // For delete - return Int (number of rows affected) instead of suspend
    @Delete
    fun deleteFavorite(article: FavoriteArticle): Int

    // For queries - use Flow
    @Query("SELECT * FROM favorite_articles")
    fun getAllFavorites(): Flow<List<FavoriteArticle>>

    // For existence check - use Int
    @Query("SELECT COUNT(*) FROM favorite_articles WHERE url = :url")
    fun getFavoriteCount(url: String): Int
}