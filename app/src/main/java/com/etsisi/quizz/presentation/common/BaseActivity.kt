package com.etsisi.quizz.presentation.common

import android.os.Bundle
import android.os.PersistableBundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf
import kotlin.reflect.KClass

abstract class BaseActivity<binding: ViewDataBinding, viewModel: ViewModel>(
    @LayoutRes private val layoutRes: Int,
    private val viewModelClass: KClass<viewModel>
): AppCompatActivity() {
    protected val navigator: Navigator by inject { parametersOf(this@BaseActivity) }
            
    protected lateinit var mBinding: binding
        private set

    protected lateinit var mViewModel: viewModel
        private set

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        
        mBinding = DataBindingUtil.setContentView(this@BaseActivity, layoutRes)
        mViewModel = getViewModel(clazz = viewModelClass)
        setUpDataBinding(mBinding, mViewModel)
        observeViewModel(mViewModel)
    }

    abstract fun observeViewModel(mViewModel: ViewModel)

    abstract fun setUpDataBinding(mBinding: ViewDataBinding, mViewModel: ViewModel)
}