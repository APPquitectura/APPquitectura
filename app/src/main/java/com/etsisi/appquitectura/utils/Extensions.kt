package com.etsisi.appquitectura.presentation.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.LabeledIntent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.Insets
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.WindowInsets
import androidx.core.view.OnApplyWindowInsetsListener
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsAnimationCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import org.json.JSONException
import org.json.JSONObject

val String.Companion.EMPTY: String
    get() = ""

val String.Companion.SLASH: String
    get() = "/"

inline fun <reified T : Activity> Activity.startClearActivity(args: Bundle? = null) {
    val intent = Intent(this, T::class.java).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    }
    args?.let {
        intent.putExtras(it)
    }
    startActivity(intent)
}

inline val <reified T> T.TAG: String
    get() = T::class.java.canonicalName ?: T::class.simpleName ?: T::class.java.simpleName

fun <T> List<T>.penultimate(): T? {
    return kotlin.runCatching { this[size - 2] }.getOrNull()
}

fun Context.showKeyboard(view: View) {
    //The view will have to be the container
    val isKeyboardVisible = ViewCompat.getRootWindowInsets(view)?.isVisible(WindowInsetsCompat.Type.ime())
    if (isKeyboardVisible == false) {
        ViewCompat.setOnApplyWindowInsetsListener(view, object : OnApplyWindowInsetsListener {
            override fun onApplyWindowInsets(v: View, insets: WindowInsetsCompat): WindowInsetsCompat {
                val imeWindow = insets.getInsets(WindowInsetsCompat.Type.ime())
                v.updatePadding(left = imeWindow.left, right = imeWindow.right, bottom = imeWindow.bottom)
                return insets
            }
        })
        ViewCompat.getWindowInsetsController(view)?.let { controller ->
            controller.show(WindowInsetsCompat.Type.ime())
        }
    }
}

fun Context.hideKeyboard(view: View) {
    val isKeyboardVisible = ViewCompat.getRootWindowInsets(view)?.isVisible(WindowInsetsCompat.Type.ime())
    if (isKeyboardVisible == false) {
        ViewCompat.getWindowInsetsController(view)?.let { controller ->
            controller.hide(WindowInsetsCompat.Type.ime())
        }
    }
}

fun FragmentActivity.checkDialogOpened(): Boolean {
    var ret = false
    supportFragmentManager.fragments.filterIsInstance<DialogFragment>().forEach {
        if (it.isCancelable) it.dismiss()
        else ret = true
    }
    return ret
}

fun Bundle.toJson(): JSONObject {
    val json = JSONObject()
    val keys = this.keySet()
    for (key in keys) {
        try {
            json.put(key, this.get(key))
        } catch (e: JSONException) {
            Log.e("toJson", e.toString())
        }
    }

    return json
}

fun List<ResolveInfo>.toLabeledIntentArray(packageManager: PackageManager): Array<LabeledIntent> =
    map {
        val packageName = it.activityInfo.packageName
        val intent = packageManager.getLaunchIntentForPackage(packageName)
        LabeledIntent(intent, packageName, it.loadLabel(packageManager), it.icon)
    }.toTypedArray()

fun <T> getMethodName(clazz: Class<T>): String {
    return "${clazz.enclosingClass?.simpleName}.${clazz.enclosingMethod?.name}"
}

fun Activity.hideSystemBars(mainContaier: View) {
    window?.apply {
        val systemBarIsVisible = ViewCompat.getRootWindowInsets(mainContaier)?.let { insets ->
            insets?.isVisible(WindowInsetsCompat.Type.systemBars())
        }
        if (systemBarIsVisible == true) {
            ViewCompat.getWindowInsetsController(mainContaier)?.let { controller ->
                controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_BARS_BY_SWIPE
                controller.hide(WindowInsetsCompat.Type.systemBars())
            }
        }
    }
}

fun Activity.showNavigationBar(mainContaier: View) {
    window?.apply {
        WindowCompat.setDecorFitsSystemWindows(this, true)
        ViewCompat.getWindowInsetsController(mainContaier)?.let { controller ->
            controller.show(WindowInsetsCompat.Type.navigationBars())
        }
    }
}

fun Activity.getWindowPixels(): Pair<Int, Int> { //Pair(width, height)
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val windowMetrics = windowManager.currentWindowMetrics
        val insets: Insets = windowMetrics.windowInsets.getInsetsIgnoringVisibility(WindowInsets.Type.systemBars() or WindowInsets.Type.displayCutout())
        val width = insets.right + insets.left
        val height = insets.top + insets.top
        Pair(width, height)
    } else {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        Pair(displayMetrics.widthPixels, displayMetrics.heightPixels)
    }

}
