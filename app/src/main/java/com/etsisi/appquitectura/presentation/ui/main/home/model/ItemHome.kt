package com.etsisi.appquitectura.presentation.ui.main.home.model

import androidx.annotation.RawRes
import androidx.annotation.StringRes

data class ItemHome(
    @StringRes
    val title: Int,
    @RawRes
    val icon: Int,
    val action: ItemHomeAction
)

enum class ItemHomeAction { PROFILE, START_GAME, RANKING, CONFIGURATION, ANALYTICS, ABOUT }