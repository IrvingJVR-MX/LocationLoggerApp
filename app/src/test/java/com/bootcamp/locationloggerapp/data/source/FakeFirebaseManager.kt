package com.bootcamp.locationloggerapp.data.source

import com.bootcamp.locationloggerapp.m.ui.repository.interfaces.FirebaseDataSource
import com.bootcamp.locationloggerapp.m.ui.repository.model.Post
import com.bootcamp.locationloggerapp.m.ui.repository.model.PostDetail
import com.bootcamp.locationloggerapp.m.ui.repository.model.User
import com.bootcamp.locationloggerapp.m.ui.utils.FirebaseCollections
import com.bootcamp.locationloggerapp.m.ui.utils.Response

class FakeFirebaseManager: FirebaseDataSource {
    override suspend fun getDocId(collection: FirebaseCollections): Response<String> {
        return Response.Success("1")
    }

    override suspend fun getUserCollection(collection: FirebaseCollections): Response<MutableList<User>> {
        val users =  mutableListOf(FakeData.user)
        return Response.Success(users)
    }

    override suspend fun <T : Any> addDocument(
        document: T,
        collection: FirebaseCollections
    ): Response<String> {
        return Response.Success("success")
    }

    override suspend fun <T : Any> addDocumentWithId(
        document: T,
        collection: FirebaseCollections,
        id: String
    ): Response<String> {
        return Response.Success("success")
    }

    override suspend fun getCollectionSnapshot(collection: FirebaseCollections): Response<MutableList<User>> {
        val users =  mutableListOf(FakeData.user)
        return Response.Success(users)
    }

    override suspend fun getNestedCollection(collection: FirebaseCollections): Response<MutableList<PostDetail>> {
        val postDetail =  mutableListOf(FakeData.postDetail)
        return Response.Success(postDetail)
    }

    override suspend fun getPostCollectionWithId(
        collection: FirebaseCollections,
        id: String
    ): Response<MutableList<Post>> {
        val post =  mutableListOf(FakeData.post)
        return Response.Success(post)
    }

    override suspend fun getUserInfo(
        collection: FirebaseCollections,
        id: String
    ): Response<MutableList<User>> {
        val users =  mutableListOf(FakeData.user)
        return Response.Success(users)
    }

    override suspend fun deleteCollectionWithId(
        collection: FirebaseCollections,
        id: String
    ): Response<Boolean> {
        return Response.Success(true)
    }

    override suspend fun updateCollectionWithId(
        collection: FirebaseCollections,
        id: String,
        name: String,
        profilePhotoUrl: String,
        profilePhotoPath: String,
        backgroundPhotoUrl: String,
        backgroundPhotoPath: String
    ): Response<Boolean> {
        return Response.Success(true)
    }
}