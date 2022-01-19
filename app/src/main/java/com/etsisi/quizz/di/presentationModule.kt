package com.etsisi.quizz.di

import androidx.appcompat.app.AppCompatActivity
import com.etsisi.quizz.presentation.common.Navigator
import org.koin.dsl.module

val presentationModule = module {
    factory { (activity: AppCompatActivity) -> Navigator(activity = activity)}
}