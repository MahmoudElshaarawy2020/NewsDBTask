package com.example.newsdbtask.ui.presentation.more

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.example.newsdbtask.R
import com.example.newsdbtask.ui.presentation.components.animation.CustomRow
import com.example.newsdbtask.ui.presentation.components.animation.RotatingBorderProfileImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoreScreen(modifier: Modifier = Modifier) {
    val gradientBrush = Brush.verticalGradient(
        colors = listOf(
            colorResource(R.color.second_blue),
            colorResource(R.color.dark_purple),
            colorResource(R.color.dark_pink),
            colorResource(R.color.light_yellow),
            colorResource(R.color.light_yellow),
        )
    )
    val customFontFamily = FontFamily(
        Font(R.font.my_font)
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(brush = gradientBrush)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(brush = gradientBrush)
                .padding(top = 56.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.size(64.dp))

            RotatingBorderProfileImage(imageRes = R.drawable.franz_img)

            Spacer(modifier = Modifier.size(10.dp))
            CustomRow(text = "Your Profile", icon = R.drawable.man_ic)
            Spacer(modifier = Modifier.size(10.dp))
            CustomRow(text = "Settings", icon = R.drawable.settings_ic)
            Spacer(modifier = Modifier.size(10.dp))
            CustomRow(text = "Contact Us", icon = R.drawable.phone_ic)
            Spacer(modifier = Modifier.size(10.dp))
            CustomRow(text = "Privacy Policy", icon = R.drawable.privacy_ic)
        }

        TopAppBar(
            title = {
                Text(
                    text = "More",
                    color = colorResource(id = R.color.light_yellow))
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = colorResource(id = R.color.second_blue).copy(alpha = 0.2f)
            )
        )

//        SnowfallEffect()

    }
}





