package com.devautro.financetracker.feature_settings.data.data_source

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsPreferences(private val context: Context) {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PREFS_NAME)

    suspend fun changeCurrentTheme(isDarkTheme: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[booleanPreferencesKey(THEME_KEY)] = isDarkTheme
        }
    }

    fun getCurrentTheme(): Flow<Boolean> = context.dataStore.data.map { pref ->
        return@map pref[booleanPreferencesKey(THEME_KEY)] ?: true
    }

    suspend fun changeCurrency(sign: String) {
        context.dataStore.edit { prefs ->
            prefs[stringPreferencesKey(CURRENCY_KEY)] = sign
        }
    }

    fun getChosenCurrency(): Flow<String> = context.dataStore.data.map { pref ->
        return@map pref[stringPreferencesKey(CURRENCY_KEY)] ?: ""
    }

    companion object {
        const val PREFS_NAME = "settings_preferences"
        const val THEME_KEY = "get_theme"
        const val CURRENCY_KEY = "get_currency"
    }
}