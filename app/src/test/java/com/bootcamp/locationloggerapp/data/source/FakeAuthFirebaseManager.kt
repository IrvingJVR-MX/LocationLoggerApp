package com.bootcamp.locationloggerapp.data.source

import com.bootcamp.locationloggerapp.m.ui.repository.interfaces.AuthFirebaseDataSource
import com.bootcamp.locationloggerapp.m.ui.utils.Response

class FakeAuthFirebaseManager: AuthFirebaseDataSource {
    override suspend fun loginUserAuth(email: String, password: String): Response<String> {
        return Response.Success("success")
    }

    override suspend fun createUserAuth(email: String, password: String): Response<String> {
        return Response.Success("1")
    }
}