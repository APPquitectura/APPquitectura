package com.etsisi.appquitectura.presentation.ui.main.game.view

import android.os.CountDownTimer
import android.widget.Toast
import androidx.core.os.bundleOf
import com.etsisi.appquitectura.R
import com.etsisi.appquitectura.databinding.FragmentQuestionBinding
import com.etsisi.appquitectura.domain.model.QuestionBO
import com.etsisi.appquitectura.presentation.common.BaseFragment
import com.etsisi.appquitectura.presentation.common.EmptyViewModel
import com.etsisi.appquitectura.presentation.ui.main.game.adapter.AnswersAdapter
import java.util.concurrent.TimeUnit

class QuestionFragment(
    private val questionBO: QuestionBO
) : BaseFragment<FragmentQuestionBinding, EmptyViewModel>(
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
        fun newInstance(question: QuestionBO) = QuestionFragment(question)
    }

    override fun setUpDataBinding(mBinding: FragmentQuestionBinding, mViewModel: EmptyViewModel) {
        mBinding.apply {
            question = questionBO
            answersRecyclerView.adapter = AnswersAdapter(
                listener = {
                    Toast.makeText(context, "${it.title} is ${it.correct}", Toast.LENGTH_SHORT)
                        .show()
                }
            ).also {
                it.addDataSet(questionBO.answers.asSequence().shuffled().toList())
            }
        }
        counter.start()
    }

    override fun observeViewModel(mViewModel: EmptyViewModel) {
        //TODO("Not yet implemented")
    }
}