package com.etsisi.appquitectura.presentation.ui.main.game.model

data class ItemGameMode(
    val action: ItemGameModeAction
)

sealed class ItemGameModeAction {
    object WeeklyGame: ItemGameModeAction()
    class ClassicGame(vararg modes: ClassicGameMode): ItemGameModeAction()
    class TestGame(val numberOfQuestions: Int, val questionTopics: QuestionTopics): ItemGameModeAction()
}

enum class ClassicGameMode(val numberOfQuestions: Int) {
    TWENTY_QUESTIONS(20),
    FORTY_QUESTIONS(40)
}