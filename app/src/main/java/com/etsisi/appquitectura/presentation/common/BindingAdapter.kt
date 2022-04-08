package com.etsisi.appquitectura.presentation.common

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.etsisi.appquitectura.R
import com.etsisi.appquitectura.domain.model.AnswerBO
import com.etsisi.appquitectura.domain.model.QuestionBO
import com.etsisi.appquitectura.presentation.utils.TAG
import com.etsisi.appquitectura.presentation.utils.getMethodName
import com.google.firebase.storage.StorageReference

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
}