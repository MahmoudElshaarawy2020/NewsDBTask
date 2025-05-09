package com.example.newsdbtask.ui.presentation.home

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.constants.Result
import com.example.data.response.ArticlesItem
import com.example.newsdbtask.R
import com.example.newsdbtask.ui.presentation.components.NewsCard
import com.example.newsdbtask.ui.presentation.favorite.FavoritesViewModel
import com.example.newsdbtask.ui.presentation.components.shimmer_loader.PlaceholderNewsCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel = hiltViewModel(),
    favoritesViewModel: FavoritesViewModel = hiltViewModel(),
    navController: NavController
) {
    val newsState by homeViewModel.newsState.collectAsState()
    val sourcesState = homeViewModel.getSourcesState.collectAsState()
    var isDropdownOpen by remember { mutableStateOf(false) }
    val context = LocalContext.current
    var selectedSource by remember { mutableStateOf("bbc-news") }
    val listState = rememberLazyListState()

    val shouldLoadMore = remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val totalItems = layoutInfo.totalItemsCount
            val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            lastVisibleItem >= totalItems - 5 && totalItems > 0
        }
    }

    LaunchedEffect(Unit) {
        homeViewModel.fetchNews(source = selectedSource, reset = true)
    }

    LaunchedEffect(shouldLoadMore.value) {
        if (shouldLoadMore.value) {
            homeViewModel.fetchNews(source = selectedSource, reset = false)
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Online News", color = colorResource(R.color.light_yellow)) },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = colorResource(R.color.light_purple)
            ),
            actions = {
                IconButton(onClick = { isDropdownOpen = !isDropdownOpen }) {
                    Icon(
                        painter = painterResource(R.drawable.filter),
                        contentDescription = "Filter"
                    )
                }

                DropdownMenu(
                    expanded = isDropdownOpen,
                    onDismissRequest = { isDropdownOpen = false },
                ) {
                    when (val result = sourcesState.value) {
                        is Result.Loading -> {
                            DropdownMenuItem(
                                text = { Text("Loading sources...") },
                                onClick = {}
                            )
                        }

                        is Result.Success -> {
                            val sources = result.data?.sources ?: emptyList()
                            sources.forEach { source ->
                                DropdownMenuItem(
                                    text = { Text(source.name ?: "No name") },
                                    onClick = {
                                        source.id?.let {
                                            selectedSource = it
                                            homeViewModel.fetchNews(source = it, reset = true)
                                            isDropdownOpen = false
                                            Toast.makeText(
                                                context,
                                                "Selected: ${source.name}",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                )
                            }
                        }

                        is Result.Error -> {
                            DropdownMenuItem(
                                text = { Text("Failed to load sources") },
                                onClick = {}
                            )
                        }

                        else -> {}
                    }
                }
            }
        )

        Box(
            modifier = Modifier
                .background(color = colorResource(R.color.light_yellow))
                .fillMaxSize()
        ) {
            when (newsState) {
                is Result.Loading -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(5) {
                            PlaceholderNewsCard()
                        }
                    }
                }

                is Result.Error -> {
                    val error = (newsState as Result.Error<List<ArticlesItem>>).message
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Error",
                            tint = Color.Red,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = error ?: "Unknown error occurred",
                            color = Color.Red,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }

                is Result.Success -> {

                    val newsList =
                        (newsState as Result.Success<List<ArticlesItem>>).data ?: emptyList()

                    if (newsList.isEmpty()) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = "Empty",
                                tint = Color.Gray,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                "No news available",
                                color = Color.Gray,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 8.dp),
                            state = listState,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(newsList) { article ->
                                val isFavorite =
                                    favoritesViewModel.favoritesMap[article.url] ?: false
                                NewsCard(
                                    isFavorite = isFavorite,
                                    url = article.urlToImage ?: "",
                                    title = article.title ?: "No Title",
                                    date = article.publishedAt ?: "Unknown Date",
                                    onFavoriteClick = { favoritesViewModel.toggleFavorite(article) }
                                )
                            }
                        }
                    }
                }

                is Result.Idle -> TODO()
            }
        }
    }
}
