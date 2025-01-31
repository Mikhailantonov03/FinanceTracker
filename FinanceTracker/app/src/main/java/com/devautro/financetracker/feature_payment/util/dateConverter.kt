package com.devautro.financetracker.feature_payment.util

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date

fun convertMillisToDate(millis: Long): String {
    val localDate = Instant.ofEpochMilli(millis)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
    val formatter = DateTimeFormatter.ofPattern("dd.MM.yy")
    return localDate.format(formatter)
}

fun convertDateToMillis(dateString: String): Long {
    val formatter = DateTimeFormatter.ofPattern("dd.MM.yy")
    val localDate = LocalDate.parse(dateString, formatter)

    return localDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
}

fun getYearOfTheDate(date: Long): Int {
    val calendar = Calendar.getInstance()
    calendar.time = Date(date)
    return calendar.get(Calendar.YEAR)
}