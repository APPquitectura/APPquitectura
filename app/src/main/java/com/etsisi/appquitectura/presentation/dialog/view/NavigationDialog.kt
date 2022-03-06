package com.etsisi.appquitectura.presentation.dialog.view

import androidx.navigation.fragment.navArgs
import com.etsisi.appquitectura.R
import com.etsisi.appquitectura.databinding.DialogNavigationBinding
import com.etsisi.appquitectura.presentation.common.BaseDialog
import com.etsisi.appquitectura.presentation.common.EmptyViewModel

class NavigationDialog: BaseDialog<DialogNavigationBinding, EmptyViewModel>(
    R.layout.dialog_navigation,
    EmptyViewModel::class
) {
    val args: NavigationDialogArgs by navArgs()

    override fun getFragmentArgs(mBinding: DialogNavigationBinding) {
        mBinding.apply {
            config = args.config
        }
    }

    override fun setUpDataBinding(binding: DialogNavigationBinding, viewModel: EmptyViewModel) {
        with(mBinding) {
            lifecycleOwner = viewLifecycleOwner
        }
    }

    override fun observeViewModel(viewModel: EmptyViewModel) {}
}