package com.etsisi.appquitectura.presentation.common

import android.view.View
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter

object BindingAdapter {

    @BindingAdapter("isVisible")
    @JvmStatic
    fun View.isVisible(isVisible: Boolean) {
        this.isVisible = isVisible
    }
}