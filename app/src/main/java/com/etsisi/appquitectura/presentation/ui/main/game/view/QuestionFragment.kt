package com.etsisi.appquitectura.presentation.ui.main.game.view

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.CountDownTimer
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.etsisi.appquitectura.R
import com.etsisi.appquitectura.databinding.FragmentQuestionBinding
import com.etsisi.appquitectura.domain.model.AnswerBO
import com.etsisi.appquitectura.domain.model.QuestionBO
import com.etsisi.appquitectura.presentation.common.BaseFragment
import com.etsisi.appquitectura.presentation.common.BindingAdapter.setImageUrl
import com.etsisi.appquitectura.presentation.common.EmptyViewModel
import com.etsisi.appquitectura.presentation.common.GameListener
import com.etsisi.appquitectura.presentation.common.QuestionListener
import com.etsisi.appquitectura.presentation.ui.main.game.adapter.AnswersAdapter
import com.etsisi.appquitectura.presentation.utils.TAG
import com.etsisi.appquitectura.presentation.utils.getMethodName
import java.util.concurrent.TimeUnit
import kotlin.math.min

class QuestionFragment(
    private val gameListener: GameListener?,
    private val questionBO: QuestionBO
) : BaseFragment<FragmentQuestionBinding, EmptyViewModel>(
    R.layout.fragment_question,
    EmptyViewModel::class
), QuestionListener {
    private var counterMillisUntilFinished = 0L
    private var counter: CountDownTimer? = null
    private val counterTime = MAX_QUESTION_TIME + GRACE_PERIOD

    companion object {
        private const val COUNT_DOWN_INTERVAL = 1000L
        private const val MAX_QUESTION_TIME = 25000L
        private const val THREE_SECONDS = 3000L
        private const val GRACE_PERIOD = 5000L

        @JvmStatic
        fun newInstance(question: QuestionBO, listener: GameListener?) = QuestionFragment(listener, question)
    }

    override fun setUpDataBinding(mBinding: FragmentQuestionBinding, mViewModel: EmptyViewModel) {
        mBinding.apply {
            question = questionBO
            answersRecyclerView.adapter = AnswersAdapter(this@QuestionFragment, questionBO).also {
                it.addDataSet(questionBO.answers)
            }
            imageQuestion.setImageUrl(questionBO.getImageFirestorageReference(), R.drawable.etsam)
            counter = object : CountDownTimer(counterTime, COUNT_DOWN_INTERVAL) {
                override fun onTick(millisUntilFinished: Long) {
                    counterMillisUntilFinished = millisUntilFinished
                    progressText.text = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished).toString()
                    progress.progress--
                    if (millisUntilFinished <= THREE_SECONDS) {
                        progressText.setTextColor(Color.RED)
                    }
                }

                override fun onFinish() {
                    progress.progress = 0
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        counter?.start()
    }

    override fun observeViewModel(mViewModel: EmptyViewModel) {
    }

    override fun onAnswerClicked(question: QuestionBO, answer: AnswerBO) {
        counter?.cancel()
        (mBinding.answersRecyclerView.adapter as? AnswersAdapter)?.showCorrectAnswer()
        gameListener?.onAnswerClicked(question, answer, min(counterMillisUntilFinished, MAX_QUESTION_TIME), counterTime - counterMillisUntilFinished)
    }
}