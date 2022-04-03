package com.etsisi.appquitectura.presentation.ui.main.model

import androidx.annotation.StringRes

data class ItemGameMode(
    @StringRes
    val title: Int,
    val action: ItemGameModeAction
)

enum class ItemGameModeAction { UNKNOWN, THIRTY_QUESTIONS, SIXTY_QUESTIONS }