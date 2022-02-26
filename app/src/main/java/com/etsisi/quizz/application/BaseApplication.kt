package com.etsisi.quizz.application

import android.app.Application
import com.etsisi.quizz.di.presentationModule
import com.etsisi.quizz.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class BaseApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        //initialize di
        initKoin()
        //initialize preferences
    }

    private fun initKoin() {
        startKoin {
            androidContext(this@BaseApplication)
            modules(
                listOf(
                    viewModelModule,
                    presentationModule
                )
            )
        }
    }
}