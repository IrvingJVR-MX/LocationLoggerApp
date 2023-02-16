package com.bootcamp.locationloggerapp.m.ui.di

import com.bootcamp.locationloggerapp.m.ui.repository.interfaces.FirebaseDataSource
import com.bootcamp.locationloggerapp.m.ui.repository.interfaces.FirebaseStorageSource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.bootcamp.locationloggerapp.m.ui.repository.managers.AuthFirebaseManager
import com.bootcamp.locationloggerapp.m.ui.repository.managers.FirebaseManager
import com.bootcamp.locationloggerapp.m.ui.repository.managers.UserManager
import com.bootcamp.locationloggerapp.m.ui.repository.interfaces.UserRepository
import com.bootcamp.locationloggerapp.m.ui.repository.managers.FirebaseStorageManager
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ManagersModule {

    @Singleton
    @Provides
    fun provideUserManager(
        authManager: AuthFirebaseManager,
        firebaseManager: FirebaseManager
    ): UserManager {
        return UserManager(authManager, firebaseManager)
    }

    @Singleton
    @Provides
    fun provideUserRepository(): UserRepository = UserManager(
        AuthFirebaseManager(FirebaseAuth.getInstance()),
        FirebaseManager(FirebaseFirestore.getInstance())
    )

    @Singleton
    @Provides
    fun provideFirebaseManager(firebaseFirestore: FirebaseFirestore): FirebaseManager {
        return FirebaseManager(firebaseFirestore)
    }

    @Singleton
    @Provides
    fun provideFirebaseDataSource(): FirebaseDataSource = FirebaseManager(
        FirebaseFirestore.getInstance()
    )

    @Singleton
    @Provides
    fun provideFirebaseStorage(): FirebaseStorageSource = FirebaseStorageManager(
        FirebaseStorage.getInstance(),
        FirebaseAuth.getInstance()
    )

}