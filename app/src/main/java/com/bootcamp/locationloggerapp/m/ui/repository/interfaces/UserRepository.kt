package com.bootcamp.locationloggerapp.m.ui.repository.interfaces

import com.bootcamp.locationloggerapp.m.ui.utils.Response


interface UserRepository {
    suspend fun createUser(
        name: String,
        email: String,
        photoUrl: String,
        photoPath: String
    ): Response<String>

    suspend fun createUserAuth(email: String, password: String): Response<String>
    suspend fun signInWithEmailAndPassword(email: String, password: String): Response<String>
    fun signOut(): Response<String>
}
