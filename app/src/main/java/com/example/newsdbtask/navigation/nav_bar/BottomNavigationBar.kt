package com.example.newsdbtask.navigation.nav_bar

import android.graphics.PointF
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.updateTransition
import androidx.compose.runtime.getValue
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.rotate
import com.example.newsdbtask.R
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.sin

@Composable
fun BottomNavigationBar(
    items: List<BottomNavItem>,
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit
) {
    val transition = updateTransition(targetState = selectedIndex, label = "nav_transition")
    val indicatorOffsetX by transition.animateDp(label = "indicatorOffsetX") { index ->
        val screenWidth = LocalConfiguration.current.screenWidthDp.dp
        val itemWidth = screenWidth / items.size
        val indicatorWidth = 50.dp
        (itemWidth * index) + (itemWidth - indicatorWidth) / 2
    }

    val LightGreen2 = Color(0xFFEEF1DA)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(Color.Transparent)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val cornerRadius = 20.dp.toPx()

            drawRoundRect(
                color = Color.White,
                topLeft = Offset(0f, 0f),
                size = Size(size.width, size.height),
                cornerRadius = CornerRadius(cornerRadius, cornerRadius)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEachIndexed { index, item ->
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.size(60.dp)
                ) {
                    if (selectedIndex != index) {
                        IconButton(
                            onClick = { onItemSelected(index) },
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = item.icon),
                                contentDescription = item.label,
                                modifier = Modifier.size(24.dp),
                                tint = Color.Gray
                            )
                        }
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .offset(x = indicatorOffsetX, y = (-12).dp)
                .size(60.dp)
                .zIndex(1f),
            contentAlignment = Alignment.TopCenter
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val radius = size.minDimension * 0.6f
                val bottomCornerRadius = radius * 1.1f
                val sideCornerRadius = radius * 0.02f

                val trianglePath = createRoundedTrianglePath(
                    centerX = size.width / 2f,
                    centerY = size.height / 2f,
                    radius = radius,
                    bottomCornerRadius = bottomCornerRadius,
                    sideCornerRadius = sideCornerRadius
                )

                rotate(degrees = -60f) {
                    drawPath(
                        path = trianglePath,
                        color = LightGreen2,
                        style = Fill
                    )
                }
            }


            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(colorResource(id = R.color.light_green2), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = items[selectedIndex].icon),
                    contentDescription = items[selectedIndex].label,
                    modifier = Modifier.size(28.dp),
                    tint = Color.White
                )
            }
        }
    }
}


fun createRoundedTrianglePath(
    centerX: Float,
    centerY: Float,
    radius: Float,
    bottomCornerRadius: Float,
    sideCornerRadius: Float
): Path {
    val path = Path()

    val points = List(3) { i ->
        val angle = Math.toRadians((i * 120 - 90).toDouble())
        Offset(
            x = centerX + cos(angle).toFloat() * radius,
            y = centerY + sin(angle).toFloat() * radius
        )
    }

    val next = { i: Int -> points[(i + 1) % 3] }
    val prev = { i: Int -> points[(i + 2) % 3] }

    for (i in points.indices) {
        val p0 = prev(i)
        val p1 = points[i]
        val p2 = next(i)

        // Directions
        val dir0 = (p0 - p1).normalize()
        val dir2 = (p2 - p1).normalize()

        val cornerRadius = when(i) {
            2 -> bottomCornerRadius
            else -> sideCornerRadius
        }

        val offset0 = p1 + dir0 * cornerRadius
        val offset2 = p1 + dir2 * cornerRadius

        if (i == 0)
            path.moveTo(offset0.x, offset0.y)
        else
            path.lineTo(offset0.x, offset0.y)

        path.quadraticBezierTo(
            p1.x, p1.y,
            offset2.x, offset2.y
        )
    }

    path.close()
    return path
}

private operator fun Offset.minus(other: Offset): Offset =
    Offset(this.x - other.x, this.y - other.y)

private operator fun Offset.plus(other: Offset): Offset =
    Offset(this.x + other.x, this.y + other.y)

private operator fun Offset.times(scalar: Float): Offset =
    Offset(this.x * scalar, this.y * scalar)

private fun Offset.normalize(): Offset {
    val length = hypot(x.toDouble(), y.toDouble()).toFloat().coerceAtLeast(0.0001f)
    return Offset(x / length, y / length)
}



