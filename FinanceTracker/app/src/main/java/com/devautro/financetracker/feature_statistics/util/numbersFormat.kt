package com.devautro.financetracker.feature_statistics.util

import java.text.DecimalFormat
import kotlin.math.floor
import kotlin.math.log10
import kotlin.math.pow

fun formatNumber(number: Double, currency: String = ""): String {
    val suffix = charArrayOf(' ', 'k', 'M', 'B', 'T', 'P', 'E')
    val value = floor(log10(number)).toInt()
    val base = value / 3
    val result = if (value >= 3 && base < suffix.size) {
        DecimalFormat("#.#").format(
            number / 10.0.pow((base * 3).toDouble())
        ) + suffix[base]
    } else {
        DecimalFormat("#,##0").format(number)
    }
    return "$currency$result"
}