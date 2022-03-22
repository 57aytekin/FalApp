package com.example.sadekahvefal.ui

import android.app.Application
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.sadekahvefal.base.BaseViewModel
import com.example.sadekahvefal.model.FalJobs
import com.example.sadekahvefal.model.HomeRecyclerViewItem
import com.example.sadekahvefal.repository.PostRepository
import com.example.sadekahvefal.utils.ApiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    application: Application,
    private val postRepository: PostRepository
) : BaseViewModel(application) {

    private val _onJobList = MutableStateFlow<ApiState<List<FalJobs>?>>(ApiState.Empty)
    val onJobList : StateFlow<ApiState<List<FalJobs>?>> = _onJobList

    init {
        getJobList()
    }

    private fun getJobList() = viewModelScope.launch {
        _onJobList.value = ApiState.Loading
        postRepository.getJob(
            scope = viewModelScope,
            onSuccess = {
                loadingDetection.postValue(false)
                _onJobList.value = ApiState.Success(it)
            },
            onErrorAction = {
                loadingDetection.postValue(false)
                _onJobList.value = ApiState.Failure(it)
            }
        )
    }


    fun savePost(post : HomeRecyclerViewItem.Post,name1 : String, name2 : String, name3 : String,) =
        viewModelScope.launch {
        _onJobList.value = ApiState.Loading
        postRepository.savePost(
            name1, name2, name3,
            post,
            scope = viewModelScope,
            onSuccess = {
                loadingDetection.postValue(false)
                if (it.isSuccessful!!) {
                    _onJobList.value = ApiState.SuccessMessage(it.message)
                } else {
                    _onJobList.value = ApiState.Failure(it.message)
                }
            },
            onErrorAction = {
                loadingDetection.postValue(false)
                _onJobList.value = ApiState.Failure(it)
            }
        )
    }
}