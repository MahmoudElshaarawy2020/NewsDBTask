package com.example.data.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_articles")
data class FavoriteArticle(
    @PrimaryKey val url: String,
    @ColumnInfo(name = "Title")
    val title: String,
    val description: String?,
    @ColumnInfo(name = "image")
    val urlToImage: String?,
    val publishedAt: String?,
    val sourceName: String?
)
