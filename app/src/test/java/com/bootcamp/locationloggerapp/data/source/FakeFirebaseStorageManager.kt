package com.bootcamp.locationloggerapp.data.source

import android.net.Uri
import com.bootcamp.locationloggerapp.m.ui.repository.interfaces.FirebaseStorageSource
import com.bootcamp.locationloggerapp.m.ui.utils.Response

class FakeFirebaseStorageManager: FirebaseStorageSource {
    override suspend fun saveImage(uri: Uri, path: String): Response<String> {
        return Response.Success("success")
    }

    override suspend fun deleteImage(path: String): Response<Boolean> {
        return  Response.Success(true)
    }
}