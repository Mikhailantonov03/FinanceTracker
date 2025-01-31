package com.devautro.financetracker.core.util

import android.content.Context
import androidx.annotation.StringRes

sealed class UiText {
    data class DynamicString(val value: String) : UiText()
    data class StringResource(
        @StringRes val id: Int,
        val arg1: Any? = null,
        val arg2: Any? = null
    ) : UiText()

    fun asString(context: Context): String {
        return when(this) {
            is DynamicString -> value
            is StringResource -> context.getString(id, *arrayOf(arg1, arg2).filterNotNull().toTypedArray())
        }
    }
}