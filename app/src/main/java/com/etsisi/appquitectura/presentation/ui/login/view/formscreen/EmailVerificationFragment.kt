package com.etsisi.appquitectura.presentation.ui.login.view.formscreen

import androidx.core.view.isVisible
import androidx.navigation.fragment.navArgs
import com.etsisi.appquitectura.R
import com.etsisi.appquitectura.databinding.FragmentEmailVerificationBinding
import com.etsisi.appquitectura.presentation.common.BaseFragment
import com.etsisi.appquitectura.presentation.common.EmptyViewModel

class EmailVerificationFragment: BaseFragment<FragmentEmailVerificationBinding, EmptyViewModel>(
    R.layout.fragment_email_verification,
    EmptyViewModel::class
) {

    val args: EmailVerificationFragmentArgs by navArgs()

    override fun setUpDataBinding(
        mBinding: FragmentEmailVerificationBinding,
        mViewModel: EmptyViewModel
    ) {
        with(mBinding) {
            btnGoToMain.setOnClickListener {
                navigator.navigateFromLoginToMain()
            }
            btnGoToMain.isVisible = args.emailVerified

            if (args.emailVerified) {

            } else {
                btnGoToMain.isVisible = false
            }
        }
    }

    override fun observeViewModel(mViewModel: EmptyViewModel) {}

}