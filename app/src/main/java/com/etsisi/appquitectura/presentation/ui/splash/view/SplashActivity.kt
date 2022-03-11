package com.etsisi.appquitectura.presentation.ui.splash.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.core.os.bundleOf
import com.etsisi.appquitectura.R
import com.etsisi.appquitectura.databinding.ActivitySplashBinding
import com.etsisi.appquitectura.presentation.common.BaseActivity
import com.etsisi.appquitectura.presentation.common.LiveEventObserver
import com.etsisi.appquitectura.presentation.ui.login.view.LoginActivity
import com.etsisi.appquitectura.presentation.ui.main.view.MainActivity
import com.etsisi.appquitectura.presentation.ui.splash.viewmodel.SplashViewModel
import com.etsisi.appquitectura.presentation.utils.TAG
import com.etsisi.appquitectura.presentation.utils.startActivity
import com.etsisi.appquitectura.utils.Constants
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase

class SplashActivity : BaseActivity<ActivitySplashBinding, SplashViewModel>(
    R.layout.activity_splash,
    SplashViewModel::class
){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with(intent) {
            if (flags and Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT != 0 && data?.host.isNullOrBlank()) {
                // Activity was brought to front and not created,
                // Thus finishing this will get us to the last viewed activity
                finish()
                return
            }
            if (data?.host.isNullOrBlank()) {
                mViewModel.login(this@SplashActivity)
            }
        }
    }

    override fun getActivityArgs(bundle: Bundle) {
        if (intent.data?.host == Constants.DYNAMIC_LINK_PREFIX) {
            Firebase
                .dynamicLinks
                .getDynamicLink(intent)
                .addOnSuccessListener(this) { pendingDynamicLinkData ->
                    pendingDynamicLinkData?.link?.let { deeplink ->
                        mViewModel.initVerificationCode(pendingDynamicLinkData)
                    }
                }
                .addOnFailureListener(this) { e ->
                    Log.e(TAG, "getDynamicLink:onFailure", e)
                }
        }
    }

    override fun observeViewModel(mViewModel: SplashViewModel) {
        with(mViewModel) {
            onCodeVerified.observe(this@SplashActivity, LiveEventObserver {
                startActivity<MainActivity>(bundleOf(MainActivity.EXTRA_CODE_VERIFIED to it))
            })
            successLogin.observe(this@SplashActivity, LiveEventObserver { success ->
                if (success)
                    startActivity<MainActivity>()
                else
                    startActivity<LoginActivity>()
            })
        }
    }

    override fun setUpDataBinding(mBinding: ActivitySplashBinding, mViewModel: SplashViewModel) {}

    override fun getFragmentContainer(): Int = Int.MIN_VALUE
}