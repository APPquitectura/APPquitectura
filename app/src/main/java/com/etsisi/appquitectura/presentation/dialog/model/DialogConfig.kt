package com.etsisi.appquitectura.presentation.dialog.model
import android.os.Parcelable
import androidx.annotation.RawRes
import androidx.annotation.StringRes
import kotlinx.parcelize.Parcelize

@Parcelize
data class DialogConfig(
    @StringRes val title: Int,
    @StringRes val body: Int? = null,
    @RawRes val lottieRes: Int? = null,
    val withEditText: Boolean = false
) : Parcelable