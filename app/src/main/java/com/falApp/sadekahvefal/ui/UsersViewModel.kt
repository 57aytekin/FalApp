package com.falApp.sadekahvefal.ui

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.falApp.sadekahvefal.base.BaseViewModel
import com.falApp.sadekahvefal.model.response.AuthResponse
import com.falApp.sadekahvefal.model.HomeRecyclerViewItem
import com.falApp.sadekahvefal.repository.UserRepository
import com.falApp.sadekahvefal.utils.ApiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor(
    application: Application,
    private val userRepository: UserRepository
) : BaseViewModel(application) {

    private val _user = MutableStateFlow<ApiState<AuthResponse>>(ApiState.Empty)
    val user: StateFlow<ApiState<AuthResponse>> = _user

    fun registerAndGetUser(user: HomeRecyclerViewItem.User) = viewModelScope.launch {
        _user.value = ApiState.Loading
        userRepository.registerUser(
            user,
            scope = viewModelScope,
            onSuccess = {
                loadingDetection.postValue(false)
                if (it!!.isSuccessful!!) {
                    _user.value = ApiState.Success(it)
                } else {
                    _user.value = ApiState.Failure(it.message)
                }
            },
            onErrorAction = {
                loadingDetection.postValue(false)
                _user.value = ApiState.Failure(it)
            }
        )
    }

    fun loginUser(email : String, password : String) = viewModelScope.launch {
        _user.value = ApiState.Loading
        userRepository.loginUser(
            email,
            password,
            scope = viewModelScope,
            onSuccess = {
                loadingDetection.postValue(false)
                if (it!!.isSuccessful!!) {
                    _user.value = ApiState.Success(it)
                } else {
                    _user.value = ApiState.Failure(it.message)
                }
            },
            onErrorAction = {
                loadingDetection.postValue(false)
                _user.value = ApiState.Failure(it)
            }
        )
    }

}