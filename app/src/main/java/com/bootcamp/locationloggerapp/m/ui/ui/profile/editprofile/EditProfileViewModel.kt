package com.bootcamp.locationloggerapp.m.ui.ui.profile.editprofile

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bootcamp.locationloggerapp.m.ui.repository.interfaces.FirebaseDataSource
import com.bootcamp.locationloggerapp.m.ui.repository.interfaces.FirebaseStorageSource
import com.bootcamp.locationloggerapp.m.ui.repository.interfaces.UserRepository
import com.bootcamp.locationloggerapp.m.ui.utils.FirebaseCollections

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel
@Inject
constructor(private  val userRepository: UserRepository,
            private  val firebaseDataSource: FirebaseDataSource,
            private val firebaseStorageSource: FirebaseStorageSource ) : ViewModel(){
    val errorMessage: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val onUpdated: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    private var profilePhotoPath = ""
    private var coverPhotoPath = ""
    private var profilePhotoUrl = ""
    private var coverPhotoUrl = ""
    private var userName = ""
    private var idUser = ""

    fun deletePhotos(idUserS: String, userNameS : String,profilePhotoUrlS: String ,profilePath: String, coverPath: String, backgroundPhotoUrlS: String, profileUri: Uri? = null, coverUri: Uri? = null) = viewModelScope.launch{
        profilePhotoPath = profilePath
        coverPhotoPath = coverPath
        userName = userNameS
        idUser = idUserS
        profilePhotoUrl = profilePhotoUrlS
        coverPhotoUrl = backgroundPhotoUrlS
        when {
            profileUri == null && coverUri !=null -> {
                if (coverPhotoPath == ""){
                    savePhotos (idUserS, null , coverUri)
                }else{
                    val result = firebaseStorageSource.deleteImage(coverPath)
                    if (result.message?.isNotEmpty() == true) {
                        errorMessage.value = result.message
                    }else {
                        savePhotos (idUserS, null , coverUri)
                    }
                }
            }
            profileUri !=null && coverUri == null -> {
                val result = firebaseStorageSource.deleteImage(profilePath)
                if (result.message?.isNotEmpty() == true) {
                    errorMessage.value = result.message
                }else{
                    savePhotos (idUserS, profileUri , null)
                }
            }
            profileUri !=null && coverUri !=null -> {
                val result = firebaseStorageSource.deleteImage(profilePath)
                if (result.message?.isNotEmpty() == true) {
                    errorMessage.value = result.message
                }else{
                    if (coverPath != ""){
                        val result2 = firebaseStorageSource.deleteImage(coverPath)
                        if (result2.message?.isNotEmpty() == true) {
                            errorMessage.value = result2.message
                        }else{
                            savePhotos (idUserS, profileUri, coverUri)
                        }
                    }else{
                        savePhotos (idUserS, profileUri, coverUri)
                    }
                }
            }
            profileUri == null && coverUri == null -> {
                updateUserInfo()
            }
        }
    }
    private fun savePhotos(idUser: String, profileUri: Uri?= null, coverUri: Uri? = null) = viewModelScope.launch{
        val photoName = UUID.randomUUID().toString()+ ".jpg"
        val coverName =  UUID.randomUUID().toString()+ ".jpg"
        val profilePath = "$idUser/profileImage/$photoName"
        val coverPath = "$idUser/coverImage/$coverName"
        when {
            profileUri == null && coverUri !=null -> {
                val result = firebaseStorageSource.saveImage(coverUri, coverPath)
                if (result.message?.isNotEmpty() == true) {
                    errorMessage.value = result.message
                }else{
                    coverPhotoUrl = result.data.toString()
                    coverPhotoPath = coverPath
                    updateUserInfo()
                }
            }
            profileUri !=null && coverUri == null -> {
                val result = firebaseStorageSource.saveImage(profileUri, profilePath)
                if (result.message?.isNotEmpty() == true) {
                    errorMessage.value = result.message
                }else{
                    profilePhotoUrl = result.data.toString()
                    profilePhotoPath = profilePath
                    updateUserInfo()
                }
            }
            profileUri !=null && coverUri !=null -> {
                val result = firebaseStorageSource.saveImage(profileUri, profilePath)
                if (result.message?.isNotEmpty() == true) {
                    errorMessage.value = result.message
                }else{
                    profilePhotoUrl = result.data.toString()
                    profilePhotoPath = profilePath
                    val result2 = firebaseStorageSource.saveImage(coverUri, coverPath)
                    if (result2.message?.isNotEmpty() == true) {
                            errorMessage.value = result.message
                    }else {
                        coverPhotoUrl = result2.data.toString()
                        coverPhotoPath = coverPath
                        updateUserInfo()
                    }
                }
            }

        }
    }


    private fun updateUserInfo() = viewModelScope.launch {
        val result = firebaseDataSource.
        updateCollectionWithId(FirebaseCollections.Users, idUser, userName, profilePhotoUrl, profilePhotoPath, coverPhotoUrl, coverPhotoPath
        )
        if (result.message?.isNotEmpty() == true) {
            errorMessage.value = result.message
        }
        else {
            onUpdated.value = result.data
        }
    }

}

