package com.etsisi.appquitectura.presentation.common

import com.etsisi.appquitectura.domain.model.AnswerBO
import com.etsisi.appquitectura.domain.model.QuestionBO
import com.etsisi.appquitectura.presentation.ui.main.home.model.ItemHome
import com.etsisi.appquitectura.presentation.ui.main.settings.model.ItemSettings

interface DialogListener {
    fun onPositiveButtonClicked()
    fun onNegativeButtonClicked()
}

interface GoogleSignInListener {
    fun initSignInGoogle()
}

fun interface HomeItemClicked {
    fun onMenuItemClicked(item: ItemHome)
}

fun interface SettingsItemClicked {
    fun onSettingsItemClicked(item: ItemSettings)
}

interface GameListener {
    fun onAnswerClicked(question: QuestionBO, answer: AnswerBO, points: Long, userMarkInMillis: Long)
    fun onGameModeSelected(gameModeIndex: Int, totalQuestions: Int = 0)
}

interface QuestionListener {
    fun onAnswerClicked(question: QuestionBO, answer: AnswerBO)
}

