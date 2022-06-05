package com.etsisi.appquitectura.presentation.ui.main

import android.view.View
import android.view.ViewTreeObserver
import com.etsisi.appquitectura.R
import com.etsisi.appquitectura.databinding.ActivityMainBinding
import com.etsisi.appquitectura.presentation.common.BaseActivity
import com.etsisi.appquitectura.presentation.common.LiveEventObserver

class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>(
    R.layout.activity_main,
    MainViewModel::class
) {

    override val isSplash: Boolean
        get() = true

    private val contentView: View
        get() = findViewById(android.R.id.content)

    private val onPreDrawListener = object : ViewTreeObserver.OnPreDrawListener {
        override fun onPreDraw(): Boolean {
            mViewModel.setGoogleClient(this@MainActivity, getString(R.string.default_web_client_id))
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

    override fun getFragmentContainer(): View {
        return mBinding.mainContainer
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