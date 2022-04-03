package com.etsisi.appquitectura.presentation.common

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter

object BindingAdapter {

    @BindingAdapter("isVisible")
    @JvmStatic
    fun View.isVisible(isVisible: Boolean) {
        this.isVisible = isVisible
    }

    @BindingAdapter("onClickCustom")
    @JvmStatic
    fun View.setCustomOnClick(method: () -> Unit) {
       this.setOnClickListener { method.invoke() }
    }

    @BindingAdapter("textRes")
    @JvmStatic
    fun TextView.textRes(@StringRes res: Int?) {
        res?.let {
            text = this.context.getString(it)
        }
    }

    @BindingAdapter("srcRes")
    @JvmStatic
    fun ImageView.setImageRes(@DrawableRes res: Int?) {
        res?.let {
            val drawable = ContextCompat.getDrawable(context, it)
            setImageDrawable(drawable)
        }
    }
}