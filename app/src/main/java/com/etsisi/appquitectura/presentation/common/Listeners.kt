package com.etsisi.appquitectura.presentation.common

import android.view.View
import com.etsisi.appquitectura.domain.enums.QuestionLevel
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

interface SettingsListener {
    fun onSettingsItemClicked(item: ItemSettings)
    fun onRepeatModeSwitch(enabled: Boolean)
    fun isRepeatModeEnabled(): Boolean
}

interface GameListener {
    fun onAnswerClicked(question: QuestionBO, answer: AnswerBO, points: Long, userMarkInMillis: Long)
    fun onGameModeSelected(gameModeIndex: Int, topicsIdSelected: IntArray?, levelSelected: QuestionLevel?)
}

interface QuestionListener {
    fun onAnswerClicked(question: QuestionBO, answer: AnswerBO)
}

interface StickyHeaderListener {
    fun getHeaderPositionForItem(itemPosition: Int): Int
    fun getHeaderLayout(headerPosition: Int): Int
    fun bindHeaderData(header: View?, headerPosition: Int)
    fun isHeader(itemPosition: Int): Boolean
}

