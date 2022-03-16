package com.etsisi.appquitectura.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class QuestionBO(
    val title: String
) : Parcelable
