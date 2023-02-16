package com.bootcamp.locationloggerapp.m.ui.utils

import com.bootcamp.locationloggerapp.m.ui.repository.model.Post
import com.bootcamp.locationloggerapp.m.ui.repository.model.PostDetail

interface IHomeList{
    fun locationDetail(post: PostDetail)
    fun postDetail(post: PostDetail)
}

interface IProfileList {
    fun locationDetail(post: Post)
    fun deletePost(position: Int)
    fun postDetail(post: Post)
}