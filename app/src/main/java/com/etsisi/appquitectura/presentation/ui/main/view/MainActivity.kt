package com.etsisi.appquitectura.presentation.ui.main.view

import android.view.View
import android.view.ViewTreeObserver
import com.etsisi.appquitectura.R
import com.etsisi.appquitectura.databinding.ActivityMainBinding
import com.etsisi.appquitectura.presentation.common.BaseActivity
import com.etsisi.appquitectura.presentation.common.LiveEventObserver
import com.etsisi.appquitectura.presentation.ui.main.viewmodel.MainViewModel

class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>(
    R.layout.activity_main,
    MainViewModel::class
) {

    override val isSplash: Boolean
        get() = true

    private val contentView: View
        get() = findViewById(android.R.id.content)

    private val onPreDrawListener = object: ViewTreeObserver.OnPreDrawListener {
        override fun onPreDraw(): Boolean {
            return if (mViewModel.initSilentLogin(this@MainActivity)) {
                contentView.viewTreeObserver.removeOnPreDrawListener(this)
                true
            } else {
                false
            }
        }
    }

    override fun observeViewModel(mViewModel: MainViewModel) {
        with(mViewModel) {
            onSuccessLogin.observe(this@MainActivity, LiveEventObserver {
                if (!it) {
                    navigator.navigateFromMainToLogin()
                    finish()
                }
            })
        }
    }

    override fun getFragmentContainer(): Int {
        return mBinding.mainContainer.id
    }

    override fun setUpDataBinding(mBinding: ActivityMainBinding, mViewModel: MainViewModel) {
        with(mBinding) {
            contentView.viewTreeObserver.addOnPreDrawListener(onPreDrawListener)
            lifecycleOwner = this@MainActivity
            viewModel = mViewModel
            lifecycle.addObserver(mViewModel)
            executePendingBindings()
        }
    }
}