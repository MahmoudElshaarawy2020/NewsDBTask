package com.example.newsdbtask.ui.presentation.home

import android.nfc.tech.MifareUltralight.PAGE_SIZE
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.LoadState.Loading.endOfPaginationReached
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.constants.Result
import com.example.newsdbtask.R
import com.example.newsdbtask.ui.presentation.components.NewsCard

// HomeScreen.kt
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val newsState = viewModel.getPagingNewsState.collectAsLazyPagingItems()
    val sourcesState = viewModel.getSourcesState.collectAsState()
    var isDropdownOpen by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val selectedSource by remember { mutableStateOf("abc-news") }
    
    val listState = rememberLazyListState()

    // Detect when we reach the end of the list
    val shouldLoadMore = remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val totalItems = layoutInfo.totalItemsCount
            val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0

            // Load more when we're within 5 items of the end
            lastVisibleItem >= totalItems - 5 && totalItems > 0
        }
    }

    LaunchedEffect(shouldLoadMore.value) {
        viewModel.fetchNews(source = selectedSource, page = 1)
        if (shouldLoadMore.value) {
            viewModel.loadNextPage()
        }
    }


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
                                        viewModel.fetchNews(source = sourceId)
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

        if (newsState.itemCount == 0 && newsState.loadState.refresh !is LoadState.Loading) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "No news available")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .background(Color.White)
                    .fillMaxSize()
                    .padding(horizontal = 8.dp, vertical = 32.dp),
                state = listState,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(newsState.itemCount) { index ->
                    val article = newsState[index]
                    article?.let {
                        NewsCard(
                            url = it.urlToImage ?: "",
                            title = it.title ?: "No Title",
                            date = it.publishedAt ?: "Unknown Date",
                        )
                    }
                }

                // Show loading indicator at the bottom when loading more
                if (newsState.loadState.append is LoadState.Loading) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }
        }
    }
}