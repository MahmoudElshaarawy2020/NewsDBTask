package com.example.newsdbtask.ui.presentation.home

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import com.example.constants.Result
import com.example.newsdbtask.R
import com.example.newsdbtask.ui.presentation.components.NewsCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val newsState = viewModel.getNewsState.collectAsState()
    val sourcesState = viewModel.getSourcesState.collectAsState()

    var isDropdownOpen by remember { mutableStateOf(false) }
    var selectedSource by remember { mutableStateOf("abc-news") }
    val context = LocalContext.current

    LaunchedEffect(selectedSource) {
            viewModel.getAllNews(selectedSource)
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

                // Dropdown Menu for sources
                DropdownMenu(
                    expanded = isDropdownOpen,
                    onDismissRequest = { isDropdownOpen = false },
                ) {
                    // Safely handling the sources list
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
                                        selectedSource = source.name ?: "Unknown"
                                        isDropdownOpen = false

                                        Toast.makeText(
                                            context,
                                            "Selected Source: ${selectedSource}",
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

        when (val result = newsState.value) {
            is Result.Loading -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "Loading...")
                }
            }

            is Result.Success -> {
                val news = result.data
                if (news?.articles.isNullOrEmpty()) {
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
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(news?.articles.orEmpty().filterNotNull()) { article ->
                            NewsCard(
                                url = article.urlToImage ?: "",
                                title = article.title ?: "No Title",
                                date = article.publishedAt ?: "Unknown Date",
                            )
                        }
                    }
                }
            }

            is Result.Error -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "Error: ${result.message ?: "Unknown Error"}")
                }
            }

            is Result.Idle -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "Waiting for data...")
                }
            }
        }
    }
}
