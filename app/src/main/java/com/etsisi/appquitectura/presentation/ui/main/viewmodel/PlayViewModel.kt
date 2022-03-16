package com.etsisi.appquitectura.presentation.ui.main.viewmodel

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import com.etsisi.appquitectura.R
import com.etsisi.appquitectura.presentation.ui.main.model.ItemGameMode
import com.etsisi.appquitectura.presentation.ui.main.model.ItemGameModeAction

class PlayViewModel: ViewModel(), LifecycleObserver {
    val gameModes = listOf(
        ItemGameMode(R.string.game_mode_thirty, ItemGameModeAction.THIRTY_QUESTIONS),
        ItemGameMode(R.string.game_mode_sixty, ItemGameModeAction.SIXTY_QUESTIONS)
    )
}