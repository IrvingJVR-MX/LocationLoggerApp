package com.bootcamp.locationloggerapp.m.ui.repository.interfaces

import android.net.Uri
import com.bootcamp.locationloggerapp.m.ui.repository.model.User
import com.bootcamp.locationloggerapp.m.ui.utils.FirebaseCollections
import com.bootcamp.locationloggerapp.m.ui.utils.Response

interface FirebaseStorageSource {
    suspend fun saveImage(uri: Uri, path: String): Response<String>
    suspend fun deleteImage(path: String): Response<Boolean>
}