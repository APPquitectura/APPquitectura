package com.etsisi.appquitectura.presentation.ui.main.ranking.model

import androidx.annotation.DrawableRes
import com.etsisi.appquitectura.presentation.ui.main.ranking.adapter.RankingViewType

data class ItemRanking(
    val name: String,
    val rankingPoints: Int,
    val position: Int,
    val viewType: RankingViewType,
    @DrawableRes val icon: Int? = null
)
