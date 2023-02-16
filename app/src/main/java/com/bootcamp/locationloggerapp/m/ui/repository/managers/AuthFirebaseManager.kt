package com.bootcamp.locationloggerapp.m.ui.repository.managers

import com.bootcamp.locationloggerapp.m.ui.utils.Response
import com.google.firebase.auth.*
import com.bootcamp.locationloggerapp.m.ui.repository.interfaces.AuthFirebaseDataSource
import kotlinx.coroutines.tasks.await


class AuthFirebaseManager(private val auth: FirebaseAuth) : AuthFirebaseDataSource {
    override suspend fun loginUserAuth(email: String, password: String): Response<String> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            return Response.Success(result.toString())
        } catch (e: Exception) {
            Response.Error(e.message.toString(), null)
        }
    }

    override suspend fun createUserAuth(email: String, password: String): Response<String> {
        return try {
            auth.createUserWithEmailAndPassword(email, password).await()
            val userId = auth.currentUser?.uid.let { auth.currentUser?.uid } ?: ""
            return Response.Success(userId)
        } catch (e: Exception) {
            Response.Error(e.message.toString(), null)
        }
    }

    fun singOut(): Response<String> {
        return try {
            val logout = auth.signOut()
            return Response.Success(logout.toString())
        } catch (e: Exception) {
            Response.Error(e.message.toString(), null)
        }
    }
}
