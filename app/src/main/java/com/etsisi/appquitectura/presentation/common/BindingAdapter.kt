package com.etsisi.appquitectura.presentation.common

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.etsisi.appquitectura.R
import com.etsisi.appquitectura.data.model.enums.ScoreLevel
import com.etsisi.appquitectura.domain.model.AnswerBO
import com.etsisi.appquitectura.domain.model.QuestionBO
import com.etsisi.appquitectura.presentation.ui.main.game.model.ItemGameMode
import com.etsisi.appquitectura.domain.enums.GameType
import com.etsisi.appquitectura.presentation.utils.TAG
import com.etsisi.appquitectura.presentation.utils.getMethodName

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

    @BindingAdapter("imageUrl", "fallbackDrawable", requireAll = false)
    @JvmStatic
    fun ImageView.setImageUrl(url: Any?, drawable: Int? = null) {
        if (url != null) {
            Glide.with(this)
                .load(url)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        Log.e(TAG, "${getMethodName(object {}.javaClass)} $e")
                        drawable?.let { setImageResource(it) }
                        return true                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }

                })
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
            text = context.getString(R.string.profile_level_value, it.number)
        }
    }

    @BindingAdapter("gameMode")
    @JvmStatic
    fun TextView.setGameMode(gameMode: ItemGameMode) {
        with(context) {
            text = when(val action = gameMode.action) {
                GameType.WeeklyGame -> getString(R.string.game_mode_weekly)
                is GameType.TestGame -> getString(R.string.game_mode_test)
                else -> {
                    val total = (action as? GameType.ClassicGame)?.classicType?.numberOfQuestions
                    getString(R.string.game_mode_classic, total)
                }
            }
        }
    }
}