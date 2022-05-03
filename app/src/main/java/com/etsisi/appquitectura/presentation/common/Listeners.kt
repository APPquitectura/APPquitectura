package com.etsisi.appquitectura.presentation.common

import com.etsisi.appquitectura.domain.model.AnswerBO
import com.etsisi.appquitectura.domain.model.QuestionBO
import com.etsisi.appquitectura.presentation.ui.main.game.model.ItemGameMode
import com.etsisi.appquitectura.presentation.ui.main.home.model.ItemHome
import com.etsisi.appquitectura.presentation.ui.main.settings.model.ItemSettings

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
}

interface QuestionListener {
    fun onAnswerClicked(question: QuestionBO, answer: AnswerBO)
}

interface PlayFragmentListener {
    fun onGameMode(item: ItemGameMode)
}

interface DialogListener {
    fun onPositiveButtonClicked()
    fun onNegativeButtonClicked()
}

