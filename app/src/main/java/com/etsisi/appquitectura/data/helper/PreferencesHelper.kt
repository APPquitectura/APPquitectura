package com.etsisi.appquitectura.data.helper

import android.content.SharedPreferences
import android.util.Log
import com.etsisi.appquitectura.data.model.enums.PreferenceKeys
import com.etsisi.appquitectura.presentation.utils.TAG
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object PreferencesHelper: KoinComponent {
    val sharedPreferences: SharedPreferences by inject()
    val gson: Gson = Gson()

    fun write(preferenceKey: PreferenceKeys, value: Int) = with(sharedPreferences.edit()) {
        putInt(preferenceKey.key, value)
        apply()
    }
    fun write(preferenceKey: PreferenceKeys, value: Boolean) = with(sharedPreferences.edit()) {
        putBoolean(preferenceKey.key, value)
        apply()
    }
    fun write(preferenceKey: PreferenceKeys, value: String) = with(sharedPreferences.edit()) {
        putString(preferenceKey.key, value)
        apply()
    }
    fun <T: Any> writeObject(preferenceKey: PreferenceKeys, value: T) {
        with(sharedPreferences.edit()) {
            val json = gson.toJson(value)
            putString(preferenceKey.key, json)
            apply()
        }
    }

    inline fun <reified T: Any> readObject(preferenceKey: PreferenceKeys): T? {
        return try {
            val json = sharedPreferences.getString(preferenceKey.key, null)
            val type = object : TypeToken<T>() {}.type
            gson.fromJson<T>(json, type)
        } catch (e: Exception) {
            Log.e(TAG, e.toString())
            null
        }
    }

    fun clear() {
        with(sharedPreferences.edit()) {
            clear()
        }
    }
}