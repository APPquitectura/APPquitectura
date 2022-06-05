package com.etsisi.analytics.di

import com.etsisi.analytics.IFirebaseAnalytics
import com.etsisi.analytics.FirebaseAnalyticsImp
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import org.koin.dsl.module

val analyticsModule = module {
    single<IFirebaseAnalytics> {
        FirebaseAnalyticsImp(analytics = Firebase.analytics)
    }
}