package com.etsisi.appquitectura.presentation.ui.main.view

import android.os.CountDownTimer
import com.etsisi.appquitectura.R
import com.etsisi.appquitectura.databinding.FragmentQuestionBinding
import com.etsisi.appquitectura.presentation.common.BaseFragment
import com.etsisi.appquitectura.presentation.common.EmptyViewModel
import java.util.concurrent.TimeUnit

class QuestionFragment : BaseFragment<FragmentQuestionBinding, EmptyViewModel>(
    R.layout.fragment_question,
    EmptyViewModel::class
) {
    private val counter = object : CountDownTimer(30000, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            with(mBinding) {
                progressText.text = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished).toString()
            }
        }

        override fun onFinish() {
            //TODO("Not yet implemented")
        }

    }

    companion object {
        @JvmStatic
        fun newInstance() = QuestionFragment()
    }

    override fun setUpDataBinding(mBinding: FragmentQuestionBinding, mViewModel: EmptyViewModel) {
        counter.start()
    }

    override fun observeViewModel(mViewModel: EmptyViewModel) {
        //TODO("Not yet implemented")
    }
}