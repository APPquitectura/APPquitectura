package com.etsisi.quizz.presentation.common

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import org.koin.androidx.viewmodel.ext.android.getViewModel
import kotlin.reflect.KClass

abstract class BaseActivity<binding: ViewDataBinding, viewModel: ViewModel>(
    @LayoutRes private val layoutRes: Int,
    private val viewModelClass: KClass<viewModel>
): AppCompatActivity() {
    protected lateinit var mBinding: binding
        private set

    protected lateinit var mViewModel: viewModel
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpSplashScreen()
        mBinding = DataBindingUtil.setContentView(this@BaseActivity, layoutRes)
        mViewModel = getViewModel(clazz = viewModelClass)
        setUpDataBinding(mBinding, mViewModel)
        observeViewModel(mViewModel)
    }

    abstract fun observeViewModel(mViewModel: ViewModel)

    abstract fun setUpDataBinding(mBinding: ViewDataBinding, mViewModel: ViewModel)

    open fun setUpSplashScreen() {}
}