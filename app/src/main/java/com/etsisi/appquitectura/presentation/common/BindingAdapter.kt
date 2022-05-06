package com.etsisi.appquitectura.presentation.common

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.databinding.BindingAdapter
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.etsisi.appquitectura.R
import com.etsisi.appquitectura.data.model.enums.ScoreLevel
import com.etsisi.appquitectura.domain.model.AnswerBO
import com.etsisi.appquitectura.domain.model.QuestionBO

object BindingAdapter {

    @BindingAdapter("isVisible")
    @JvmStatic
    fun View.isVisible(isVisible: Boolean) {
        this.isVisible = isVisible
    }

    @BindingAdapter("onClickCustom")
    @JvmStatic
    fun View.setCustomOnClick(method: () -> Unit) {
       this.setOnClickListener { method.invoke() }
    }

    @BindingAdapter("textRes")
    @JvmStatic
    fun TextView.textRes(@StringRes res: Int?) {
        res?.let {
            text = this.context.getString(it)
        }
    }

    @BindingAdapter("srcRes")
    @JvmStatic
    fun ImageView.setImageRes(@DrawableRes res: Int?) {
        res?.let {
            val drawable = ContextCompat.getDrawable(context, it)
            setImageDrawable(drawable)
        }
    }

    @BindingAdapter("imageUrl")
    @JvmStatic
    fun ImageView.setImageUrl(url: String?) {
        if (!url.isNullOrEmpty()) {
            Glide.with(this)
                .load(url)
                .into(this)
        }
    }

    @BindingAdapter("dynamicText", "dynamicRes")
    @JvmStatic
    fun TextView.setDynamicText(string: String?, @StringRes res: Int?) {
        if (string != null && res != null) {
            text = context.getString(res, string)
        }
    }

    @BindingAdapter("answersListener", "answerItem", "questionItem")
    @JvmStatic
    fun View.setAnswersBackground(listener: QuestionListener, answerBO: AnswerBO, questionBO: QuestionBO) {
        setOnClickListener {
            if (answerBO.correct) {
                setBackgroundResource(R.drawable.shape_answer_correct)
            } else {
                setBackgroundResource(R.drawable.shape_answer_incorrect)
            }
            listener?.onAnswerClicked(questionBO, answerBO)
        }
    }

    @BindingAdapter("customRawRes")
    @JvmStatic
    fun LottieAnimationView.setCustomRawRes(rawRes: Int?) {
        rawRes?.let { this.setAnimation(it) }
    }

    @BindingAdapter("userLevel")
    @JvmStatic
    fun TextView.setUserLevel(scoreLevel: ScoreLevel?) {
        scoreLevel?.let {
            text = when(it) {
                ScoreLevel.LEVEL_0 -> {
                    "Nivel 0"
                }
                ScoreLevel.LEVEL_1 -> {
                    "Nivel 1"
                }
                ScoreLevel.LEVEL_2 -> {
                    "Nivel 2"
                }
                ScoreLevel.LEVEL_3 -> {
                    "Nivel 3"
                }
                ScoreLevel.LEVEL_4 -> {
                    "Nivel 4"
                }
                ScoreLevel.LEVEL_5 -> {
                    "Nivel 5"
                }
                ScoreLevel.LEVEL_6 -> {
                    "Nivel 6"
                }
                ScoreLevel.LEVEL_7 -> {
                    "Nivel 7"
                }
                ScoreLevel.LEVEL_8 -> {
                    "Nivel 8"
                }
                ScoreLevel.LEVEL_9 -> {
                    "Nivel 9"
                }
                ScoreLevel.LEVEL_10 -> {
                    "Nivel 10"
                }
            }
        }
    }
}