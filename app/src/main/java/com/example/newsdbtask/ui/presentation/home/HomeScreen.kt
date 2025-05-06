package com.example.newsdbtask.ui.presentation.home

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.constants.Result
import com.example.newsdbtask.R
import com.example.newsdbtask.ui.presentation.components.NewsCard

// HomeScreen.kt
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    navController: NavController,
) {
    val newsList by viewModel.newsList.collectAsState()
    val sourcesState = viewModel.getSourcesState.collectAsState()
    var isDropdownOpen by remember { mutableStateOf(false) }
    val context = LocalContext.current
    var selectedSource by remember { mutableStateOf("bbc-news") }
    val isFavorite by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()
    var savedFirstVisibleItemIndex by remember { mutableStateOf(0) }
    var savedFirstVisibleItemScrollOffset by remember { mutableStateOf(0) }
    val isNavigated by viewModel.isNavigated.collectAsState()


    val shouldLoadMore = remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val totalItems = layoutInfo.totalItemsCount
            val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0

            // Load more when we're within 5 items of the end
            lastVisibleItem >= totalItems - 5 && totalItems > 0
        }
    }

    LaunchedEffect(Unit) {
        viewModel.fetchNews(source = selectedSource, reset = true)
        viewModel.setIsNavigated(false)
    }

    LaunchedEffect(shouldLoadMore.value) {
        if (shouldLoadMore.value) {
            viewModel.fetchNews(source = selectedSource, reset = false)
        }
    }

    //to reset the flag when navigating to another screen
//    LaunchedEffect(isNavigated) {
//        if (isNavigated) {
//            viewModel.setIsNavigated(false)
//        }
//    }


    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("News DB") },
            actions = {
                IconButton(onClick = { isDropdownOpen = !isDropdownOpen }) {
                    Icon(
                        modifier = Modifier.size(24.dp),
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
                                    text = { Text(text = source.name ?: "No name") },
                                    onClick = {
                                        val sourceId = source.id ?: return@DropdownMenuItem
                                        selectedSource = sourceId
                                        viewModel.fetchNews(source = sourceId, reset = true)
                                        isDropdownOpen = false
                                        Toast.makeText(
                                            context,
                                            "Selected Source: ${source.name}",
                                            Toast.LENGTH_SHORT
                                        ).show()
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

        LazyColumn(
            modifier = Modifier
                .background(Color.White)
                .fillMaxSize()
                .padding(horizontal = 8.dp, vertical = 8.dp),
            state = listState,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(newsList) { article ->
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
