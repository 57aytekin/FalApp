package com.falApp.sadekahvefal.ui.fragment.admin

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.falApp.sadekahvefal.base.BaseViewModel
import com.falApp.sadekahvefal.model.response.PostResponse
import com.falApp.sadekahvefal.repository.AdminRepository
import com.falApp.sadekahvefal.utils.ApiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminViewModel @Inject constructor(
    application: Application,
    private val repository: AdminRepository
) : BaseViewModel(application) {
    private val _onJobList = MutableStateFlow<ApiState<PostResponse?>>(ApiState.Empty)
    val onJobList : StateFlow<ApiState<PostResponse?>> = _onJobList

    init {
        getIsConfirmPost()
    }

    fun getIsConfirmPost() =
        viewModelScope.launch {
            _onJobList.value = ApiState.Loading
            repository.getConfirmPost(
                scope = viewModelScope,
                onSuccess = {
                    if (it.isSuccessful!!)
                        _onJobList.value = ApiState.Success(it)
                    else
                        _onJobList.value = ApiState.Failure(it.message)
                    loadingDetection.postValue(false)
                },
                onErrorAction = {
                    loadingDetection.postValue(false)
                    _onJobList.value = ApiState.Failure(it)
                }
            )
        }

    fun updatePostConfirmed(postId : Int, isConfirmed : Int, token : String, message : String) =
        viewModelScope.launch {
            _onJobList.value = ApiState.Loading
            repository.updateIsConfirm(postId, isConfirmed, token, message,
            scope =  viewModelScope,
            onSuccess = {
                if (it.isSuccessful!!)
                    _onJobList.value = ApiState.SuccessMessage(it.message)
                else
                    _onJobList.value = ApiState.Failure(it.message)
            },
            onErrorAction = {
                loadingDetection.postValue(false)
                _onJobList.value = ApiState.Failure(it)
            })
        }
}