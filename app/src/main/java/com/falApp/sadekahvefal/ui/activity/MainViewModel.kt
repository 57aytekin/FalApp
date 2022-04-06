package com.falApp.sadekahvefal.ui.activity

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.falApp.sadekahvefal.base.BaseViewModel
import com.falApp.sadekahvefal.model.response.HomeResponse
import com.falApp.sadekahvefal.repository.MainRepository
import com.falApp.sadekahvefal.utils.ApiState
import com.falApp.sadekahvefal.utils.PrefUtils
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

    fun updateTokenAndLastLogin(token : String) = viewModelScope.launch {
            _onJobList.value = ApiState.Loading
            mainRepository.updateTokenAndLastLogin(prefUtils.getUserId(), token, scope = viewModelScope,
                onSuccess = {
                    if (it.isSuccessful!!)
                        _onJobList.value = ApiState.SuccessMessage(it.message)
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