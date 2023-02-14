package com.bootcamp.locationloggerapp.m.ui.ui.signin

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bootcamp.locationloggerapp.m.ui.repository.interfaces.FirebaseStorageSource
import com.bootcamp.locationloggerapp.m.ui.repository.interfaces.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel
@Inject
constructor( private  val repository: UserRepository ,private val firebaseStorageSource: FirebaseStorageSource) : ViewModel(){
    val errorMessage: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val onUserCreated: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

   private lateinit var uriImage : Uri
   private lateinit var usernameUser:String
   private lateinit var emailUser: String
   private lateinit var idUser : String

    private fun savePhoto() = viewModelScope.launch{
        val filename = UUID.randomUUID().toString()+ ".jpg"
        val photoPath = "$idUser/profileImage/$filename"
        val result = firebaseStorageSource.saveImage(uriImage, photoPath)
        if (result.message?.isNotEmpty() == true) {
            errorMessage.value = result.message
        }else{
            val photoUrl = result.data!!
            registerUser(photoUrl, photoPath)
        }
    }


    fun registerUserAuth(username:String,email: String, password: String, uri: Uri) = viewModelScope.launch {
        uriImage = uri
        usernameUser = username
        emailUser = email
        val result = repository.createUserAuth(email, password)
        if (result.message?.isNotEmpty() == true) {
            errorMessage.value = result.message
        }
        else {
            idUser = result.data.toString()
            savePhoto()
        }
    }

    private fun registerUser(photoUrl : String, photoPath: String)= viewModelScope.launch{
        val result = repository.createUser(usernameUser, emailUser, photoUrl, photoPath)
        if (result.message?.isNotEmpty() == true && result.message !="") {
            errorMessage.value = result.message
        }
        else {
            onUserCreated.value = true
        }
    }
}

