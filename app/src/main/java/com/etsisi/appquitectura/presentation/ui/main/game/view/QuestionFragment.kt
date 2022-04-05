package com.etsisi.appquitectura.presentation.ui.main.game.view

import android.os.CountDownTimer
import androidx.core.os.bundleOf
import com.etsisi.appquitectura.R
import com.etsisi.appquitectura.databinding.FragmentQuestionBinding
import com.etsisi.appquitectura.domain.model.QuestionBO
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
        private const val QUESTION = "question"
        @JvmStatic
        fun newInstance(question: QuestionBO) = QuestionFragment().apply {
            arguments = bundleOf(QUESTION to question)
        }
    }

    override fun getFragmentArgs(mBinding: FragmentQuestionBinding) {
        mBinding.apply {
            question = arguments?.getParcelable(QUESTION)
        }
    }

    override fun setUpDataBinding(mBinding: FragmentQuestionBinding, mViewModel: EmptyViewModel) {
        counter.start()
    }

    override fun observeViewModel(mViewModel: EmptyViewModel) {
        //TODO("Not yet implemented")
    }
}