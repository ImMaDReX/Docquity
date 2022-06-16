package com.madrex.docquity.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.madrex.docquity.retrofit.PostApi
import com.madrex.docquity.models.Post
import javax.inject.Inject

class PostRepository @Inject constructor(private val postApi: PostApi){
    private val postsMLD = MutableLiveData<List<Post>>()
    val postsLD : LiveData<List<Post>>
        get() = postsMLD

    suspend fun getPosts(){
        val result = postApi.getPosts()
        if(result.isSuccessful && result.body() != null){
            postsMLD.postValue(result.body())
        } else {
            postsMLD.postValue(mutableListOf())
        }
    }

    suspend fun getPost(postId:String){
        val result = postApi.getPost(postId)
        if(result.isSuccessful && result.body() !=null){
            val posts = mutableListOf<Post>()
            posts.add(result.body() as Post)
            postsMLD.postValue(posts)
        } else {
            postsMLD.postValue(mutableListOf())
        }
    }

}