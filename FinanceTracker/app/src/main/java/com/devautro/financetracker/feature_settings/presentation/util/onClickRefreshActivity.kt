package com.devautro.financetracker.feature_settings.presentation.util

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat


fun onClickRefreshActivity(context: Context, locale: String) {
    context.findActivity()?.runOnUiThread {
        val appLocale = LocaleListCompat.forLanguageTags(locale)
        AppCompatDelegate.setApplicationLocales(appLocale)
    }
}