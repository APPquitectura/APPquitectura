package com.etsisi.appquitectura.utils

object Constants {
    const val DYNAMIC_LINK_PREFIX = "etsisi.page.link"
    const val sendEmailVerificationDeeplink = "https://etsisi.page.link/verify_mail"
    const val resetPasswordDeepLink = "https://www.appquitectura.com/resetPassword"




    //Firestore
    const val users_collection = "users"
    const val questions_collection = "questions"
    const val score_collection = "score"
    const val general_ranking_collection = "score" + "/" + "ranking" + "/" + "general"

    //Firebase Storage
    const val imagesRef = "images"

    //ROOM
    const val DATABASE_NAME = "appquitectura-db"

}