package com.bootcamp.locationloggerapp.m.ui.repository.managers

import com.bootcamp.locationloggerapp.m.ui.repository.model.User
import com.bootcamp.locationloggerapp.m.ui.utils.FirebaseCollections
import com.bootcamp.locationloggerapp.m.ui.utils.Response
import com.bootcamp.locationloggerapp.m.ui.repository.interfaces.UserRepository
import com.google.firebase.auth.FirebaseAuth

class UserManager(
    private val authManager: AuthFirebaseManager,
    private val firebaseManager: FirebaseManager
) :
    UserRepository {
    override suspend fun createUser(
        name: String,
        email: String,
        photoUrl: String,
        photoPath: String
    ): Response<String> {
        val fireAuth = FirebaseAuth.getInstance()
        val userid = fireAuth.currentUser!!.uid
        val user = User(userid, name, email, photoUrl, photoPath)
        return firebaseManager.addDocumentWithId(user, FirebaseCollections.Users, userid)
    }

    override suspend fun createUserAuth(email: String, password: String): Response<String> {
        return authManager.createUserAuth(email, password)
    }

    override suspend fun signInWithEmailAndPassword(
        email: String,
        password: String
    ): Response<String> {
        return authManager.loginUserAuth(email, password)
    }

    override fun signOut(): Response<String> {
        return authManager.singOut()
    }
}
