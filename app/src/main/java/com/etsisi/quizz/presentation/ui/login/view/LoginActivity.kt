package com.etsisi.quizz.presentation.ui.login.view

import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import com.etsisi.quizz.R
import com.etsisi.quizz.databinding.ActivityLoginBinding
import com.etsisi.quizz.presentation.common.BaseActivity
import com.etsisi.quizz.presentation.ui.login.viewmodel.LoginViewModel

class LoginActivity : BaseActivity<ActivityLoginBinding, LoginViewModel>(
    R.layout.activity_login, LoginViewModel::class
) {
    override fun observeViewModel(mViewModel: ViewModel) {
        //TODO("Not yet implemented")
    }

    override fun setUpDataBinding(mBinding: ViewDataBinding, mViewModel: ViewModel) {
        //TODO("Not yet implemented")
    }

}