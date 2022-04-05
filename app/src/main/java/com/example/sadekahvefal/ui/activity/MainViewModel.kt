package com.example.sadekahvefal.ui.activity

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.example.sadekahvefal.base.BaseViewModel
import com.example.sadekahvefal.model.Comment
import com.example.sadekahvefal.model.response.HomeResponse
import com.example.sadekahvefal.repository.MainRepository
import com.example.sadekahvefal.utils.ApiState
import com.example.sadekahvefal.utils.PrefUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    application: Application,
    private val mainRepository: MainRepository,
    private val prefUtils: PrefUtils
) : BaseViewModel(application){
    private val _onJobList = MutableStateFlow<ApiState<HomeResponse?>>(ApiState.Empty)
    val onJobList : StateFlow<ApiState<HomeResponse?>> = _onJobList

    init {
        getHomeItems()
    }

    private fun getHomeItems() =
        viewModelScope.launch {
            _onJobList.value = ApiState.Loading
            mainRepository.getHomeItem(
                prefUtils.getUserId(),
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
}