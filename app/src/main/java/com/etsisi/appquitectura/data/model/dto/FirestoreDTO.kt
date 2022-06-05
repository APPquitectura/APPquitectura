package com.etsisi.appquitectura.data.model.dto

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ServerTimestamp

open class FirestoreDTO (
    @ServerTimestamp
    private val timestamp: FieldValue = FieldValue.serverTimestamp()
)