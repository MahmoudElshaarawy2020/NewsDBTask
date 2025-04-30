package com.example.newsdbtask.di

import android.content.Context
import androidx.room.Room
import com.example.data.local.dao.ArticleDao
import com.example.data.local.database.NewsDatabase
import com.example.data.remote.ApiService
import com.example.data.repoImpl.ArticleRepositoryImpl
import com.example.data.repoImpl.GetNewsRepoImpl
import com.example.data.repoImpl.GetSourceRepoImpl
import com.example.domain.repo.ArticleRepository
import com.example.domain.repo.GetNewsRepo
import com.example.domain.repo.GetSourcesRepo
import com.example.domain.use_case.AddToFavoritesUseCase
import com.example.domain.use_case.IsFavoriteUseCase
import com.example.domain.use_case.RemoveFromFavoritesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideGetNewsRepo(apiService: ApiService): GetNewsRepo {
        return GetNewsRepoImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideGetSourcesRepo(apiService: ApiService): GetSourcesRepo {
        return GetSourceRepoImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): NewsDatabase {
        return Room.databaseBuilder(
            context,
            NewsDatabase::class.java,
            "news_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideArticleDao(appDatabase: NewsDatabase) = appDatabase.articleDao()

    @Provides
    @Singleton
    fun provideArticleRepository(articleDao: ArticleDao): ArticleRepository {
        return ArticleRepositoryImpl(articleDao)
    }
}