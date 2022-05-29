package com.etsisi.analytics

import com.etsisi.analytics.enums.LoginType
import com.etsisi.analytics.enums.ScreenType
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent

class FirebaseAnalyticsImp(
    private val analytics: FirebaseAnalytics
): IFirebaseAnalytics {

    override fun onNewScreen(screenType: ScreenType, activity: String) {
        analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW){
            param(FirebaseAnalytics.Param.SCREEN_NAME, screenType.screen)
            param(FirebaseAnalytics.Param.SCREEN_CLASS, activity)
        }
    }

    override fun onLogin(loginType: LoginType) {
        analytics.logEvent(FirebaseAnalytics.Event.LOGIN){
            param(FirebaseAnalytics.Param.METHOD, loginType.method)
        }
    }

    override fun onsignUp() {
        analytics.logEvent(FirebaseAnalytics.Event.SIGN_UP){
            param(FirebaseAnalytics.Param.METHOD, "By google or form")
        }
    }

    override fun onItemGameModeClick(mode: String) {
        analytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT){
            param(FirebaseAnalytics.Param.ITEM_ID, "id")
            param(FirebaseAnalytics.Param.CONTENT_TYPE, mode)
        }
    }

    override fun onPostScore(level: Int, now: Int) {
        analytics.logEvent(FirebaseAnalytics.Event.POST_SCORE){
            param(FirebaseAnalytics.Param.SCORE, now.toLong())
            param(FirebaseAnalytics.Param.LEVEL, level.toLong())
        }
    }

    override fun onItemHomeMenuClick(itemId: String, contentType: String) {
        analytics.logEvent(FirebaseAnalytics.Event.SELECT_ITEM) {
            param(FirebaseAnalytics.Param.ITEM_ID, itemId)
            param(FirebaseAnalytics.Param.ITEM_NAME, "name")
            param(FirebaseAnalytics.Param.CONTENT_TYPE, contentType)

        }
    }
}