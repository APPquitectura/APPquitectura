package com.etsisi.appquitectura.presentation.ui.login.view

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.os.Build
import android.view.View
import android.view.animation.AnticipateInterpolator
import androidx.annotation.RequiresApi
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import com.etsisi.appquitectura.R
import com.etsisi.appquitectura.databinding.ActivityLoginBinding
import com.etsisi.appquitectura.presentation.common.BaseActivity
import com.etsisi.appquitectura.presentation.common.EmptyViewModel
import com.etsisi.appquitectura.presentation.common.LiveEventObserver
import com.etsisi.appquitectura.presentation.ui.login.viewmodel.LoginViewModel
import com.etsisi.appquitectura.presentation.utils.deviceApiIsAtLeast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : BaseActivity<ActivityLoginBinding, LoginViewModel>(
    R.layout.activity_login, LoginViewModel::class
) {

    override fun setUpDataBinding(mBinding: ActivityLoginBinding, mViewModel: LoginViewModel) {
        with(mBinding) {
            lifecycleOwner = this@LoginActivity
            lifecycle.addObserver(mViewModel)
        }
    }

    override fun observeViewModel(mViewModel: LoginViewModel) {
        with(mViewModel) {
            isUserLoggedIn.observe(this@LoginActivity, LiveEventObserver{ isLogged ->

            })
        }
    }

    override fun getFragmentContainer(): Int = mBinding.navHostLogin.id


    @SuppressLint("NewApi")
    override fun setUpSplashScreen() {
        installSplashScreen()
        if (deviceApiIsAtLeast(Build.VERSION_CODES.S)) {
            setUpSplashAnimation()
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun setUpSplashAnimation() {
        splashScreen.setOnExitAnimationListener { splashScreenView ->
            // Create your custom animation.
            val slideUp = ObjectAnimator.ofFloat(
                splashScreenView,
                View.TRANSLATION_Y,
                0f,
                -splashScreenView.height.toFloat()
            )
            slideUp.interpolator = AnticipateInterpolator()
            slideUp.duration = 200L

            // Call SplashScreenView.remove at the end of your custom animation.
            slideUp.doOnEnd { splashScreenView.remove() }

            // Run your animation.
            slideUp.start()
        }

    }

}