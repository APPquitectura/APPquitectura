package com.etsisi.appquitectura.domain.model

import android.os.Parcelable
import com.etsisi.appquitectura.data.helper.FireStorageHelper
import com.etsisi.appquitectura.data.model.entities.QuestionEntity
import com.etsisi.appquitectura.domain.enums.QuestionLevel
import com.etsisi.appquitectura.domain.enums.QuestionSubject
import com.etsisi.appquitectura.domain.enums.QuestionTopic
import kotlinx.parcelize.Parcelize

@Parcelize
data class QuestionBO(
    val id: String?,
    val title: String?,
    val subject: QuestionSubject,
    val level: QuestionLevel,
    val labels: List<QuestionTopic>,
    val imageRef: String,
    val answers: List<AnswerBO>
): Parcelable {

    fun getImageFirestorageReference() = FireStorageHelper.getImageReference(imageRef)

    fun toEntity() = QuestionEntity(
        id = id.orEmpty(),
        title = title.orEmpty(),
        subject = subject.value,
        level = level.value,
        labels = labels.map { it.value }.joinToString(","),
        imageRef = imageRef,
        answers = answers
    )
}
