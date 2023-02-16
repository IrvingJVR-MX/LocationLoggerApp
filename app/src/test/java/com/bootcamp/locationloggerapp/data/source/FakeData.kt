package com.bootcamp.locationloggerapp.data.source

import com.bootcamp.locationloggerapp.m.ui.repository.model.Post
import com.bootcamp.locationloggerapp.m.ui.repository.model.PostDetail
import com.bootcamp.locationloggerapp.m.ui.repository.model.User
import com.google.firebase.firestore.GeoPoint

object FakeData {
    val user = User("1","Irving","irvingJVR@hotmail.com","https://i.redd.it/zu8rpygrxbf31.jpg","","","")
    private val geoPoint = GeoPoint(40.71427,-74.00597)
    val postDetail = PostDetail("1","1","New York","The city that never sleeps"
        ,"https://es.wikipedia.org/wiki/%C3%81rea_metropolitana_de_Nueva_York#/media/Archivo:New_york_times_square-terabass.jpg"
        ,"New york", geoPoint,"","")
    val post = Post("1","1","New York","The city that never sleeps","https://es.wikipedia.org/wiki/%C3%81rea_metropolitana_de_Nueva_York#/media/Archivo:New_york_times_square-terabass.jpg"
        ,"New york","", geoPoint)
}