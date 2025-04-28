package com.example.newsdbtask.ui.presentation.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.constants.Result
import com.example.data.response.NewsResponse
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import com.example.domain.use_case.GetAllNewsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAllNewsUseCase: GetAllNewsUseCase
) : ViewModel() {

    private val _getNewsState = MutableStateFlow<Result<NewsResponse>>(Result.Loading())
    val getNewsState: MutableStateFlow<Result<NewsResponse>> get() = _getNewsState

    init {
        getAllNews()
    }

    private fun getAllNews(query: String = "bitcoin") {
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

}


