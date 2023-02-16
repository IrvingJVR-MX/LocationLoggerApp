package com.bootcamp.locationloggerapp.m.ui.repository.managers

import android.net.Uri
import com.bootcamp.locationloggerapp.m.ui.repository.interfaces.FirebaseStorageSource
import com.bootcamp.locationloggerapp.m.ui.utils.Response
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

class FirebaseStorageManager(
    private val firebaseStorage: FirebaseStorage,
    private val firebaseAuth: FirebaseAuth
) : FirebaseStorageSource {
    override suspend fun saveImage(uri: Uri, path: String): Response<String> {
        return try {
            var result = ""
            val ref = firebaseStorage.reference.storage.reference
            ref.child(path)
                .putFile(uri)
                .await()
                .storage.downloadUrl.addOnCompleteListener { task ->
                    result = "${task.result}"
                }.await()
            return Response.Success(result)
        } catch (e: Exception) {
            Response.Error(e.message.toString(), null)
        }
    }

    override suspend fun deleteImage(path: String): Response<Boolean> {
        return try {
            var result = false
            val ref = firebaseStorage.reference.storage.reference
            ref.child(path)
                .delete()
                .addOnSuccessListener {
                    result = true
                }
                .await()
            return Response.Success(result)
        } catch (e: Exception) {
            Response.Error(e.message.toString(), null)
        }
    }

}