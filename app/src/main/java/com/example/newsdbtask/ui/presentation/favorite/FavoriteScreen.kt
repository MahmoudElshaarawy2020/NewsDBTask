package com.example.newsdbtask.ui.presentation.favorite

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.newsdbtask.R
import com.example.newsdbtask.ui.presentation.components.NewsCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteScreen(
    modifier: Modifier = Modifier,
    viewModel: FavoritesViewModel = hiltViewModel(),
) {
    val favorites by viewModel.favorites.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = colorResource(R.color.light_yellow))
    ) {
        TopAppBar(
            title = { Text(
                "Bookmarks",
                color = colorResource(R.color.light_yellow)
            ) },
            colors = TopAppBarDefaults.smallTopAppBarColors(
                containerColor = colorResource(R.color.light_purple)
            )
        )

        when {
            favorites.isEmpty() -> {
                Column(
                    modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "No Bookmarks yet",
                        color = Color.Gray
                    )

                }
            }

            else -> {

                LazyColumn(
                    modifier = Modifier
                        .background(color = colorResource(R.color.light_yellow))
                        .fillMaxSize()
                        .padding(horizontal = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {


                    items(favorites) { article ->
                        val isFavorite = viewModel.favoritesMap[article.url] ?: false

                        NewsCard(
                            isFavorite = isFavorite,
                            url = article.urlToImage ?: "",
                            title = article.title ?: "No Title",
                            date = article.publishedAt ?: "Unknown Date",
                            onFavoriteClick = {
                                viewModel.toggleFavorite(article)
                            }
                        )
                    }
                }
            }

        }
    }
}
