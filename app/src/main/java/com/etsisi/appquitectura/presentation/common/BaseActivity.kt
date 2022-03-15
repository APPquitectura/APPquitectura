package com.etsisi.appquitectura.presentation.common

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHost
import com.etsisi.appquitectura.utils.NavigationTracker
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.parameter.parametersOf
import kotlin.reflect.KClass

abstract class BaseActivity<binding: ViewDataBinding, viewModel: ViewModel>(
    @LayoutRes private val layoutRes: Int,
    private val viewModelClass: KClass<viewModel>
): AppCompatActivity() {
    private val navigationTracker: NavigationTracker by inject()

    protected open val isSplash: Boolean
        get() = false

    protected lateinit var splashScreen: SplashScreen
        private set

    protected lateinit var mBinding: binding
        private set

    protected lateinit var mViewModel: viewModel
        private set

    protected lateinit var navigator: Navigator
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (isSplash) {
            splashScreen = installSplashScreen()
        }
        mBinding = DataBindingUtil.setContentView(this@BaseActivity, layoutRes)
        mViewModel = getViewModel(clazz = viewModelClass)

        intent.extras?.let { bundle ->
            if (!bundle.isEmpty || intent.data != null) {
                getActivityArgs(bundle)
            }
        }

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

    override fun onResume() {
        super.onResume()
        getNavController(::getFragmentContainer)?.let {
            it.addOnDestinationChangedListener(navigationTracker)
        }
    }

    override fun onPause() {
        super.onPause()
        getNavController(::getFragmentContainer)?.let {
            it.removeOnDestinationChangedListener(navigationTracker)
        }
    }

    open fun getActivityArgs(bundle: Bundle) {}

    abstract fun observeViewModel(mViewModel: viewModel)

    abstract fun setUpDataBinding(mBinding: binding, mViewModel: viewModel)

    abstract fun getFragmentContainer(): Int
}