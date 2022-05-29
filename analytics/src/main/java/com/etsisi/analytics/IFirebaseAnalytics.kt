package com.etsisi.analytics

import com.etsisi.analytics.enums.LoginType
import com.etsisi.analytics.enums.SignUpType

interface IFirebaseAnalytics {

    fun onLogin(email:String, loginType: LoginType)
    fun onSignUp(type: SignUpType, email: String, course: String, city: String, academicGroup: String, academicRecord: String)

    fun onItemGameModeClick(mode: String, totalQuestions: Int, topics: List<String>, level: String)
    fun onPostScore(correctAnswers: Int, experience: Int, rankingPointsEarned: Int, rankingType: String)

    fun onItemHomeMenuClick(itemId: String, contentType: String)
}