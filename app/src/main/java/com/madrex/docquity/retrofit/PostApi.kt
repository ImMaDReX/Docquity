package com.madrex.docquity.retrofit

import com.madrex.docquity.models.Post
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path


interface PostApi {
    @GET("/posts")
    suspend fun getPosts() : Response<List<Post>>

    @GET("/posts/{postId}")
    suspend fun getPost(@Path("postId") postId:String) : Response<Post>
}