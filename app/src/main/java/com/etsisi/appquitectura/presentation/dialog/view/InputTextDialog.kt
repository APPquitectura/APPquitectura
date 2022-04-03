package com.etsisi.appquitectura.presentation.dialog.view

import android.widget.Toast
import androidx.navigation.fragment.navArgs
import com.etsisi.appquitectura.R
import com.etsisi.appquitectura.databinding.DialogInputTextBinding
import com.etsisi.appquitectura.presentation.common.BaseDialog
import com.etsisi.appquitectura.presentation.common.LiveEventObserver
import com.etsisi.appquitectura.presentation.dialog.enums.DialogType
import com.etsisi.appquitectura.presentation.dialog.viewmodel.InputTextViewModel

class InputTextDialog: BaseDialog<DialogInputTextBinding, InputTextViewModel>(
        R.layout.dialog_input_text,
        InputTextViewModel::class
) {

    val args: InputTextDialogArgs by navArgs()

    override fun setUpDataBinding(binding: DialogInputTextBinding, viewModel: InputTextViewModel) {
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            this.viewModel = viewModel
            this.config = args.config
            executePendingBindings()
        }
    }

    override fun observeViewModel(viewModel: InputTextViewModel) {
        with(viewModel) {
            onPossitiveButtonClicked.observe(viewLifecycleOwner, LiveEventObserver {
                when(args.dialogType) {
                    DialogType.RESET_PASSWORD -> {
                        resetPassword()
                    }
                }
            })

            onResult.observe(viewLifecycleOwner, LiveEventObserver { success ->
                when(args.dialogType) {
                    DialogType.RESET_PASSWORD -> {
                        if (success) {
                            dismiss()
                        } else {
                            dismiss()
                            Toast.makeText(requireContext(), "error", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            })
        }
    }
}