package com.etsisi.appquitectura.domain.model

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object CurrentUser: KoinComponent {
    private val auth: FirebaseAuth by inject()

    val currentUserInstance: FirebaseUser?
        get() = auth.currentUser

    val isEmailVerfied: Boolean
        get() = auth.currentUser?.isEmailVerified ?: false

    val name: String?
        get() = auth.currentUser?.displayName

    val email: String?
        get() = auth.currentUser?.email

    val photoUrl: Uri?
        get() = auth.currentUser?.photoUrl

    val userUid: String?
        get() = auth.currentUser?.uid
}