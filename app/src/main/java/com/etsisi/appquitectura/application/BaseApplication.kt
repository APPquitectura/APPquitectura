package com.etsisi.appquitectura.application

import android.app.Application
import com.etsisi.appquitectura.di.presentationModule
import com.etsisi.appquitectura.di.useCaseModule
import com.etsisi.appquitectura.di.viewModelModule
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
                    presentationModule,
                    useCaseModule
                )
            )
        }
    }
}