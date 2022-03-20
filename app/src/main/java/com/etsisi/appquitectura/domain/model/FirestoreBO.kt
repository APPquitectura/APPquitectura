package com.etsisi.appquitectura.domain.model

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ServerTimestamp

open class FirestoreBO (
    @ServerTimestamp
    private val timestamp: FieldValue = FieldValue.serverTimestamp()
)