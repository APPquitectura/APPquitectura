package com.etsisi.appquitectura.presentation.utils

import android.os.Build

val String.Companion.EMPTY: String
    get() = ""

inline fun deviceApiIsAtLeast(version: Int): Boolean  {
    return Build.VERSION.SDK_INT >= version
}