package com.bootcamp.locationloggerapp.m.ui.ui.post.viewmodel

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bootcamp.locationloggerapp.m.ui.repository.interfaces.FirebaseDataSource
import com.bootcamp.locationloggerapp.m.ui.repository.interfaces.FirebaseStorageSource
import com.bootcamp.locationloggerapp.m.ui.repository.model.Post
import com.bootcamp.locationloggerapp.m.ui.utils.FirebaseCollections
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject


@HiltViewModel
class PostViewModel
@Inject
constructor (private val firebaseStorageSource: FirebaseStorageSource, private  val repository: FirebaseDataSource) : ViewModel(){
    val errorMessage: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val onPosted: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
    private lateinit var uriImage : Uri
    private lateinit var postDetail : Post


    private fun postPhoto () = viewModelScope.launch{
        val id = postDetail.id
        val filename = UUID.randomUUID().toString()+ ".jpg"
        val path = "Posts/$id/$filename"
        val result = firebaseStorageSource.saveImage(uriImage, path)
        if (result.message?.isNotEmpty() == true) {
            errorMessage.value = result.message
        }else{
            postDetail.photoUrl = result.data
            addPostDocument()
        }
    }

    private fun addPostDocument() = viewModelScope.launch {
        val id = postDetail.id?.let {  postDetail.id } ?: ""
        val result = repository.addDocumentWithId(postDetail,FirebaseCollections.Posts, id)
        if (result.message?.isNotEmpty() == true) {
            errorMessage.value = result.message
        }else{
            onPosted.value = true
        }
    }


    fun addPost(post: Post,uri: Uri) = viewModelScope.launch {
        uriImage = uri
        val resultId =  repository.getDocId(FirebaseCollections.Posts)
        if (resultId.message?.isNotEmpty() == true) {
            errorMessage.value = resultId.message
        }
        else {
            post.id =  resultId.data?.let {resultId.data } ?: ""
            postDetail = post
            postPhoto()
        }
    }
}
