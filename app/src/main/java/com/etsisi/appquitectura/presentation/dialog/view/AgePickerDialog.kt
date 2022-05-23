package com.etsisi.appquitectura.presentation.dialog.view

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import java.time.LocalDate

class AgePickerDialog : DialogFragment() {

    private var observer: DatePickerDialog.OnDateSetListener? = null

    private val day: Int?
        @RequiresApi(Build.VERSION_CODES.O)
        get() = runCatching { LocalDate.now().dayOfMonth }.getOrNull()

    private val month: Int?
        @RequiresApi(Build.VERSION_CODES.O)
        get() = runCatching { LocalDate.now().monthValue }.getOrNull()

    private val year: Int?
        @RequiresApi(Build.VERSION_CODES.O)
        get() = runCatching { LocalDate.now().year }.getOrNull()

    companion object {
        const val TAG = "AgePickerDialog"

        fun newInstance(
            listener: DatePickerDialog.OnDateSetListener
        ) = AgePickerDialog().apply {
             observer = listener
        }
    }

    @SuppressLint("NewApi")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return if (day != null && month != null && year != null) {
            DatePickerDialog(requireContext(), observer, year!!, month!!, day!!)
        } else {
            DatePickerDialog(requireContext())
        }
    }
}