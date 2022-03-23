package com.etsisi.appquitectura.presentation.ui.login.view.formscreen

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import com.etsisi.appquitectura.R
import com.etsisi.appquitectura.databinding.FragmentRegisterBinding
import com.etsisi.appquitectura.presentation.common.BaseFragment
import com.etsisi.appquitectura.presentation.common.LiveEventObserver
import com.etsisi.appquitectura.presentation.ui.login.enums.RegisterError
import com.etsisi.appquitectura.presentation.ui.login.viewmodel.RegisterViewModel
import com.etsisi.appquitectura.presentation.utils.hideKeyboard
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showDrawable

class RegisterFormFragment : BaseFragment<FragmentRegisterBinding, RegisterViewModel>(
    R.layout.fragment_register,
    RegisterViewModel::class
), AdapterView.OnItemSelectedListener {

    override fun setUpDataBinding(mBinding: FragmentRegisterBinding, mViewModel: RegisterViewModel) {
        mBinding.apply {
            viewModel = mViewModel
            lifecycleOwner = viewLifecycleOwner
            lifecycle.addObserver(mViewModel)
            ArrayAdapter.createFromResource(
                requireContext(), R.array.years_array,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                yearSpinner.adapter = adapter
                yearSpinner.onItemSelectedListener = this@RegisterFormFragment
            }
            executePendingBindings()
        }
    }

    override fun observeViewModel(mViewModel: RegisterViewModel) {
        with(mViewModel){
            loaded.observe(viewLifecycleOwner){ loaded ->
                if (loaded) {
                    requireContext().hideKeyboard(mBinding.form.etPassword)
                    mBinding.btnRegister.showDrawable(btnDrawable()) {
                        buttonText = getString(R.string.done)
                    }
                } else {
                    mBinding.btnRegister.hideProgress(getString(R.string.register_btn_text))
                }
            }

            onSuccessRegister.observe(viewLifecycleOwner, LiveEventObserver { navType ->
                requireContext().hideKeyboard(mBinding.btnRegister)
            })

            emailVerificationSended.observe(viewLifecycleOwner) {
                navigator.openVerifyEmailFragment()
            }

            onError.observe(viewLifecycleOwner, LiveEventObserver { dialogConfig ->
                navigator.openDialog(dialogConfig)
            })
        }
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
        parent.getItemAtPosition(position)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        mViewModel.setError(RegisterError.YEAR_UNSELECTED.value)
    }

    private fun  btnDrawable(): Drawable = ContextCompat
        .getDrawable(requireContext(), R.drawable.ic_check_round_selected)
        ?.apply {
            setBounds(0, 0, 50, 50)
        }!!
}