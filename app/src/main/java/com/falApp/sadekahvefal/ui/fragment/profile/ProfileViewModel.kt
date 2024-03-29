package com.falApp.sadekahvefal.ui.fragment.profile

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.falApp.sadekahvefal.base.BaseViewModel
import com.falApp.sadekahvefal.model.response.UserProfileResponse
import com.falApp.sadekahvefal.repository.ProfileRepository
import com.falApp.sadekahvefal.utils.ApiState
import com.falApp.sadekahvefal.utils.PrefUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ProfileViewModel @Inject constructor(
    application: Application,
    private val profileRepository: ProfileRepository,
    private val prefUtils: PrefUtils
) : BaseViewModel(application) {
    private val _onJobList = MutableStateFlow<ApiState<UserProfileResponse?>>(ApiState.Empty)
    val onJobList : StateFlow<ApiState<UserProfileResponse?>> = _onJobList


    fun getUserProfile(userId : Int) = viewModelScope.launch {
        _onJobList.value = ApiState.Loading
        profileRepository.getUserProfile(userId, viewModelScope,
            onSuccess = {
                loadingDetection.postValue(false)
                if (it.isSuccessful!!) {
                    _onJobList.value = ApiState.Success(it)
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

    fun updateProfile(userId: Int, email : String, firstName : String, lastName : String, image : String) = viewModelScope.launch {
        _onJobList.value = ApiState.Loading
        profileRepository.updateProfile(userId, email, firstName, lastName, image, prefUtils.getUserName()!!, viewModelScope,
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