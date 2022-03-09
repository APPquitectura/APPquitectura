package com.etsisi.appquitectura.presentation.ui.login.view

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import android.view.View
import android.view.animation.AnticipateInterpolator
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.etsisi.appquitectura.R
import com.etsisi.appquitectura.databinding.ActivityLoginBinding
import com.etsisi.appquitectura.presentation.common.BaseActivity
import com.etsisi.appquitectura.presentation.common.GoogleSignInListener
import com.etsisi.appquitectura.presentation.common.LiveEventObserver
import com.etsisi.appquitectura.presentation.ui.login.viewmodel.LoginViewModel
import com.etsisi.appquitectura.presentation.utils.TAG
import com.etsisi.appquitectura.presentation.utils.deviceApiIsAtLeast
import com.etsisi.appquitectura.utils.Constants
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase

class LoginActivity : BaseActivity<ActivityLoginBinding, LoginViewModel>(
    R.layout.activity_login, LoginViewModel::class
), GoogleSignInListener {

    private val googleSignInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val completedTask = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            try {
                val account = completedTask.getResult(ApiException::class.java)
                mViewModel.initFirebaseLoginWithCredentials(account.idToken, this)
            } catch (e: ApiException) {
                mViewModel.initGoogleLoginFailed(e.statusCode)
            }
        }

    override fun getActivityArgs() {
        if (intent.data?.host == Constants.DYNAMIC_LINK_PREFIX) {
            mViewModel.showLoading(true, R.string.verifying)
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

    override fun setUpDataBinding(mBinding: ActivityLoginBinding, mViewModel: LoginViewModel) {
        with(mBinding) {
            viewModel = mViewModel
            lifecycleOwner = this@LoginActivity
            lifecycle.addObserver(mViewModel)
        }
    }

    override fun observeViewModel(mViewModel: LoginViewModel) {
        with(mViewModel) {
            // String default_web_client_id is auto-generated
            setGoogleClient(this@LoginActivity, getString(R.string.default_web_client_id))
            onError.observe(this@LoginActivity, LiveEventObserver { dialogConfig ->
                navigator.openDialog(dialogConfig)
            })
            onSuccessCode.observe(this@LoginActivity, LiveEventObserver {
                if (it) {
                    navigator.navigateFromLoginToMain()
                }
            })
        }
    }

    override fun getFragmentContainer(): Int = mBinding.navHostLogin.id

    override fun onStart() {
        super.onStart()
        if (!mViewModel.isUserLogged()) {
            GoogleSignIn.getLastSignedInAccount(this)?.let { account ->
                mViewModel.initFirebaseLoginWithCredentials(account.idToken, this)
            }
        }
    }


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
            val slideUp = ObjectAnimator.ofFloat(
                splashScreenView,
                View.TRANSLATION_Y,
                0f,
                -splashScreenView.height.toFloat()
            )
            slideUp.interpolator = AnticipateInterpolator()
            slideUp.duration = 200L
            slideUp.doOnEnd { splashScreenView.remove() }
            slideUp.start()
        }
    }

    override fun initSignInGoogle() {
        googleSignInLauncher.launch(mViewModel.googleClient.signInIntent)
    }

}