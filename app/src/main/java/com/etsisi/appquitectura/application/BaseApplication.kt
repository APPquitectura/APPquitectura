package com.etsisi.appquitectura.application

import android.app.Application
import com.etsisi.appquitectura.BuildConfig
import com.etsisi.appquitectura.di.databaseModule
import com.etsisi.appquitectura.di.localDataSourceModule
import com.etsisi.appquitectura.di.preferencesModule
import com.etsisi.appquitectura.di.presentationModule
import com.etsisi.appquitectura.di.remoteDataSourceModule
import com.etsisi.appquitectura.di.repositoryModule
import com.etsisi.appquitectura.di.useCaseModule
import com.etsisi.appquitectura.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class BaseApplication: Application() {

    companion object {
        const val appDatabaseVersion: Int = BuildConfig.DATABASE_VERSION
    }

    override fun onCreate() {
        super.onCreate()
        initKoin()
    }

    private fun initKoin() {
        startKoin {
            androidContext(this@BaseApplication)
            modules(
                listOf(
                    viewModelModule,
                    presentationModule,
                    useCaseModule,
                    repositoryModule,
                    remoteDataSourceModule,
                    localDataSourceModule,
                    databaseModule,
                    preferencesModule
                )
            )
        }
    }
}