package com.example.newsdbtask.ui.presentation.components.chart

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.ColorTemplate

@Composable
fun ArticleSourceBarChart(
    articleCountsBySource: Map<String, Int>,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val sortedEntries = articleCountsBySource.entries.sortedByDescending { it.value }
    val labels = sortedEntries.map { it.key }
    val values = sortedEntries.mapIndexed { index, entry ->
        BarEntry(index.toFloat() + 0.1f, entry.value.toFloat())
    }

    val barDataSet = remember {
        BarDataSet(values, "Articles by Source").apply {
            setColors(*ColorTemplate.MATERIAL_COLORS)
            valueTextSize = 12f
            setDrawValues(true)
        }
    }

    val barData = remember {
        BarData(barDataSet).apply {
            barWidth = 0.3f
        }
    }
Column(
    modifier = modifier
        .fillMaxSize()
        .padding(bottom = 32.dp)
        .verticalScroll(rememberScrollState())
        .navigationBarsPadding(),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(rememberScrollState())
    ) {
        AndroidView(
            modifier = Modifier
                .width((labels.size * 130).dp)
                .heightIn(min = 300.dp, max = 500.dp),
            factory = {
                BarChart(context).apply {
                    this.data = barData

                    xAxis.apply {
                        valueFormatter = IndexAxisValueFormatter(labels)
                        position = XAxis.XAxisPosition.BOTTOM
                        granularity = 1f
                        setDrawGridLines(true)
                        setDrawAxisLine(true)
                        setDrawLabels(true)
                        labelRotationAngle = -45f
                        textSize = 9f
                        setLabelCount(labels.size)
                        setCenterAxisLabels(false)
                        setAvoidFirstLastClipping(false)

                        yOffset = 10f
                        xOffset = 2f
                    }

                    axisLeft.apply {
                        axisMinimum = 0f
                        setDrawLabels(true)
                        setDrawAxisLine(true)
                        setDrawGridLines(true)
                        textSize = 12f
                    }

                    axisRight.isEnabled = false
                    description.isEnabled = false
                    legend.isEnabled = false
                    setFitBars(true)

                    setVisibleXRangeMaximum(labels.size.toFloat())
                    moveViewToX(0f)
                    setScaleEnabled(true)
                    setPinchZoom(true)

                    extraLeftOffset = 15f
                    extraRightOffset = (labels.size * 2).toFloat()

                    animateY(1500)
                }
            },
            update = {
                it.data = barData
                it.invalidate()
            }
        )
    }
}

}
