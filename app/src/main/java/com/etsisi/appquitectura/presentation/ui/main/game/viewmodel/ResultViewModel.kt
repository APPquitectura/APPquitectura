package com.etsisi.appquitectura.presentation.ui.main.game.viewmodel

import android.content.res.Resources
import android.graphics.Color
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.ViewModel
import com.bluehomestudio.luckywheel.WheelItem
import com.etsisi.appquitectura.R
import com.etsisi.appquitectura.domain.usecase.UpdateUserDetailsUseCase
import com.etsisi.appquitectura.presentation.ui.main.game.model.ItemRoulette

class ResultViewModel(
    private val updateUserDetailsUseCase: UpdateUserDetailsUseCase
): ViewModel() {

    val rouletteItems = mutableListOf<ItemRoulette>()

    fun getRouletteItems(resources: Resources): List<WheelItem> {
        with(resources) {
            rouletteItems.add(ItemRoulette(
                points = 10,
                backgroundColor = Color.LTGRAY,
                drawable = getDrawable(R.drawable.ic_about, null).toBitmap(),
                title = getString(R.string.roulette_item_1)))

            rouletteItems.add(ItemRoulette(
                points = 4,
                backgroundColor = Color.BLUE,
                drawable = getDrawable(R.drawable.ic_about, null).toBitmap(),
                title =   getString(R.string.roulette_item_1)))

            rouletteItems.add(
                ItemRoulette(
                points = 5,
                backgroundColor = Color.BLACK,
                drawable = getDrawable(R.drawable.ic_about, null).toBitmap(),
                title = getString(R.string.roulette_item_1))
            )

            rouletteItems.add(
                ItemRoulette(
                points = 2,
                backgroundColor = Color.GRAY,
                drawable = getDrawable(R.drawable.ic_about, null).toBitmap(),
                title = getString(R.string.roulette_item_1))
            )

            rouletteItems.add(ItemRoulette(
                points = 7,
                backgroundColor = Color.RED,
                drawable = getDrawable(R.drawable.ic_about, null).toBitmap(),
                title = getString(R.string.roulette_item_1))
            )

            rouletteItems.add(
                ItemRoulette(
                points = 8,
                backgroundColor = Color.YELLOW,
                drawable = getDrawable(R.drawable.ic_about, null).toBitmap(),
                title = getString(R.string.roulette_item_1))
            )
        }

        return rouletteItems.map { it.getWidgetItem() }
    }

    fun updateUserScore(itemRouletteIndex: Int) {
        updateUserDetailsUseCase.invoke(
            params = UpdateUserDetailsUseCase.Params(
                Pair(UpdateUserDetailsUseCase.USER_FIELD.SCORE, rouletteItems[itemRouletteIndex].points)
            )
        ) {

        }
    }
}