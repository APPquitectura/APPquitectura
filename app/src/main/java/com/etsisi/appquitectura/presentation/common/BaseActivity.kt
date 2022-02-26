package com.etsisi.appquitectura.presentation.common

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHost
import org.koin.android.ext.android.get
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf
import kotlin.reflect.KClass

abstract class BaseActivity<binding: ViewDataBinding, viewModel: ViewModel>(
    @LayoutRes private val layoutRes: Int,
    private val viewModelClass: KClass<viewModel>
): AppCompatActivity() {
    protected lateinit var mBinding: binding
        private set

    protected lateinit var mViewModel: viewModel
        private set

    protected lateinit var navigator: Navigator
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpSplashScreen()
        mBinding = DataBindingUtil.setContentView(this@BaseActivity, layoutRes)
        mViewModel = getViewModel(clazz = viewModelClass)
        setUpDataBinding(mBinding, mViewModel)
        observeViewModel(mViewModel)
        getNavController(::getFragmentContainer)?.let { navController ->
            navigator = get { parametersOf(navController) }
        }
    }

    private fun getNavController(fragmentContainer:() -> Int): NavController? {
        val result = runCatching {
            (supportFragmentManager.findFragmentById(fragmentContainer()) as? NavHost)?.navController
        }.getOrNull()
        return result
    }

    abstract fun observeViewModel(mViewModel: ViewModel)

    abstract fun setUpDataBinding(mBinding: ViewDataBinding, mViewModel: ViewModel)

    abstract fun getFragmentContainer(): Int

    open fun setUpSplashScreen() {}
}