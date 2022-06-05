package com.etsisi.appquitectura.presentation.common

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.etsisi.appquitectura.R

open class BaseViewModel(): ViewModel() {

    protected val _loading by lazy { MutableLiveData(Pair(false, R.string.loading)) }
    val loading: LiveData<Pair<Boolean, Int>>
        get() = _loading

    fun showLoading(show: Boolean, @StringRes msgRes: Int = R.string.loading) {
        _loading.value = Pair(show, msgRes)
    }
}