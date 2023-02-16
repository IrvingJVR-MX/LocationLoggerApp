package com.bootcamp.locationloggerapp.m.ui.repository.interfaces

import com.bootcamp.locationloggerapp.m.ui.repository.model.Post
import com.bootcamp.locationloggerapp.m.ui.repository.model.PostDetail
import com.bootcamp.locationloggerapp.m.ui.repository.model.User
import com.bootcamp.locationloggerapp.m.ui.utils.FirebaseCollections
import com.bootcamp.locationloggerapp.m.ui.utils.Response

interface FirebaseDataSource {
    suspend fun getDocId(collection: FirebaseCollections): Response<String>
    suspend fun getUserCollection(collection: FirebaseCollections): Response<MutableList<User>>
    suspend fun <T : Any> addDocument(
        document: T,
        collection: FirebaseCollections
    ): Response<String>

    suspend fun <T : Any> addDocumentWithId(
        document: T,
        collection: FirebaseCollections,
        id: String
    ): Response<String>

    suspend fun getCollectionSnapshot(collection: FirebaseCollections): Response<MutableList<User>>
    suspend fun getNestedCollection(collection: FirebaseCollections): Response<MutableList<PostDetail>>
    suspend fun getPostCollectionWithId(
        collection: FirebaseCollections,
        id: String
    ): Response<MutableList<Post>>

    suspend fun getUserInfo(
        collection: FirebaseCollections,
        id: String
    ): Response<MutableList<User>>

    suspend fun deleteCollectionWithId(
        collection: FirebaseCollections,
        id: String
    ): Response<Boolean>

    suspend fun updateCollectionWithId(
        collection: FirebaseCollections,
        id: String,
        name: String,
        profilePhotoUrl: String,
        profilePhotoPath: String,
        backgroundPhotoUrl: String,
        backgroundPhotoPath: String
    ): Response<Boolean>


}
