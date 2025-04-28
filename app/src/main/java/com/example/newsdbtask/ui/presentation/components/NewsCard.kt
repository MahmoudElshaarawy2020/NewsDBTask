package com.example.newsdbtask.ui.presentation.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NewsCard(url: String, title: String, date: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp) // Outer padding
            .aspectRatio(18f / 12f) // Keep nice card proportion
            .clip(RoundedCornerShape(16.dp)) // Rounded corners
            .background(Color.LightGray) // Background behind image
            .shadow(8.dp, RoundedCornerShape(16.dp)) // Add soft shadow
    ) {
        AsyncImage(
            model = url,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Darker gradient overlay (looks better)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(65.dp)
                .align(Alignment.BottomStart)
                .background(
                    Color.Black.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)
                )
                .padding(horizontal = 8.dp, vertical = 8.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(
                    text = date,
                    color = Color.LightGray,
                    fontSize = 12.sp
                )
                Text(
                    text = title,
                    color = Color.White,
                    fontSize = 16.sp,
                    maxLines = 1,
                    softWrap = false,
                    overflow = TextOverflow.Visible,
                    modifier = Modifier
                        .fillMaxWidth()
                        .basicMarquee()
                )
            }
        }
    }
}


@Preview
@Composable
private fun NewCardPrev() {
    NewsCard(
        "https://www.google.com/imgres?q=images&imgurl=https%3A%2F%2Fimages.pexels.com%2Fphotos%2F414612%2Fpexels-photo-414612.jpeg%3Fcs%3Dsrgb%26dl%3Dpexels-souvenirpixels-414612.jpg%26fm%3Djpg&imgrefurl=https%3A%2F%2Fwww.pexels.com%2Fsearch%2Fnatural%2F&docid=dwm0OYbiSD8M7M&tbnid=oXTudgfT3pqXSM&vet=12ahUKEwj98PvEj_qMAxVPK_sDHQ9oLvgQM3oECBkQAA..i&w=5306&h=3770&hcb=2&itg=1&ved=2ahUKEwj98PvEj_qMAxVPK_sDHQ9oLvgQM3oECBkQAA",
        "Title",
        "Date"
    )
}

