package com.etsisi.appquitectura.presentation.ui.login.view

import androidx.activity.result.contract.ActivityResultContracts
import com.etsisi.appquitectura.R
import com.etsisi.appquitectura.databinding.ActivityLoginBinding
import com.etsisi.appquitectura.presentation.common.BaseActivity
import com.etsisi.appquitectura.presentation.common.GoogleSignInListener
import com.etsisi.appquitectura.presentation.common.LiveEventObserver
import com.etsisi.appquitectura.presentation.ui.login.viewmodel.LoginViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException

class LoginActivity : BaseActivity<ActivityLoginBinding, LoginViewModel>(
    R.layout.activity_login, LoginViewModel::class
), GoogleSignInListener {

    private val googleSignInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val completedTask = GoogleSignIn.getSignedInAccountFromIntent(it.data)
            try {
                val account = completedTask.getResult(ApiException::class.java)
                mViewModel.initFirebaseLoginWithCredentials(account, true,this)
            } catch (e: ApiException) {
                mViewModel.initGoogleLoginFailed(e.statusCode)
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
        }
    }

    override fun getFragmentContainer(): Int = mBinding.navHostLogin.id

    override fun initSignInGoogle() {
        googleSignInLauncher.launch(mViewModel.googleClient.signInIntent)
    }

}