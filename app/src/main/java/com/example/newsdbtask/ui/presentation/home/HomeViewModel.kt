package com.example.newsdbtask.ui.presentation.home

import android.util.Log
import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.example.constants.Result
import com.example.data.response.ArticlesItem
import com.example.data.response.Source
import com.example.data.response.SourcesResponseList
import com.example.domain.use_case.AddToFavoritesUseCase
import com.example.domain.use_case.GetAllNewsUseCase
import com.example.domain.use_case.GetFavoritesUseCase
import com.example.domain.use_case.GetSourcesUseCase
import com.example.domain.use_case.IsFavoriteUseCase
import com.example.domain.use_case.RemoveFromFavoritesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// HomeViewModel.kt
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAllNewsUseCase: GetAllNewsUseCase,
    private val getSourcesUseCase: GetSourcesUseCase,
    private val addToFavoritesUseCase: AddToFavoritesUseCase,
    private val removeFromFavoritesUseCase: RemoveFromFavoritesUseCase,
    private val getFavoritesUseCase: GetFavoritesUseCase,
    private val isFavoriteUseCase: IsFavoriteUseCase
) : ViewModel() {

    private val _favoritesMap = mutableStateMapOf<String, Boolean>()
    val favoritesMap: Map<String, Boolean> get() = _favoritesMap

    private val _favorites = MutableStateFlow<List<ArticlesItem>>(emptyList())
    val favorites: StateFlow<List<ArticlesItem>> = _favorites

    private val _getPagingNewsState = MutableStateFlow<PagingData<ArticlesItem>>(PagingData.empty())
    val getPagingNewsState: StateFlow<PagingData<ArticlesItem>> = _getPagingNewsState

    private val _newsList = MutableStateFlow<List<ArticlesItem>>(emptyList())
    val newsList: StateFlow<List<ArticlesItem>> = _newsList

    private val _getSourcesState = MutableStateFlow<Result<SourcesResponseList>>(Result.Loading())
    val getSourcesState: StateFlow<Result<SourcesResponseList>> get() = _getSourcesState

    private val _currentSource = MutableStateFlow("bbc-news")
    val currentSource: StateFlow<String> = _currentSource

    private var _pageSize = 10
    private var _currentPage = 1
    private var isLoadingMore = false
    private var isEndReached = false

    private val _isNavigated = MutableStateFlow(false)
    val isNavigated: StateFlow<Boolean> = _isNavigated

    private var _shouldSkipNextPageIncrement = false
    private var _lastNavigationTime = 0L
    private val navigationDebounceTime = 500L

    init {
        getSources()
        fetchNews(reset = true)
        loadFavorites()
    }

    fun prepareForNavigation() {
        _shouldSkipNextPageIncrement = true
        _lastNavigationTime = System.currentTimeMillis()
    }

    fun setIsNavigated(navigated: Boolean) {
        _isNavigated.value = navigated
    }

    fun fetchNews(source: String? = null, reset: Boolean = false) {
        viewModelScope.launch {
            try {
                source?.let {
                    if (it != _currentSource.value) {
                        _currentSource.value = it
                        _currentPage = 1
                        isEndReached = false
                        _newsList.value = emptyList()
                    }
                }

                if (isLoadingMore || isEndReached) return@launch
                isLoadingMore = true

                val now = System.currentTimeMillis()
                val shouldSkip =
                    _shouldSkipNextPageIncrement && (now - _lastNavigationTime) < navigationDebounceTime

                val pageToLoad = _currentPage

                val newArticles = getAllNewsUseCase(_currentSource.value, _pageSize, pageToLoad)

                if (reset) {
                    _newsList.value = newArticles
                } else {
                    _newsList.value = _newsList.value + newArticles
                }

                if (newArticles.size < _pageSize) {
                    isEndReached = true
                } else if (!shouldSkip) {
                    _currentPage++
                }

                // Reset skip flag only after page decision
                _shouldSkipNextPageIncrement = false

            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error fetching news: ${e.message}")
            } finally {
                isLoadingMore = false
            }
        }
    }

//    fun loadNextPage() {
//        currentPage++
//        fetchNews()
//    }

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

        if (current) {
            removeFromFavoritesUseCase(article)
            _favorites.value = _favorites.value.filterNot { it.url == url }
        } else {
            addToFavoritesUseCase(article)
            _favorites.value += article
        }

        _favoritesMap[url] = !current
    }

    private fun loadFavorites() {
        viewModelScope.launch {
            try {
                getFavoritesUseCase().collect { favorites ->
                    _favorites.value = favorites.map { favorite ->
                        ArticlesItem(
                            source = Source(name = favorite.sourceName),
                            title = favorite.title,
                            description = favorite.description,
                            url = favorite.url,
                            urlToImage = favorite.urlToImage,
                            publishedAt = favorite.publishedAt
                        )
                    }
                    // Update favorites map
                    favorites.forEach {
                        _favoritesMap[it.url] = true
                    }
                }
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error loading favorites", e)
            }
        }
    }


    suspend fun isFavorite(url: String): Boolean {
        return _favoritesMap[url] ?: isFavoriteUseCase(url).also {
            _favoritesMap[url] = it
        }
    }


}