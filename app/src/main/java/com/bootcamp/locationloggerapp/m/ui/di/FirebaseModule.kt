package com.bootcamp.locationloggerapp.m.ui.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.bootcamp.locationloggerapp.m.ui.repository.managers.AuthFirebaseManager
import com.bootcamp.locationloggerapp.m.ui.repository.managers.FirebaseManager
import com.bootcamp.locationloggerapp.m.ui.repository.managers.FirebaseStorageManager
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object FirebaseModule {

    @Singleton
    @Provides
    fun provideAuthFirebaseManager(auth: FirebaseAuth): AuthFirebaseManager {
        return AuthFirebaseManager(auth)
    }

    @Singleton
    @Provides
    fun provideFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }


    @Singleton
    @Provides
    fun provideFirebaseManager(db: FirebaseFirestore): FirebaseManager {
        return FirebaseManager(db)
    }

    @Singleton
    @Provides
    fun provideFirebaseFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Singleton
    @Provides
    fun provideFirebaseStorageManager(
        firebaseStorage: FirebaseStorage,
        firebaseAuth: FirebaseAuth
    ): FirebaseStorageManager {
        return FirebaseStorageManager(firebaseStorage, firebaseAuth)
    }

    @Singleton
    @Provides
    fun provideFirebaseStorage(): FirebaseStorage {
        return FirebaseStorage.getInstance()
    }

}