package com.devautro.financetracker.feature_statistics.presentation.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.yml.charts.axis.AxisData
import co.yml.charts.ui.barchart.GroupBarChart
import co.yml.charts.ui.barchart.models.BarPlotData
import co.yml.charts.ui.barchart.models.BarStyle
import co.yml.charts.ui.barchart.models.GroupBar
import co.yml.charts.ui.barchart.models.GroupBarChartData
import co.yml.charts.ui.barchart.models.GroupSeparatorConfig
import co.yml.charts.ui.barchart.models.SelectionHighlightData
import com.devautro.financetracker.R
import com.devautro.financetracker.feature_statistics.util.formatNumber
import com.devautro.financetracker.ui.theme.DarkestColor
import com.devautro.financetracker.ui.theme.ExpenseRed
import com.devautro.financetracker.ui.theme.IncomeGreen

@Composable
fun Chart(
    modifier: Modifier = Modifier,
    groupedBars: List<GroupBar>,
    maxAmount: Double
) {
    val xAxisData = AxisData.Builder()
        .startDrawPadding(42.dp)
        .axisStepSize(50.dp)
        .backgroundColor(MaterialTheme.colorScheme.primaryContainer)
        .steps(groupedBars.size - 1)
        .labelData { i -> groupedBars[i].label }
        .labelAndAxisLinePadding(10.dp)
        .axisLabelFontSize(12.sp)
        .axisLabelColor(DarkestColor)
        .axisLineColor(DarkestColor)
        .shouldDrawAxisLineTillEnd(true)
        .build()

    val yAxisData = AxisData.Builder()
        .topPadding(42.dp)
        .steps(4)
        .backgroundColor(MaterialTheme.colorScheme.primaryContainer)
        .labelData { i ->
            val yScale = maxAmount / 4
            formatNumber(i * yScale)
        }
        .labelAndAxisLinePadding(42.dp)
        .axisLabelFontSize(12.sp)
        .axisLabelColor(DarkestColor)
        .axisLineColor(DarkestColor)
        .build()

    val sum = stringResource(id = R.string.sum)

    val barPlotData = BarPlotData(
        groupBarList = groupedBars,
        barColorPaletteList = listOf(IncomeGreen, ExpenseRed),
        barStyle = BarStyle(
            isGradientEnabled = true,
            paddingBetweenBars = 42.dp,
            selectionHighlightData = SelectionHighlightData(
                groupBarPopUpLabel = { _, y ->
                    val amount = formatNumber(y.toDouble())
                    "$sum $amount"
                }
            )
        )
    )

    val groupBarChartData = GroupBarChartData(
        barPlotData = barPlotData,
        xAxisData = xAxisData,
        yAxisData = yAxisData,
        backgroundColor = MaterialTheme.colorScheme.primaryContainer,
        groupSeparatorConfig = GroupSeparatorConfig(showSeparator = false)
    )

    GroupBarChart(modifier = modifier, groupBarChartData = groupBarChartData)
}