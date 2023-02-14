package com.bootcamp.locationloggerapp.m.ui.repository.model
import com.google.firebase.firestore.GeoPoint

data class Post(
    var id: String? = null,
    val userId: String? = null,
    val title: String? = null,
    val description: String? = null,
    var photoUrl: String? = null,
    val placeName : String? =  null,
    val photoPath: String? =null,
    val geoPoint: GeoPoint? = null
)
