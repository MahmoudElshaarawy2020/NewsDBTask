package com.example.newsdbtask.di

import com.example.data.remote.ApiService
import com.example.data.repoImpl.GetNewsRepoImpl
import com.example.domain.repo.GetNewsRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
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
}