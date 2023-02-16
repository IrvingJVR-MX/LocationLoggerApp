package com.bootcamp.locationloggerapp.m.ui.ui.home.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bootcamp.locationloggerapp.m.ui.repository.interfaces.FirebaseDataSource
import com.bootcamp.locationloggerapp.m.ui.repository.model.PostDetail
import com.bootcamp.locationloggerapp.m.ui.utils.FirebaseCollections
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
@Inject
constructor(private val repository: FirebaseDataSource) : ViewModel() {
    val errorMessage: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val logPostList: MutableLiveData<MutableList<PostDetail>> by lazy {
        MutableLiveData<MutableList<PostDetail>>()
    }

    fun getUser() = viewModelScope.launch {
        val result = repository.getNestedCollection(FirebaseCollections.Users)
        if (result.message?.isNotEmpty() == true) {
            errorMessage.value = result.message
        } else {
            logPostList.value = result.data
        }
    }

}

