package com.etsisi.appquitectura.domain.enums

sealed class GameType {
    object WeeklyGame: GameType()
    class ClassicGame(val classicType: ClassicGameType): GameType()
    class TestGame(val numberOfQuestions: Int, val questionTopics: List<QuestionTopic>): GameType()
}