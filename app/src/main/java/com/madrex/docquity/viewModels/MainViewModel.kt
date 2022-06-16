package com.madrex.docquity.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.madrex.docquity.repository.PostRepository
import com.madrex.docquity.models.Post
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val postRepository: PostRepository): ViewModel() {
    val postsLD : LiveData<List<Post>>
        get() = postRepository.postsLD
    init {
        getPosts()
    }

    fun getPost(postId: String){
        viewModelScope.launch {
            postRepository.getPost(postId)
        }
    }

    fun getPosts(){
        viewModelScope.launch {
            postRepository.getPosts()
        }
    }
}