package com.example.newsdbtask.ui.presentation.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.constants.Result
import com.example.data.response.NewsResponse
import com.example.data.response.SourcesResponse
import com.example.data.response.SourcesResponseList
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import com.example.domain.use_case.GetAllNewsUseCase
import com.example.domain.use_case.GetSourcesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAllNewsUseCase: GetAllNewsUseCase,
    private val getSourcesUseCase: GetSourcesUseCase
) : ViewModel() {

    private val _getNewsState = MutableStateFlow<Result<NewsResponse>>(Result.Loading())
    val getNewsState: MutableStateFlow<Result<NewsResponse>> get() = _getNewsState

    private val _getSourcesState = MutableStateFlow<Result<SourcesResponseList>>(Result.Loading())
    val getSourcesState: StateFlow<Result<SourcesResponseList>> get() = _getSourcesState

    init {
        getSources()
    }

     fun getAllNews(query: String) {
        viewModelScope.launch {
            try {
                val newsResponse = getAllNewsUseCase.invoke(query)
                _getNewsState.value = Result.Success(newsResponse)

                Log.d("getNewsSuccess", "News Data: ${newsResponse}")
            } catch (e: Exception) {
                Log.e("getNewsError", "API call failed", e)
                _getNewsState.value = Result.Error("Unexpected Error: ${e.message}")
            }
        }
    }
    private fun getSources() {
        viewModelScope.launch {
            try {
                val sourcesResponse = getSourcesUseCase.invoke()
                _getSourcesState.value = Result.Success(sourcesResponse)

                Log.d("getSourcesSuccess", "News sources: ${sourcesResponse}")
            } catch (e: Exception) {
                Log.e("getSourcesError", "API call failed", e)
                _getSourcesState.value = Result.Error("Unexpected Error: ${e.message}")
            }
        }
    }
}


