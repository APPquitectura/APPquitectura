package com.etsisi.analytics

import com.etsisi.analytics.enums.LoginType
import com.etsisi.analytics.enums.ScreenType

interface IFirebaseAnalytics {

    fun onNewScreen(screenType: ScreenType, activity: String)

    fun onLogin(loginType: LoginType)
    fun onsignUp()

    fun onItemGameModeClick(mode: String)
    fun onPostScore(level: Int, now: Int)

    fun onItemHomeMenuClick(itemId: String, contentType: String)
}