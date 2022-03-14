package com.etsisi.appquitectura.presentation.common

import com.etsisi.appquitectura.presentation.ui.main.model.ItemHome

fun interface HomeItemClicked {
    fun onMenuItemClicked(item: ItemHome)
}