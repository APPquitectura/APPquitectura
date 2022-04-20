package com.etsisi.appquitectura.presentation.ui.main.home.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class ItemHome(
    @StringRes
    val title: Int,
    @DrawableRes
    val icon: Int,
    val action: ItemHomeAction
)

enum class ItemHomeAction { PROFILE, START_GAME, RANKING, CONFIGURATION, ANALYTICS, ABOUT }