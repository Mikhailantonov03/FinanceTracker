package com.devautro.financetracker.core.util

import com.devautro.financetracker.R
import com.devautro.financetracker.feature_settings.presentation.FlagItem
import com.devautro.financetracker.ui.theme.GreenAccentCard
import com.devautro.financetracker.ui.theme.GreenPaleCard
import com.devautro.financetracker.ui.theme.OrangeAccentCard
import com.devautro.financetracker.ui.theme.OrangePaleCard
import com.devautro.financetracker.ui.theme.PinkAccentCard
import com.devautro.financetracker.ui.theme.PinkPaleCard
import com.devautro.financetracker.ui.theme.PurpleAccentCard
import com.devautro.financetracker.ui.theme.PurplePaleCard

object Const {

    val months = mapOf(
        R.string.january to "January",
        R.string.february to "February",
        R.string.march to "March",
        R.string.april to "April",
        R.string.may to "May",
        R.string.june to "June",
        R.string.july to "July",
        R.string.august to "August",
        R.string.september to "September",
        R.string.october to "October",
        R.string.november to "November",
        R.string.december to "December"
    )

    fun getResourceIdByEnglishMonth(monthTag: String): Int? {
        return months.entries.find { it.value == monthTag }?.key
    }

    val filterTags = listOf(R.string.week, R.string.month, R.string.all)

    val sourcePaleColors = listOf(OrangePaleCard, PinkPaleCard, GreenPaleCard, PurplePaleCard)
    val sourceAccentColors = listOf(OrangeAccentCard, PinkAccentCard, GreenAccentCard, PurpleAccentCard)

    val flags = listOf(
        FlagItem(languageName = R.string.english, imageResource = R.drawable.gb, localeLanguage = "en"),
        FlagItem(languageName = R.string.spanish, imageResource = R.drawable.es, localeLanguage = "es"),
        FlagItem(languageName = R.string.russian, imageResource = R.drawable.ru, localeLanguage = "ru")
    )

}