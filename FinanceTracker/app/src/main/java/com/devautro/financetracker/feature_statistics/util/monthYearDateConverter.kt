package com.devautro.financetracker.feature_statistics.util

import java.time.Instant
import java.time.YearMonth
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Locale

fun convertMillisToMonthYear(millis: Long): String {
    val instant = Instant.ofEpochMilli(millis)

    val localDate = instant.atZone(ZoneId.systemDefault()).toLocalDate()

    val formatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.US)

    return localDate.format(formatter)
}

fun convertMonthYearToMillis(monthYear: String): Long? {
    val formatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.US)

    return try {
        val yearMonth = YearMonth.parse(monthYear, formatter)

        val localDate = yearMonth.atDay(1)

        localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    } catch (e: DateTimeParseException) {
        println("MyLog : ${e.message}")
        null
    }
}
