package com.etsisi.appquitectura.presentation.ui.main.ranking.model

import com.etsisi.appquitectura.domain.model.UserBO

data class ItemRanking(
    val userBO: UserBO,
    val position: Int
)
