package com.etsisi.appquitectura.data.datasource.local

import androidx.room.TypeConverter
import com.etsisi.appquitectura.domain.model.AnswerBO
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    val gson by lazy { Gson() }

    @TypeConverter
    fun fromAnswersList(value: List<AnswerBO>): String {
        val type = object : TypeToken<List<AnswerBO>>() {}.type
        return gson.toJson(value, type)
    }

    @TypeConverter
    fun toAnswersList(value: String): List<AnswerBO> {
        val type = object : TypeToken<List<AnswerBO>>() {}.type
        return gson.fromJson<List<AnswerBO>>(value, type)
    }
}