package com.etsisi.appquitectura.presentation.ui.login.view

import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import com.etsisi.appquitectura.R
import com.etsisi.appquitectura.databinding.ActivityLoginBinding
import com.etsisi.appquitectura.presentation.common.BaseActivity
import com.etsisi.appquitectura.presentation.common.GoogleSignInListener
import com.etsisi.appquitectura.presentation.common.LiveEventObserver
import com.etsisi.appquitectura.presentation.ui.login.viewmodel.LoginViewModel
import com.etsisi.appquitectura.presentation.utils.TAG
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
                mViewModel.onGoogleSignInClicked(account, this)
            } catch (e: ApiException) {
                mViewModel.initGoogleLoginFailed(e.statusCode)
            }
        }

    override fun onResume() {
        super.onResume()
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
            onError.observe(this@LoginActivity, LiveEventObserver { dialogConfig ->
                navigator.openLoginDialog(dialogConfig)
            })
            onCodeVerified.observe(this@LoginActivity, LiveEventObserver {
                navigator.navigateFromLoginToMain(this@LoginActivity)
            })
        }
    }

    override fun getFragmentContainer(): Int = mBinding.navHostLogin.id

    override fun initSignInGoogle() {
        googleSignInLauncher.launch(mViewModel.googleClient.signInIntent)
    }

}