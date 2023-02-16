package com.bootcamp.locationloggerapp.data.source

import com.bootcamp.locationloggerapp.m.ui.repository.interfaces.UserRepository
import com.bootcamp.locationloggerapp.m.ui.utils.Response

class FakeUserManager: UserRepository {
    override suspend fun createUser(
        name: String,
        email: String,
        photoUrl: String,
        photoPath: String
    ): Response<String> {
        return Response.Success("success")
    }

    override suspend fun createUserAuth(email: String, password: String): Response<String> {
        return Response.Success("success")
    }

    override suspend fun signInWithEmailAndPassword(
        email: String,
        password: String
    ): Response<String> {
        return Response.Success("success")
    }

    override fun signOut(): Response<String> {
        return Response.Success("success")
    }
}