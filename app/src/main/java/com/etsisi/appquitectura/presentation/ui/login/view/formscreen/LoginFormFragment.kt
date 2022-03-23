package com.etsisi.appquitectura.presentation.ui.login.view.formscreen

import android.graphics.Color
import com.etsisi.appquitectura.R
import com.etsisi.appquitectura.databinding.FragmentLoginFormBinding
import com.etsisi.appquitectura.presentation.common.BaseFragment
import com.etsisi.appquitectura.presentation.common.LiveEventObserver
import com.etsisi.appquitectura.presentation.ui.login.view.LoginActivity
import com.etsisi.appquitectura.presentation.ui.login.viewmodel.LoginViewModel
import com.etsisi.appquitectura.presentation.utils.hideKeyboard
import com.github.razir.progressbutton.attachTextChangeAnimator
import com.github.razir.progressbutton.bindProgressButton
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress

class LoginFormFragment: BaseFragment<FragmentLoginFormBinding, LoginViewModel>(
    R.layout.fragment_login_form,
    LoginViewModel::class
) {
    override val isSharedViewModel: Boolean
        get() = true

    override fun setUpDataBinding(mBinding: FragmentLoginFormBinding, mViewModel: LoginViewModel) {
        with(mBinding) {
            lifecycleOwner = viewLifecycleOwner
            viewModel = mViewModel
            (requireActivity() as? LoginActivity)?.let { googleListener = it }
            executePendingBindings()
            bindProgressButton(btnRegister)
            btnLogin.attachTextChangeAnimator()
            btnRegister.attachTextChangeAnimator()
        }
    }

    override fun observeViewModel(mViewModel: LoginViewModel) {
        with(mViewModel) {
            loaded.observe(viewLifecycleOwner) { loaded ->
                if (loaded) {
                    mBinding.btnLogin.hideProgress(R.string.login_btn_txt)
                } else {
                    requireContext().hideKeyboard(mBinding.form.etPassword)
                    mBinding.btnLogin.showProgress {
                        buttonText = getString(R.string.loading)
                        progressColors = intArrayOf(Color.WHITE, Color.BLACK)
                    }
                }
            }

            onSuccessLogin.observe(viewLifecycleOwner, LiveEventObserver {
                if (it) {
                    navigator.navigateFromLoginToMain()
                }
            })

            onRegister.observe(viewLifecycleOwner, LiveEventObserver {
                navigator.openRegisterFragment()
            })

            emailVerificationSended.observe(viewLifecycleOwner) {
                navigator.openVerifyEmailFragment()
            }
        }
    }
}