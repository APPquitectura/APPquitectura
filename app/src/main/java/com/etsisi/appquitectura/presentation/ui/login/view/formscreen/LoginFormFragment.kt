package com.etsisi.appquitectura.presentation.ui.login.view.formscreen

import android.graphics.Color
import androidx.core.view.isVisible
import androidx.navigation.fragment.navArgs
import com.etsisi.appquitectura.R
import com.etsisi.appquitectura.databinding.FragmentLoginFormBinding
import com.etsisi.appquitectura.domain.enums.LoginNavType
import com.etsisi.appquitectura.presentation.common.BaseFragment
import com.etsisi.appquitectura.presentation.common.LiveEventObserver
import com.etsisi.appquitectura.presentation.ui.login.view.LoginActivity
import com.etsisi.appquitectura.presentation.ui.login.viewmodel.LoginViewModel
import com.etsisi.appquitectura.presentation.utils.hideKeyboard
import com.github.razir.progressbutton.attachTextChangeAnimator
import com.github.razir.progressbutton.bindProgressButton
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showDrawable
import com.github.razir.progressbutton.showProgress

class LoginFormFragment: BaseFragment<FragmentLoginFormBinding, LoginViewModel>(
    R.layout.fragment_login_form,
    LoginViewModel::class
) {
    private val args: LoginFormFragmentArgs by navArgs()

    override val isSharedViewModel: Boolean
        get() = args.navType == LoginNavType.LOGIN

    override fun getFragmentArgs(mBinding: FragmentLoginFormBinding) {
        when(args.navType) {
            LoginNavType.REGISTER -> {
                mBinding.btnGoogle.isVisible = false
                mBinding.btnLogin.isVisible = false
            }
            LoginNavType.LOGIN -> {
                mBinding.btnGoogle.isVisible = true
                mBinding.btnRegister.isVisible = true
            }
        }
    }

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
                when(args.navType) {
                    LoginNavType.LOGIN -> {
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
                    LoginNavType.REGISTER -> {
                        if (loaded) {
                            requireContext().hideKeyboard(mBinding.form.etPassword)
                            mBinding.btnRegister.showDrawable(mViewModel.btnDrawable()) {
                                buttonText = getString(R.string.done)
                            }
                        } else {
                            mBinding.btnRegister.hideProgress(getString(R.string.register_btn_text))
                        }
                    }
                }
            }
            onSuccessRegister.observe(viewLifecycleOwner, LiveEventObserver { navType ->
                requireContext().hideKeyboard(mBinding.btnRegister)
            })
            onVerifyEmail.observe(viewLifecycleOwner) {
                navigator.openVerifyEmailFragment()
            }
            onRegister.observe(viewLifecycleOwner, LiveEventObserver {
                if (args.navType == LoginNavType.REGISTER) {
                    initRegister()
                } else {
                    navigator.openRegisterFragment()
                }
            })
        }
    }
}