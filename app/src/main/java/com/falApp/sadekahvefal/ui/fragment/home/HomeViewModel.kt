package com.falApp.sadekahvefal.ui.fragment.home

import android.app.Application
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.falApp.sadekahvefal.base.BaseViewModel
import com.falApp.sadekahvefal.model.response.TopUserResponse
import com.falApp.sadekahvefal.repository.HomeRepository
import com.falApp.sadekahvefal.utils.ApiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    application: Application,
    private val homeRepository: HomeRepository
) : BaseViewModel(application){

    private val _onTopUserList = MutableStateFlow<ApiState<TopUserResponse?>>(
        ApiState.Empty)
    val onTopUserList : StateFlow<ApiState<TopUserResponse?>> = _onTopUserList

    init {
        getTopUser()
    }

    private fun getTopUser() = viewModelScope.launch {
        _onTopUserList.value = ApiState.Loading
        homeRepository.getTopUsers(
            scope = viewModelScope,
            onSuccess = {
                loadingDetection.postValue(false)
                _onTopUserList.value = ApiState.Success(it)
            },
            onErrorAction = {
                loadingDetection.postValue(false)
                _onTopUserList.value = ApiState.Failure(it)
            }
        )
    }
    val postList = homeRepository.getPost().cachedIn(viewModelScope)


}