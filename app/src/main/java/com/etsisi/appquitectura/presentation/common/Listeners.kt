package com.etsisi.appquitectura.presentation.common

import com.etsisi.appquitectura.presentation.ui.main.model.ItemGameMode
import com.etsisi.appquitectura.presentation.ui.main.model.ItemHome
import com.etsisi.appquitectura.presentation.ui.main.model.ItemSettings

interface GoogleSignInListener {
    fun initSignInGoogle()
}

fun interface HomeItemClicked {
    fun onMenuItemClicked(item: ItemHome)
}

fun interface SettingsItemClicked {
    fun onSettingsItemClicked(item: ItemSettings)
}

interface PlayFragmentListener {
    fun onGameMode(item: ItemGameMode)
}

