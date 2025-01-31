package com.devautro.financetracker.core.presentation.navigation

import androidx.annotation.ColorInt
import kotlinx.serialization.Serializable

@Serializable
object Home


@Serializable
object Incomes

@Serializable
object Expenses

@Serializable
object Charts

@Serializable
object Accounts

@Serializable
object AddAccount

@Serializable
data class EditAccount(val id: Long, @ColorInt val paleColor: Int)

@Serializable
object Settings
