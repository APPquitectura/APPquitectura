package com.etsisi.appquitectura.presentation.ui.main.game.model

import com.etsisi.appquitectura.domain.model.QuestionTopic

data class ItemGameMode(
    val action: ItemGameModeAction
)

sealed class ItemGameModeAction {
    object WeeklyGame: ItemGameModeAction()
    class ClassicGame(val classicType: ClassicGameMode): ItemGameModeAction()
    class TestGame(val numberOfQuestions: Int, val questionTopics: List<QuestionTopic>): ItemGameModeAction()
}

enum class ClassicGameMode(val numberOfQuestions: Int) {
    TWENTY_QUESTIONS(20),
    FORTY_QUESTIONS(40)
}