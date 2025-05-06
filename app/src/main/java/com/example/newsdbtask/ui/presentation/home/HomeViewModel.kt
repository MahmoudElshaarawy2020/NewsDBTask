package com.example.newsdbtask.ui.presentation.home

import android.util.Log
import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.SavedStateHandle
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

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAllNewsUseCase: GetAllNewsUseCase,
    private val getSourcesUseCase: GetSourcesUseCase,
) : ViewModel() {

    private val _newsState = MutableStateFlow<Result<List<ArticlesItem>>>(Result.Loading())
    val newsState: StateFlow<Result<List<ArticlesItem>>> = _newsState

    private val _getSourcesState = MutableStateFlow<Result<SourcesResponseList>>(Result.Loading())
    val getSourcesState: StateFlow<Result<SourcesResponseList>> get() = _getSourcesState

    private val _currentSource = MutableStateFlow("bbc-news")
    val currentSource: StateFlow<String> = _currentSource

    private var _currentPage = 1
    private var _pageSize = 10
    private var isLoadingMore = false
    private var isEndReached = false

    init {
        getSources()
        fetchNews(reset = true)
    }

    fun fetchNews(source: String? = null, reset: Boolean = false) {
        viewModelScope.launch {
            try {
                source?.let {
                    if (it != _currentSource.value) {
                        _currentSource.value = it
                        _currentPage = 1
                        isEndReached = false
                        _newsState.value = Result.Loading()
                    }
                }

                if (isLoadingMore || isEndReached) return@launch
                isLoadingMore = true

                val pageToLoad = if (reset) 1 else _currentPage
                val newArticles = getAllNewsUseCase(_currentSource.value, _pageSize, pageToLoad)

                if (reset) {
                    _newsState.value = Result.Success(newArticles)
                } else {
                    val currentData = (_newsState.value as? Result.Success)?.data ?: emptyList()
                    _newsState.value = Result.Success(currentData + newArticles)
                }

                if (newArticles.size < _pageSize) {
                    isEndReached = true
                } else {
                    _currentPage++
                }

            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error fetching news: ${e.message}")
                _newsState.value = Result.Error(e.message ?: "Unknown error occurred")
            } finally {
                isLoadingMore = false
            }
        }
    }

    private fun getSources() {
        viewModelScope.launch {
            try {
                _getSourcesState.value = Result.Loading()
                val sourcesResponse = getSourcesUseCase()
                _getSourcesState.value = Result.Success(sourcesResponse)
            } catch (e: Exception) {
                _getSourcesState.value = Result.Error("Unexpected Error: ${e.message}")
            }
        }
    }
}