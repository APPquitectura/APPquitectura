package com.etsisi.analytics

import androidx.core.os.bundleOf
import com.etsisi.analytics.enums.AppQuitecturaEventParams
import com.etsisi.analytics.enums.LoginType
import com.etsisi.analytics.enums.SignUpType
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent

class FirebaseAnalyticsImp(
    private val analytics: FirebaseAnalytics
) : IFirebaseAnalytics {

    override fun onLogin(email: String, loginType: LoginType) {
        analytics.apply {
            setUserId(email)
            logEvent(FirebaseAnalytics.Event.LOGIN) {
                param(FirebaseAnalytics.Param.METHOD, loginType.method)
            }
        }
    }

    override fun onSignUp(
        type: SignUpType,
        email: String,
        course: String,
        city: String,
        academicGroup: String,
        academicRecord: String
    ) {
        analytics.apply {
            logEvent(FirebaseAnalytics.Event.SIGN_UP) {
                param(FirebaseAnalytics.Param.METHOD, type.method)
            }
            setUserId(email)
            setUserProperty(AppQuitecturaEventParams.COURSE.key, course)
            setUserProperty(AppQuitecturaEventParams.CITY.key, city)
            setUserProperty(AppQuitecturaEventParams.ACADEMIC_GROUP.key, academicGroup)
            setUserProperty(AppQuitecturaEventParams.ACADEMIC_RECORD.key, academicRecord)
        }
    }

    override fun onItemGameModeClick(
        mode: String,
        totalQuestions: Int,
        topics: List<String>,
        level: String
    ) {
        analytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT) {
            param(AppQuitecturaEventParams.GAME_MODE_SELECTED.key, bundleOf(
                    AppQuitecturaEventParams.GAME_MODE_SELECTED.key to mode,
                    AppQuitecturaEventParams.TOTAL_QUESTIONS.key to totalQuestions,
                    AppQuitecturaEventParams.QUESTION_TOPICS.key to topics,
                    AppQuitecturaEventParams.GAME_LEVEL.key to level
                )
            )
        }
    }

    override fun onPostScore(correctAnswers: Int, experience: Int, rankingPointsEarned: Int, rankingType: String) {
        analytics.apply {
            logEvent(FirebaseAnalytics.Event.POST_SCORE) {
                param(FirebaseAnalytics.Param.SCORE, rankingPointsEarned.toLong())
                param(FirebaseAnalytics.Param.LEVEL, experience.toLong())
                param(AppQuitecturaEventParams.SCORE_CORRECT_ANSWERS.key, correctAnswers.toLong())
                param(AppQuitecturaEventParams.SCORE_EXPERIENCE.key, experience.toLong())
                param(AppQuitecturaEventParams.SCORE_RANKING_POINTS.key, rankingPointsEarned.toLong())
                param(AppQuitecturaEventParams.RANKING_TYPE.key, rankingType)
            }
        }
    }

    override fun onItemHomeMenuClick(itemId: String, contentType: String) {
        analytics.logEvent(FirebaseAnalytics.Event.SELECT_ITEM) {
            param(FirebaseAnalytics.Param.SCREEN_NAME, "Home")
            param(FirebaseAnalytics.Param.ITEM_ID, itemId)
            param(FirebaseAnalytics.Param.CONTENT_TYPE, contentType)
        }
    }
}