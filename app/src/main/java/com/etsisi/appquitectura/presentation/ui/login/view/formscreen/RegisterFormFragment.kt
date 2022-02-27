package com.etsisi.appquitectura.presentation.ui.login.view.formscreen

import android.graphics.Color
import androidx.core.content.ContextCompat
import com.etsisi.appquitectura.R
import com.etsisi.appquitectura.databinding.FragmentRegisterBinding
import com.etsisi.appquitectura.presentation.common.BaseFragment
import com.etsisi.appquitectura.presentation.common.LiveEventObserver
import com.etsisi.appquitectura.presentation.ui.login.viewmodel.LoginViewModel
import com.etsisi.appquitectura.presentation.utils.hideKeyboard
import com.github.razir.progressbutton.attachTextChangeAnimator
import com.github.razir.progressbutton.bindProgressButton
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showDrawable

class RegisterFormFragment: BaseFragment<FragmentRegisterBinding, LoginViewModel>(
    R.layout.fragment_register,
    LoginViewModel::class
) {
    private val drawable by lazy {
        ContextCompat.getDrawable(requireContext(), R.drawable.ic_check_round_selected)?.apply {
            setBounds(0, 0, 50, 50)
        }
    }
    override val isSharedViewModel: Boolean
        get() = true

    override fun setUpDataBinding(mBinding: FragmentRegisterBinding, mViewModel: LoginViewModel) {
        with(mBinding) {
            lifecycleOwner = viewLifecycleOwner
            viewModel = mViewModel
            bindProgressButton(btnRegister)
            btnRegister.attachTextChangeAnimator()
        }
    }

    override fun observeViewModel(mViewModel: LoginViewModel) {
        with(mViewModel) {
            onSuccessRegister.observe(viewLifecycleOwner, LiveEventObserver { navType ->
                if (navType == LoginViewModel.REGISTER_NAVIGATION_TYPE.SUCESS) {
                    //To main
                } else {
                    //verify email
                }
                requireContext().hideKeyboard(mBinding.btnRegister)
            })
            loading.observe(viewLifecycleOwner) { isLoading ->
                if (isLoading) {
                    mBinding.btnRegister.showDrawable(drawable!!) {
                        buttonText = getString(R.string.done)
                    }
                } else {
                    mBinding.btnRegister.hideProgress(getString(R.string.register_btn_text))
                }
            }
        }
    }
}