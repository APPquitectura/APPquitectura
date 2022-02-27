package com.etsisi.appquitectura.presentation.ui.login.view.formscreen

import android.graphics.Color
import android.widget.Toast
import com.etsisi.appquitectura.R
import com.etsisi.appquitectura.databinding.FragmentLoginFormBinding
import com.etsisi.appquitectura.presentation.common.BaseFragment
import com.etsisi.appquitectura.presentation.common.LiveEventObserver
import com.etsisi.appquitectura.presentation.ui.login.viewmodel.LoginViewModel
import com.etsisi.appquitectura.presentation.utils.hideKeyboard
import com.github.razir.progressbutton.attachTextChangeAnimator
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress

class LoginFormFragment: BaseFragment<FragmentLoginFormBinding, LoginViewModel>(
    R.layout.fragment_login_form,
    LoginViewModel::class
) {

    override val isSharedViewModel: Boolean
        get() = true

    override fun observeViewModel(mViewModel: LoginViewModel) {
        with(mViewModel) {
            loading.observe(viewLifecycleOwner) {
                if (it) {
                    requireContext().hideKeyboard(mBinding.btnLogin)
                    mBinding.btnLogin.showProgress {
                        buttonText = getString(R.string.loading)
                        progressColors = intArrayOf(Color.WHITE, Color.BLACK)
                    }
                } else {
                    mBinding.btnLogin.hideProgress(R.string.login_btn_txt)
                }
            }
            onRegister.observe(viewLifecycleOwner, LiveEventObserver {
                navigator.openRegisterFragment()
            })
            onSuccessLogin.observe(viewLifecycleOwner, LiveEventObserver {
                Toast.makeText(requireContext(), "LOG IN!", Toast.LENGTH_LONG).show()
            })
        }
    }

    override fun setUpDataBinding(mBinding: FragmentLoginFormBinding, mViewModel: LoginViewModel) {
        with(mBinding) {
            lifecycleOwner = viewLifecycleOwner
            viewModel = mViewModel
            executePendingBindings()
            btnLogin.attachTextChangeAnimator()
        }
    }
}