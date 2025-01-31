package com.devautro.financetracker.core.util

import java.util.Locale
import kotlin.math.abs

fun formatDoubleToString(value: Double, sign: String = ""): String {
    val isNegative = value < 0
    val absoluteValue = abs(value)

    val stringValue = String.format(Locale.US, "%.2f", absoluteValue)

    val (integerPart, decimalPart) = stringValue.split(".")

    val modifiedIntegerPart = integerPart.reversed().chunked(3).joinToString(" ").reversed()

    return if (isNegative) {
        "$sign-$modifiedIntegerPart.$decimalPart"
    } else {
        "$sign$modifiedIntegerPart.$decimalPart"
    }

}

fun formatStringToDouble(value: String): Double {
    val regex = Regex("^-?\\d+(\\.\\d{1,2})?$")
    if (!regex.matches(value)) {
        throw NumberFormatException("Invalid Input: only digits, point and up to 2 digits after allowed")
    }

    return value.toDouble()

}

fun String.isConvertibleToDouble(): Boolean {
    val regex = Regex("^-?\\d+(\\.\\d{1,2})?$")
    return regex.matches(this)
}