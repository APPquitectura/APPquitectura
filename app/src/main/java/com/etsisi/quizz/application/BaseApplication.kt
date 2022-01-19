package com.etsisi.quizz.application

import android.app.Application

class BaseApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        //initialize di
        //initialize preferences
    }
}