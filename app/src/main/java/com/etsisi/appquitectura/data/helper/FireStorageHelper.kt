package com.etsisi.appquitectura.data.helper

import com.etsisi.appquitectura.utils.Constants
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.io.File
import java.io.FileInputStream

object FireStorageHelper {

    val db by lazy { FirebaseStorage.getInstance() }
    val imagesRef by lazy { db.getReference().child(Constants.imagesRef) }

    fun uploadImage(id: String, imagePath: String): UploadTask {
        val stream = FileInputStream(File(imagePath))
        val ref = getImageReference(id)
        return ref.putStream(stream)
        /*uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            ref.downloadUrl
        }.addOnSuccessListener { taskSnapshot ->
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            // ...
        }*/
    }

    fun getImageReference(id: String): StorageReference {
        return imagesRef.child(id)
    }
}