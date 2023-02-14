package com.bootcamp.locationloggerapp.m.ui.ui.profile.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bootcamp.locationloggerapp.m.ui.repository.interfaces.FirebaseDataSource
import com.bootcamp.locationloggerapp.m.ui.repository.interfaces.FirebaseStorageSource
import com.bootcamp.locationloggerapp.m.ui.repository.interfaces.UserRepository
import com.bootcamp.locationloggerapp.m.ui.repository.model.Post
import com.bootcamp.locationloggerapp.m.ui.repository.model.User
import com.bootcamp.locationloggerapp.m.ui.utils.FirebaseCollections
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel
@Inject
constructor(private  val userRepository: UserRepository,
            private  val firebaseDataSource: FirebaseDataSource,
            private val firebaseStorageSource: FirebaseStorageSource
) : ViewModel(){
    val errorMessage: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val logPostList : MutableLiveData<MutableList<Post>> by lazy {
        MutableLiveData<MutableList<Post>>()
    }

    val userInfo : MutableLiveData<User> by lazy {
        MutableLiveData<User>()
    }

    val onUserSignOut: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    val onDeletedPost: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }


    fun deletePhoto(id: String ,path: String) = viewModelScope.launch{
        val result = firebaseStorageSource.deleteImage(path)
        if (result.message?.isNotEmpty() == true) {
            errorMessage.value = result.message
        }else{
            if (result.data == true){
                deleteCollection(id)
            }else{
                errorMessage.value = "Error"
            }
        }
    }

   private fun deleteCollection(id: String) = viewModelScope.launch {
        val result = firebaseDataSource.deleteCollectionWithId(FirebaseCollections.Posts,id)
        if (result.message?.isNotEmpty() == true)  {
            errorMessage.value = result.message
        }
        else {
            onDeletedPost.value = true
        }
    }


    fun getUserInfo(id: String) = viewModelScope.launch {
        val result = firebaseDataSource.getUserInfo(FirebaseCollections.Users,id)
        if (result.message?.isNotEmpty() == true) {
            errorMessage.value = result.message
        }
        else {
            userInfo.value = result.data?.get(0)
        }
    }


    fun getUserCollections(id: String) = viewModelScope.launch {
        val result = firebaseDataSource.getPostCollectionWithId(FirebaseCollections.Posts,id)
        if (result.message?.isNotEmpty() == true) {
            errorMessage.value = result.message
        }
        else {
            logPostList.value = result.data
        }
    }

    fun signOut(){
        val result = userRepository.signOut()
        if (result.message?.isNotEmpty() == true) {
            errorMessage.value = result.message
        }
        else {
            onUserSignOut.value = true
        }
    }

}

