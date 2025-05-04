package com.example.newsdbtask.ui.presentation.home

import android.util.Log
import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.constants.Result
import com.example.data.response.ArticlesItem
import com.example.data.response.NewsResponse
import com.example.data.response.SourcesResponseList
import com.example.domain.use_case.AddToFavoritesUseCase
import com.example.domain.use_case.GetAllNewsUseCase
import com.example.domain.use_case.GetSourcesUseCase
import com.example.domain.use_case.IsFavoriteUseCase
import com.example.domain.use_case.RemoveFromFavoritesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

// HomeViewModel.kt
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAllNewsUseCase: GetAllNewsUseCase,
    private val getSourcesUseCase: GetSourcesUseCase,
    private val addToFavoritesUseCase: AddToFavoritesUseCase,
    private val removeFromFavoritesUseCase: RemoveFromFavoritesUseCase,
    private val isFavoriteUseCase: IsFavoriteUseCase
) : ViewModel() {


    private val _favoritesMap = mutableStateMapOf<String, Boolean>()
    val favoritesMap: Map<String, Boolean> get() = _favoritesMap

    private val _getPagingNewsState = MutableStateFlow<PagingData<ArticlesItem>>(PagingData.empty())
    val getPagingNewsState: StateFlow<PagingData<ArticlesItem>> = _getPagingNewsState

    private val _getSourcesState = MutableStateFlow<Result<SourcesResponseList>>(Result.Loading())
    val getSourcesState: StateFlow<Result<SourcesResponseList>> get() = _getSourcesState

    private val _currentSource = MutableStateFlow("bbc-news")
    val currentSource: StateFlow<String> = _currentSource

    private var currentPage = 1
    private val PAGE_SIZE = 2

    init {
        getSources()
    }

    fun fetchNews(source: String? = null, page: Int? = null) {
        viewModelScope.launch {
            try {
                // Update source if changed
                source?.let {
                    if (it != _currentSource.value) {
                        _currentSource.value = it
                        currentPage = 1
                    }
                }

                // Update page if needed
                page?.let { currentPage = it }

                getAllNewsUseCase(_currentSource.value, currentPage, PAGE_SIZE)
                    .cachedIn(viewModelScope)
                    .collect { pagingData ->
                        _getPagingNewsState.value = pagingData
                    }
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error fetching news", e)
            }
        }
    }

    fun loadNextPage() {
        currentPage++
        fetchNews()
    }

    private fun getSources() {
        viewModelScope.launch {
            try {
                val sourcesResponse = getSourcesUseCase()
                _getSourcesState.value = Result.Success(sourcesResponse)
            } catch (e: Exception) {
                _getSourcesState.value = Result.Error("Unexpected Error: ${e.message}")
            }
        }
    }

    fun toggleFavorite(article: ArticlesItem) = viewModelScope.launch {
        val url = article.url ?: return@launch
        val current = _favoritesMap[url] ?: isFavoriteUseCase(url)
        if (current) removeFromFavoritesUseCase(article) else addToFavoritesUseCase(article)
        _favoritesMap[url] = !current
    }

    suspend fun isFavorite(url: String): Boolean {
        return _favoritesMap[url] ?: isFavoriteUseCase(url).also {
            _favoritesMap[url] = it
        }
    }


}