package com.etsisi.appquitectura.presentation.ui.main.game.model

data class ItemGameMode(
    val action: ItemGameModeAction
)

enum class ItemGameModeAction(val totalQuestions: Int) {
    NONE(0),
    TWENTY_QUESTIONS(20),
    FORTY_QUESTIONS(40)
}