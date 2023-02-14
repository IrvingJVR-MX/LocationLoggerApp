package com.bootcamp.locationloggerapp.m.ui.ui.login.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bootcamp.locationloggerapp.m.ui.repository.interfaces.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel
@Inject
constructor( private  val repository: UserRepository) : ViewModel(){
    val errorMessage: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val onLogin: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    fun signIn(email: String, password: String) = viewModelScope.launch {
            val result = repository.signInWithEmailAndPassword(email, password)
            if (result.message?.isNotEmpty() == true) {
                errorMessage.value = result.message
            }
            else {
                onLogin.value = true
            }
    }
}

