package com.bootcamp.locationloggerapp.m.ui.repository.interfaces

import com.bootcamp.locationloggerapp.m.ui.utils.Response

interface AuthFirebaseDataSource {
    suspend fun loginUserAuth(email: String, password: String): Response<String>
    suspend fun createUserAuth(email: String, password: String): Response<String>
}
