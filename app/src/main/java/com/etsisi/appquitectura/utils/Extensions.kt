package com.etsisi.appquitectura.presentation.utils

import android.content.Context
import android.os.Build
import android.view.View
import android.view.inputmethod.InputMethodManager

val String.Companion.EMPTY: String
    get() = ""

inline fun deviceApiIsAtLeast(version: Int): Boolean  {
    return Build.VERSION.SDK_INT >= version
}

inline val <reified T> T.TAG: String
    get() = T::class.java.canonicalName ?: T::class.simpleName ?: T::class.java.simpleName

fun Context.showKeyboard(view: View) {
    if (view.requestFocus()) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }
}

fun Context.hideKeyboard(view: View) {
    if (view.hasFocus()) {
        view.clearFocus()
    }
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}