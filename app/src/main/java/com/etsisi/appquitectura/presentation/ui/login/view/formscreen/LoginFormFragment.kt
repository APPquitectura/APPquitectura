package com.etsisi.appquitectura.presentation.ui.login.view.formscreen

import com.etsisi.appquitectura.R
import com.etsisi.appquitectura.databinding.FragmentLoginFormBinding
import com.etsisi.appquitectura.presentation.common.BaseFragment
import com.etsisi.appquitectura.presentation.ui.login.viewmodel.LoginViewModel

class LoginFormFragment: BaseFragment<FragmentLoginFormBinding, LoginViewModel>(
    R.layout.fragment_login_form,
    LoginViewModel::class
) {

    override val isSharedViewModel: Boolean
        get() = true

    override fun observeViewModel(mViewModel: LoginViewModel) {
        //TODO("Not yet implemented")
    }

    override fun setUpDataBinding(mBinding: FragmentLoginFormBinding, mViewModel: LoginViewModel) {
        //TODO("Not yet implemented")
    }
}