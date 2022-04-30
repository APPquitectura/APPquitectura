package com.etsisi.appquitectura.data.helper

import android.util.Log
import com.etsisi.appquitectura.presentation.utils.TAG
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

object FirestoreHelper {

    val db by lazy { FirebaseFirestore.getInstance() }

    //Creates document with random Id
    fun writeDocumentWithRandomId(
        collection: String,
        data: Any,
        onSuccess: () -> Unit,
        onError: (error: Exception) -> Unit
    ) {
        db
            .collection(collection)
            .add(data)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(it) }
    }

    //Creates or replace document with custom Id
    fun writeDocument(
        collection: String,
        document: String,
        data: Any,
        onSuccess: () -> Unit,
        onError: (error: Exception) -> Unit
    ) {
        val path = collection + "/" + document
        db
            .document(path)
            .set(data)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(it) }
    }

    //Creates or merge document with custom Id
    fun writeDocumentMerging(
        collection: String,
        document: String,
        data: Any,
        onSuccess: () -> Unit,
        onError: (error: Exception) -> Unit
    ) {
        val path = collection + "/" + document
        db
            .document(path)
            .set(data, SetOptions.merge())
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(it) }
    }

    //Concat with a '.' the embedded fields
    fun updateDocument(
        collection: String,
        document: String,
        data: Map<String, Any?>,
        onSuccess: () -> Unit,
        onError: (error: Exception) -> Unit
    ) {
        val path = collection + "/" + document
        db
            .document(path)
            .update(data)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(it) }
    }

    // Atomically add a new element to the "arrayField" array field.
    fun addArrayNewElement(
        collection: String,
        document: String,
        arrayField: String,
        newElements: List<Any>,
        onSuccess: () -> Unit,
        onError: (error: Exception) -> Unit
    ) {
        val path = collection + "/" + document
        db
            .document(path)
            .update(arrayField, FieldValue.arrayUnion(newElements))
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(it) }
    }

    //Remove all instances of "removeElements" in the array
    fun removeArrayElement(
        collection: String,
        document: String,
        arrayField: String,
        removeElements: List<Any>,
        onSuccess: () -> Unit,
        onError: (error: Exception) -> Unit
    ) {
        val path = collection + "/" + document
        db
            .document(path)
            .update(arrayField, FieldValue.arrayRemove(removeElements))
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(it) }
    }

    //Increments a numeric value
    fun incrementNumberValue(
        collection: String,
        document: String,
        field: String,
        increment: Long,
        onSuccess: () -> Unit,
        onError: (error: Exception) -> Unit
    ) {
        val path = collection + "/" + document
        db
            .document(path)
            .update(field, FieldValue.increment(increment))
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(it) }
    }

    //Deletes specific document but not it's subcollections
    fun deleteSpecificDocument(
        collection: String,
        document: String,
        onSuccess: () -> Unit,
        onError: (error: Exception) -> Unit
    ) {
        val path = collection + "/" + document
        db
            .document(path)
            .delete()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(it) }
    }

    //Retrieves a document of a collection
    inline fun <reified T> readDocument(
        collection: String,
        document: String,
        crossinline onSuccess: (data: T) -> Unit,
        crossinline onError: (error: Exception) -> Unit
    ) {
        val path = collection + "/" + document

        db
            .document(path)
            .get()
            .addOnSuccessListener {
                Log.e("XXX", "readDocument ${it}")
                val result = it.toObject(T::class.java)
                if (result == null) {
                    onError(FirestoreNoDocumentExistsException())
                } else {
                    onSuccess(result)
                }
            }
            .addOnFailureListener {
                Log.e("XXX", "readDocument ${it}")
                Log.e(TAG, it.toString())
                onError(it)
            }
    }

    //Retrieves all documents of a collection
    inline fun <reified T> readDocumentsOfACollection(
        collection: String,
        crossinline onSuccess: (data: List<T>) -> Unit,
        crossinline onError: (error: Exception) -> Unit
    ) {
        db
            .collection(collection)
            .get()
            .addOnSuccessListener {
                val result = it.documents.mapNotNull { document ->
                    document.toObject(T::class.java)
                }
                if (result.isEmpty()) {
                    onError(FirestoreNoDocumentExistsException())
                } else {
                    onSuccess(result)
                }
            }
            .addOnFailureListener { onError(it) }
    }

    //Retrieves all documents with a equal query
    inline fun <reified T> readDocumentsWithQuery(
        collection: String,
        query: Pair<String, Any>,
        crossinline onSuccess: (data: List<T>) -> Unit,
        crossinline onError: (error: Exception) -> Unit
    ) {

        db
            .collection(collection)
            .whereEqualTo(query.first, query.second)
            .get()
            .addOnSuccessListener {
                val result = it.documents.mapNotNull { document ->
                    document.toObject(T::class.java)
                }
                if (result.isEmpty()) {
                    onError(FirestoreNoDocumentExistsException())
                } else {
                    onSuccess(result)
                }
            }
            .addOnFailureListener { onError(it) }

    }

    //Update data with a transaction
    /*
    val sfDocRef = db.collection("cities").document("SF")

db.runTransaction { transaction ->
    val snapshot = transaction.get(sfDocRef)
    val newPopulation = snapshot.getDouble("population")!! + 1
    if (newPopulation <= 1000000) {
        transaction.update(sfDocRef, "population", newPopulation)
        newPopulation
    } else {
        throw FirebaseFirestoreException("Population too high",
                FirebaseFirestoreException.Code.ABORTED)
    }
}.addOnSuccessListener { result ->
    Log.d(TAG, "Transaction success: $result")
}.addOnFailureListener { e ->
    Log.w(TAG, "Transaction failure.", e)
}
     */

    //Write multiple Data

    /*
    val nycRef = db.collection("cities").document("NYC")
val sfRef = db.collection("cities").document("SF")
val laRef = db.collection("cities").document("LA")

// Get a new write batch and commit all write operations
db.runBatch { batch ->
    // Set the value of 'NYC'
    batch.set(nycRef, City())

    // Update the population of 'SF'
    batch.update(sfRef, "population", 1000000L)

    // Delete the city 'LA'
    batch.delete(laRef)
}.addOnCompleteListener {
    // ...
}
     */

}