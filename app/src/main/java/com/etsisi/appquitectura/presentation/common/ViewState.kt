package com.etsisi.appquitectura.presentation.common

sealed class ViewState {
    object IDLE: ViewState()
    object LOADING: ViewState()
    object ERROR: ViewState()
}
