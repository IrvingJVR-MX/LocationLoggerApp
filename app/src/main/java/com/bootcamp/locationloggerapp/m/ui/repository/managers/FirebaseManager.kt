package com.bootcamp.locationloggerapp.m.ui.repository.managers

import com.bootcamp.locationloggerapp.m.ui.repository.model.User
import com.bootcamp.locationloggerapp.m.ui.utils.FirebaseCollections
import com.bootcamp.locationloggerapp.m.ui.utils.Response
import com.google.firebase.firestore.FirebaseFirestore
import com.bootcamp.locationloggerapp.m.ui.repository.interfaces.FirebaseDataSource
import com.bootcamp.locationloggerapp.m.ui.repository.model.Post
import com.bootcamp.locationloggerapp.m.ui.repository.model.PostDetail
import com.bootcamp.locationloggerapp.m.ui.utils.Constants
import kotlinx.coroutines.tasks.await

class FirebaseManager(private val db: FirebaseFirestore) : FirebaseDataSource {
    override suspend fun getDocId(collection: FirebaseCollections): Response<String> {
        return try {
            val id = db.collection(collection.toString()).document().id
            return Response.Success(id)
        } catch (e: Exception) {
            Response.Error(e.message.toString(), null)
        }
    }

    override suspend fun <T : Any> addDocumentWithId(
        document: T,
        collection: FirebaseCollections,
        id: String
    ): Response<String> {
        return try {
            db.collection(collection.toString()).document(id).set(document)
                .addOnFailureListener { e ->
                    Response.Error(e.message.toString(), null)
                }
            return Response.Success("")
        } catch (e: Exception) {
            Response.Error(e.message.toString(), null)
        }
    }

    override suspend fun <T : Any> addDocument(
        document: T,
        collection: FirebaseCollections
    ): Response<String> {
        return try {
            val result = db.collection(collection.toString()).add(document).await()
            return Response.Success(result.id)
        } catch (e: Exception) {
            Response.Error(e.message.toString(), null)
        }
    }

    override suspend fun getUserCollection(collection: FirebaseCollections): Response<MutableList<User>> {
        return try {
            val list = db.collection(collection.toString()).get().await()
            return Response.Success(list.toObjects(User::class.java))
        } catch (e: Exception) {
            Response.Error(e.message.toString(), null)
        }
    }

    override suspend fun getPostCollectionWithId(
        collection: FirebaseCollections,
        id: String
    ): Response<MutableList<Post>> {
        return try {
            val list = db.collection(collection.toString())
                .whereEqualTo(Constants.userId, id)
                .get()
                .await()
            return Response.Success(list.toObjects(Post::class.java))
        } catch (e: Exception) {
            Response.Error(e.message.toString(), null)
        }
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
        return try {
            var result = false
            db.collection(collection.toString())
                .document(id)
                .update(
                    "name",
                    name,
                    "backgroundPhotoUrl",
                    backgroundPhotoUrl,
                    "backgroundPhotoPath",
                    backgroundPhotoPath,
                    "profilePhotoPath",
                    profilePhotoPath,
                    "profilePhotoUrl",
                    profilePhotoUrl
                )
                .addOnSuccessListener {
                    result = true
                }
                .await()
            return Response.Success(result)
        } catch (e: Exception) {
            Response.Error(e.message.toString(), null)
        }
    }

    override suspend fun deleteCollectionWithId(
        collection: FirebaseCollections,
        id: String
    ): Response<Boolean> {
        return try {
            var result = false
            db.collection(collection.toString())
                .document(id)
                .delete()
                .addOnSuccessListener {
                    result = true
                }
                .await()
            return Response.Success(result)
        } catch (e: Exception) {
            Response.Error(e.message.toString(), null)
        }
    }

    override suspend fun getUserInfo(
        collection: FirebaseCollections,
        id: String
    ): Response<MutableList<User>> {
        return try {
            val userInfo = db.collection(collection.toString())
                .whereEqualTo("id", id)
                .get()
                .await()
            return Response.Success(userInfo.toObjects(User::class.java))
        } catch (e: Exception) {
            Response.Error(e.message.toString(), null)
        }
    }

    override suspend fun getCollectionSnapshot(collection: FirebaseCollections): Response<MutableList<User>> {
        return try {
            val userList = mutableListOf<User>()
            val list = db.collection(collection.toString())
            list.addSnapshotListener { snapshot, e ->
                if (e != null) {
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    for (document in snapshot.documents) {
                        document.toObject(User::class.java)?.let { userList.add(it) }
                    }
                }
            }
            return Response.Success(userList)
        } catch (e: Exception) {
            Response.Error(e.message.toString(), null)
        }
    }

    override suspend fun getNestedCollection(collection: FirebaseCollections): Response<MutableList<PostDetail>> {
        return try {
            val postDetailList = mutableListOf<PostDetail>()
            val posts = db.collection(FirebaseCollections.Posts.toString()).get().await()
            val postList = posts.toObjects(Post::class.java)
            for (post in postList) {
                db.collection(FirebaseCollections.Users.toString())
                    .whereEqualTo("id", post.userId)
                    .get()
                    .addOnSuccessListener { result ->
                        for (document in result) {
                            val userName = document.data["name"]
                            val userPhotoUrl = document.data["profilePhotoUrl"]
                            val postDetail = PostDetail(
                                post.id,
                                post.userId,
                                post.title,
                                post.description,
                                post.photoUrl,
                                post.placeName,
                                post.geoPoint,
                                userName as String?,
                                userPhotoUrl as String?
                            )
                            postDetailList.add(postDetail)
                        }
                    }
                    .await()
            }
            return Response.Success(postDetailList)
        } catch (e: Exception) {
            Response.Error(e.message.toString(), null)
        }
    }


}
