package com.etsisi.appquitectura.presentation.ui.main.game.viewmodel

import android.content.Context
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bluehomestudio.luckywheel.WheelItem
import com.etsisi.appquitectura.R
import com.etsisi.appquitectura.data.helper.PreferencesHelper
import com.etsisi.appquitectura.data.model.enums.PreferenceKeys
import com.etsisi.appquitectura.domain.enums.QuestionTopic
import com.etsisi.appquitectura.domain.enums.RankingType
import com.etsisi.appquitectura.domain.model.CurrentUser
import com.etsisi.appquitectura.domain.model.UserBO
import com.etsisi.appquitectura.domain.model.UserGameScoreBO
import com.etsisi.appquitectura.domain.usecase.GetQuestionTopicsUseCase
import com.etsisi.appquitectura.domain.usecase.GetWeeklyQuestionTopicUseCase
import com.etsisi.appquitectura.domain.usecase.UpdateRankingPointsUseCase
import com.etsisi.appquitectura.domain.usecase.UpdateUserDetailsUseCase
import com.etsisi.appquitectura.presentation.ui.main.game.model.ItemRouletteType
import com.etsisi.appquitectura.presentation.ui.main.game.model.ItemRoulette

class ResultViewModel(
    private val getQuestionTopicsUseCase: GetQuestionTopicsUseCase,
    private val getWeeklyQuestionTopicUseCase: GetWeeklyQuestionTopicUseCase,
    private val updateUserDetailsUseCase: UpdateUserDetailsUseCase,
    private val updateRankingPointsUseCase: UpdateRankingPointsUseCase
) : ViewModel() {

    private val _result = MutableLiveData<UserGameScoreBO>()
    val result: LiveData<UserGameScoreBO>
        get() = _result

    private val _regard = MutableLiveData<Pair<ItemRouletteType, Int>>()
    val regard: LiveData<Pair<ItemRouletteType, Int>>
        get() = _regard

    private val _showRoulette = MutableLiveData<Boolean>()
    val showRoulette: LiveData<Boolean>
        get() = _showRoulette

    private val rouletteItems = mutableListOf<ItemRoulette>()
    private val userGameScore = PreferencesHelper.readObject<UserGameScoreBO>(PreferenceKeys.USER_SCORE)

    init {
        _showRoulette.value = userGameScore?.rankingType != RankingType.UNKOWN
    }

    fun getRouletteItems(context: Context): List<WheelItem> {
        with(context.resources) {
            rouletteItems.add(
                ItemRoulette(
                    points = 25,
                    backgroundColor = ContextCompat.getColor(context, R.color.bronze),
                    drawable = getDrawable(R.drawable.ic_coins, null).toBitmap(),
                    title = getString(R.string.roulette_item_points, 25),
                    type = ItemRouletteType.POINTS
                )
            )

            rouletteItems.add(
                ItemRoulette(
                    points = 50,
                    backgroundColor = ContextCompat.getColor(context, R.color.teal_200),
                    drawable = getDrawable(R.drawable.ic_trophy, null).toBitmap(),
                    title = getString(R.string.roulette_item_exp, 50),
                    type = ItemRouletteType.EXP
                )
            )


            rouletteItems.add(
                ItemRoulette(
                    points = 5,
                    backgroundColor = ContextCompat.getColor(context, R.color.primary_red),
                    drawable = getDrawable(R.drawable.ic_badge, null).toBitmap(),
                    title = getString(R.string.roulette_item_points, 5),
                    type = ItemRouletteType.POINTS
                )
            )

            rouletteItems.add(
                ItemRoulette(
                    points = 7,
                    backgroundColor = ContextCompat.getColor(context, R.color.primary_blue),
                    drawable = getDrawable(R.drawable.ic_about, null).toBitmap(),
                    title = getString(R.string.roulette_item_exp, 25),
                    type = ItemRouletteType.EXP
                )
            )

            rouletteItems.add(
                ItemRoulette(
                    points = 0,
                    backgroundColor = ContextCompat.getColor(context, R.color.primary_yellow),
                    drawable = getDrawable(R.drawable.ic_about, null).toBitmap(),
                    title = getString(R.string.generic_error_title),
                    type = ItemRouletteType.NONE
                )
            )
        }

        return rouletteItems.map { it.getWidgetItem() }
    }

    fun updateUserScore(itemRouletteIndex: Int) {
        userGameScore?.let { score ->
            val rouletteItemSelected = rouletteItems[itemRouletteIndex]
            _result.value = score

            _regard.value = Pair(rouletteItemSelected.type, rouletteItemSelected.points)
            updateUserDetailsUseCase.invoke(
                params = UpdateUserDetailsUseCase.Params(
                    id = CurrentUser.email,
                    field = mapOf(
                        UpdateUserDetailsUseCase.USER_FIELD.TOTAL_ANSWERS to score.userQuestions.size,
                        UpdateUserDetailsUseCase.USER_FIELD.TOTAL_CORRECT_ANSWERS to score.getAllCorrectAnswers().size,
                        UpdateUserDetailsUseCase.USER_FIELD.EXPERIENCE to score.getExperience().plus(rouletteItemSelected.points.takeIf { rouletteItemSelected.type == ItemRouletteType.EXP } ?: 0)
                    )
                )
            ) { userUpdated ->
                userUpdated?.let {
                    updateRanking(it)
                }
            }
        }
    }

    private fun updateRanking(userUpdated: UserBO) {
        userGameScore?.let { score ->
            val params = UpdateRankingPointsUseCase.Params(
                id = userUpdated.id,
                points = score.getRankingPoints(),
                rankingType = score.rankingType ?: RankingType.UNKOWN,
                weeklyTopic = getWeeklyTopic()
            )

            updateRankingPointsUseCase.invoke(
                params = params
            ) {
            }
        }
    }

    private fun getWeeklyTopic(): QuestionTopic {
        return getQuestionTopicsUseCase
            .invoke()
            .filter { it != QuestionTopic.UNKNOWN }
            .let {
                getWeeklyQuestionTopicUseCase.invoke(params = GetWeeklyQuestionTopicUseCase.Params(it))
            }
    }

}