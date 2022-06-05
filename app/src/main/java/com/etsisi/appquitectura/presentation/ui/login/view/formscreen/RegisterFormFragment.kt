package com.etsisi.appquitectura.presentation.ui.login.view.formscreen

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.graphics.drawable.Drawable
import android.os.Build
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.DatePicker
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import java.time.LocalDate
import com.etsisi.appquitectura.R
import com.etsisi.appquitectura.databinding.FragmentRegisterBinding
import com.etsisi.appquitectura.presentation.common.BaseFragment
import com.etsisi.appquitectura.presentation.common.LiveEventObserver
import com.etsisi.appquitectura.presentation.dialog.enums.DialogType
import com.etsisi.appquitectura.presentation.dialog.view.AgePickerDialog
import com.etsisi.appquitectura.presentation.ui.login.enums.RegisterError
import com.etsisi.appquitectura.presentation.ui.login.viewmodel.RegisterViewModel
import com.etsisi.appquitectura.presentation.utils.EMPTY
import com.etsisi.appquitectura.presentation.utils.ERROR
import com.etsisi.appquitectura.presentation.utils.hideKeyboard
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showDrawable
import java.time.format.DateTimeFormatter

class RegisterFormFragment : BaseFragment<FragmentRegisterBinding, RegisterViewModel>(
    R.layout.fragment_register,
    RegisterViewModel::class
) {

    override fun setUpDataBinding(mBinding: FragmentRegisterBinding, mViewModel: RegisterViewModel) {
        mBinding.apply {
            viewModel = mViewModel
            lifecycleOwner = viewLifecycleOwner
            lifecycle.addObserver(mViewModel)
            (courseSpinnerView as? AutoCompleteTextView)?.apply {
                setAdapter(ArrayAdapter(
                    requireContext(),
                    R.layout.item_array,
                    mViewModel.courseOptions
                ))
                setOnItemClickListener { parent, view, position, id ->
                    mViewModel.setCourseSelected(position)
                }
                setText(adapter.getItem(0).toString(), false)
            }
            (genderSpinnerView as? AutoCompleteTextView)?.apply {
                setAdapter(ArrayAdapter(
                    requireContext(),
                    R.layout.item_array,
                    mViewModel.getGenreOptions(requireContext())
                ))
                setOnItemClickListener { parent, view, position, id ->
                    mViewModel.setGenreSelected(position)
                }
                setText(adapter.getItem(0).toString(), false)
            }
            etAge.apply {
                setOnClickListener {
                    showDatePickerDialog()
                }
                isVisible = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
                if (!isVisible) {
                   setText(String.EMPTY)
                }
            }
            executePendingBindings()
        }
    }

    override fun observeViewModel(mViewModel: RegisterViewModel) {
        with(mViewModel){
            inputDataError.observe(viewLifecycleOwner, LiveEventObserver {
                with(mBinding) {
                    when(it) {
                        RegisterError.EMPTY_NAME -> nameLayout.error = String.ERROR
                        RegisterError.EMPTY_SURNAME -> surnameLayout.error = String.ERROR
                        RegisterError.EMPTY_ACADEMIC_RECORD -> academicRecordLayout.error = String.ERROR
                        RegisterError.EMPTY_ACADEMIC_GROUP -> academicGroupLayout.error = String.ERROR
                        RegisterError.EMPTY_CITY -> cityLayout.error = String.ERROR
                        RegisterError.EMPTY_AGE -> ageLayout.error = String.ERROR
                    }
                }
            })

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
                navigator.openVerifyEmailFragment(name.value.orEmpty())
            }

            onError.observe(viewLifecycleOwner, LiveEventObserver { dialogConfig ->
                navigator.openLoginDialog(dialogConfig, DialogType.LOGIN_ERROR)
            })
        }
    }

    private fun  btnDrawable(): Drawable = ContextCompat
        .getDrawable(requireContext(), R.drawable.ic_check_round_selected)
        ?.apply {
            setBounds(0, 0, 50, 50)
        }!!


    private fun showDatePickerDialog() {
        AgePickerDialog.newInstance(listener = object : DatePickerDialog.OnDateSetListener {
            @SuppressLint("NewApi")
            override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                mViewModel.setDateField(LocalDate.of(year, month, dayOfMonth).format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
            }
        }).show(requireActivity().supportFragmentManager, AgePickerDialog.TAG)
    }
}