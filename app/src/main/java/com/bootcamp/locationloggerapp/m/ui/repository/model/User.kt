package com.bootcamp.locationloggerapp.m.ui.repository.model

data class User(
    val id: String? = null,
    val name: String? = null,
    val email: String? = null,
    val profilePhotoUrl: String? = "",
    val profilePhotoPath: String? = "",
    val backgroundPhotoUrl: String? = "",
    val backgroundPhotoPath: String? = ""
)
