package com.example.newsdbtask.ui.presentation.favorite

import android.util.Log
import androidx.compose.runtime.mutableStateMapOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.response.ArticlesItem
import com.example.data.response.Source
import com.example.domain.use_case.AddToFavoritesUseCase
import com.example.domain.use_case.GetFavoritesUseCase
import com.example.domain.use_case.IsFavoriteUseCase
import com.example.domain.use_case.RemoveFromFavoritesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val addToFavoritesUseCase: AddToFavoritesUseCase,
    private val removeFromFavoritesUseCase: RemoveFromFavoritesUseCase,
    private val getFavoritesUseCase: GetFavoritesUseCase,
    private val isFavoriteUseCase: IsFavoriteUseCase
) : ViewModel() {

    private val _favorites = MutableStateFlow<List<ArticlesItem>>(emptyList())
    val favorites: StateFlow<List<ArticlesItem>> = _favorites

    private val _favoritesMap = mutableStateMapOf<String, Boolean>()
    val favoritesMap: Map<String, Boolean> get() = _favoritesMap

    init {
        loadFavorites()
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
                    favorites.forEach {
                        _favoritesMap[it.url] = true
                    }
                }
            } catch (e: Exception) {
                Log.e("FavoritesViewModel", "Error loading favorites", e)
            }
        }
    }

    suspend fun isFavorite(url: String): Boolean {
        return _favoritesMap[url] ?: isFavoriteUseCase(url).also {
            _favoritesMap[url] = it
        }
    }
}
