package com.etsisi.appquitectura.presentation.ui.login.view.formscreen

import com.etsisi.appquitectura.R
import com.etsisi.appquitectura.databinding.FragmentEmailVerificationBinding
import com.etsisi.appquitectura.presentation.common.BaseFragment
import com.etsisi.appquitectura.presentation.common.EmptyViewModel

class EmailVerificationFragment: BaseFragment<FragmentEmailVerificationBinding, EmptyViewModel>(
    R.layout.fragment_email_verification,
    EmptyViewModel::class
) {

    override fun setUpDataBinding(
        mBinding: FragmentEmailVerificationBinding,
        mViewModel: EmptyViewModel
    ) {
        with(mBinding) {
            btnGoToEmail.setOnClickListener {
                navigator.openInboxMail(requireActivity())
            }
        }
    }

    override fun observeViewModel(mViewModel: EmptyViewModel) {}

}