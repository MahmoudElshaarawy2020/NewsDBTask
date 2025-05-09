package com.example.newsdbtask.ui.presentation.chart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import com.example.newsdbtask.R
import com.example.newsdbtask.ui.presentation.components.chart.ArticleSourceBarChart

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChartScreen(
    modifier: Modifier = Modifier
) {
    val articleCounts = mapOf(
        "CNN" to 14,
        "BBC" to 10,
        "Reuters" to 22,
        "Al Jazeera" to 8,
        "NY Times" to 18,
        "Fox News" to 12,
        "The Guardian" to 6,
        "The Hindu" to 16,
        "ABC News" to 32,
        "Bloomberg" to 28,
        "BBC Sport" to 14,
        "The Washington Post" to 10,
        "ANSA.it" to 15,
        "Ary news" to 10,
        "Blasting news" to 14,
        "Business Insider" to 12
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = colorResource(R.color.light_yellow)),
    ) {
        TopAppBar(
            title = {
                Text(
                    "Analytics",
                    color = colorResource(id = R.color.light_yellow)
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = colorResource(id = R.color.light_purple)
            )
        )
        Box(
            modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {

            ArticleSourceBarChart(articleCounts)
        }
    }

}