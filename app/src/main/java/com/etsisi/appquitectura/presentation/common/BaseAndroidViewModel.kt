package com.etsisi.appquitectura.presentation.common

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

open class BaseAndroidViewModel(
    private val applicationContext: Application
): AndroidViewModel(applicationContext), LifecycleObserver {

    protected val _loading by lazy { MutableLiveData<Pair<Boolean, String?>>() }
    val loading: LiveData<Pair<Boolean, String?>>
        get() = _loading

    fun showLoading(show: Boolean, msgRes: Int? = null) {
        val msg = msgRes?.let { applicationContext.getString(it) }
        _loading.value = Pair(show, msg)
    }
}