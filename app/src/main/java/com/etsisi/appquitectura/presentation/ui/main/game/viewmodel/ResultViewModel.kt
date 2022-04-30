package com.etsisi.appquitectura.presentation.ui.main.game.viewmodel

import android.content.Context
import android.content.res.Resources
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bluehomestudio.luckywheel.WheelItem
import com.etsisi.appquitectura.R
import com.etsisi.appquitectura.domain.model.UserGameScoreBO
import com.etsisi.appquitectura.domain.usecase.FetchScoresReferenceUseCase
import com.etsisi.appquitectura.domain.usecase.UpdateUserDetailsUseCase
import com.etsisi.appquitectura.presentation.ui.main.game.model.ItemRoulette

class ResultViewModel(
    private val updateUserDetailsUseCase: UpdateUserDetailsUseCase,
    private val fetchScoresReferenceUseCase: FetchScoresReferenceUseCase
): ViewModel() {

    val rouletteItems = mutableListOf<ItemRoulette>()

    private val _correctQuestions = MutableLiveData<String>()
    val correctQuestions: LiveData<String>
        get() = _correctQuestions

    private val _timeAverage = MutableLiveData<String>()
    val timeAverage: LiveData<String>
        get() = _timeAverage

    init {
        fetchScoresReference()
    }

    fun getRouletteItems(context: Context): List<WheelItem> {
        with(context.resources) {
            rouletteItems.add(ItemRoulette(
                points = 10,
                backgroundColor = ContextCompat.getColor(context, R.color.bronze),
                drawable = getDrawable(R.drawable.ic_coins, null).toBitmap(),
                title = getString(R.string.roulette_item_1)))

            rouletteItems.add(ItemRoulette(
                points = 4,
                backgroundColor = ContextCompat.getColor(context, R.color.teal_200),
                drawable = getDrawable(R.drawable.ic_trophy, null).toBitmap(),
                title =   getString(R.string.roulette_item_2)))


            rouletteItems.add(
                ItemRoulette(
                points = 0,
                backgroundColor = ContextCompat.getColor(context, R.color.primary_red),
                drawable = getDrawable(R.drawable.ic_badge, null).toBitmap(),
                title = getString(R.string.roulette_item_4))
            )

            rouletteItems.add(ItemRoulette(
                points = 7,
                backgroundColor = ContextCompat.getColor(context, R.color.primary_blue),
                drawable = getDrawable(R.drawable.ic_about, null).toBitmap(),
                title = getString(R.string.roulette_item_1))
            )

            rouletteItems.add(
                ItemRoulette(
                points = 8,
                backgroundColor = ContextCompat.getColor(context, R.color.primary_yellow),
                drawable = getDrawable(R.drawable.ic_about, null).toBitmap(),
                title = getString(R.string.roulette_item_1))
            )
        }

        return rouletteItems.map { it.getWidgetItem() }
    }

    fun setUserScore(itemRouletteIndex: Int, gameScoreBO: UserGameScoreBO) {
        _timeAverage.value = gameScoreBO.averageUserMillisToAnswer.toString()
        _correctQuestions.value = gameScoreBO.getAllCorrectAnswers().size.toString()
        updateUserDetailsUseCase.invoke(
            params = UpdateUserDetailsUseCase.Params(
                mapOf(
                    UpdateUserDetailsUseCase.USER_FIELD.SCORE_ACCUM to rouletteItems[itemRouletteIndex].points,
                    UpdateUserDetailsUseCase.USER_FIELD.TOTAL_ANSWERS to gameScoreBO.userQuestions.size,
                    UpdateUserDetailsUseCase.USER_FIELD.TOTAL_CORRECT_ANSWERS to gameScoreBO.getAllCorrectAnswers().size
                )
            )
        ) {

        }
    }

    private fun fetchScoresReference() {
        fetchScoresReferenceUseCase.invoke(
            scope = viewModelScope,
            params = Unit,
            onResult = {
                if (it.isNotEmpty()) {

                }
            }
        )
    }
}