package com.etsisi.appquitectura.presentation.ui.main.game.view

import android.graphics.drawable.Drawable
import android.os.CountDownTimer
import android.util.Log
import android.widget.Toast
import androidx.core.os.bundleOf
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.etsisi.appquitectura.R
import com.etsisi.appquitectura.databinding.FragmentQuestionBinding
import com.etsisi.appquitectura.domain.model.QuestionBO
import com.etsisi.appquitectura.presentation.common.BaseFragment
import com.etsisi.appquitectura.presentation.common.BindingAdapter.setImageUrl
import com.etsisi.appquitectura.presentation.common.EmptyViewModel
import com.etsisi.appquitectura.presentation.common.GameListener
import com.etsisi.appquitectura.presentation.ui.main.game.adapter.AnswersAdapter
import com.etsisi.appquitectura.presentation.utils.TAG
import com.etsisi.appquitectura.presentation.utils.getMethodName
import java.util.concurrent.TimeUnit

class QuestionFragment(
    private val listener: GameListener?,
    private val questionBO: QuestionBO
) : BaseFragment<FragmentQuestionBinding, EmptyViewModel>(
    R.layout.fragment_question,
    EmptyViewModel::class
) {
    private val counter = object : CountDownTimer(10000, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            with(mBinding) {
                progressText.text = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished).toString()
            }
        }

        override fun onFinish() {
            listener?.setNexQuestion()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(question: QuestionBO, listener: GameListener?) =
            QuestionFragment(listener, question)
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
            imageQuestion.apply {
                Glide.with(this)
                    .load(questionBO.getImageFirestorageReference())
                    .centerInside()
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            Log.e(TAG, "${getMethodName(object {}.javaClass)} $e")
                            return true
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            counter.start()
                            return false
                        }
                    })
                    .into(this)
            }
        }
    }

    override fun observeViewModel(mViewModel: EmptyViewModel) {
        //TODO("Not yet implemented")
    }
}