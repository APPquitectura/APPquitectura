package com.etsisi.appquitectura.presentation.dialog.view

import androidx.navigation.fragment.navArgs
import com.etsisi.appquitectura.R
import com.etsisi.appquitectura.databinding.DialogNavigationBinding
import com.etsisi.appquitectura.presentation.common.BaseDialog
import com.etsisi.appquitectura.presentation.common.DialogListener
import com.etsisi.appquitectura.presentation.common.EmptyViewModel
import com.etsisi.appquitectura.presentation.dialog.enums.DialogType

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
            buttonsListener = object : DialogListener {
                override fun onPositiveButtonClicked() {
                    when (args.dialogType) {
                        DialogType.WARNING_LEAVING_GAME -> {
                            navigator.navigateToHome()
                        }
                    }
                }

                override fun onNegativeButtonClicked() {
                    dismiss()
                }

            }
        }
    }

    override fun observeViewModel(viewModel: EmptyViewModel) {}
}