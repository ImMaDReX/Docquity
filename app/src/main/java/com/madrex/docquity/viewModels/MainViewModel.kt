package com.madrex.assignment.viewModels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.madrex.assignment.repository.PostRepository
import com.madrex.docquity.models.Post
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val postRepository: PostRepository): ViewModel() {
    val postsLD : LiveData<List<Post>>
        get() = postRepository.postsLD
    init {
        viewModelScope.launch {
            postRepository.getPosts()
        }
    }

    fun getPost(postId: String){
        viewModelScope.launch {
            postRepository.getPost(postId)
        }
    }
}