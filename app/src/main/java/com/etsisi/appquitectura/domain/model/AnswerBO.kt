package com.etsisi.appquitectura.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AnswerBO(
    val title: String,
    val correct: Boolean
): Parcelable
