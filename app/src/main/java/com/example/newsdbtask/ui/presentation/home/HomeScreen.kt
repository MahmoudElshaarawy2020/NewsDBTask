package com.example.newsdbtask.ui.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.constants.Result
import com.example.newsdbtask.ui.presentation.home.HomeViewModel

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val newsState = viewModel.getNewsState.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when (val result = newsState.value) {
            is Result.Loading -> {
                Text(text = "Loading...")
            }
            is Result.Success -> {
                val news = result.data
                if (news?.articles.isNullOrEmpty()) {
                    Text(text = "No news available")
                } else {
                    Text(text = "First News Title: ${news?.articles?.firstOrNull()?.title ?: "No title"}")
                }
            }
            is Result.Error -> {
                Text(text = "Error: ${result.message ?: "Unknown Error"}")
            }
            is Result.Idle -> {
                Text(text = "Waiting for data...")
            }
        }
    }
}
