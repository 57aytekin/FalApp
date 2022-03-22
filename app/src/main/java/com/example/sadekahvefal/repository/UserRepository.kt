package com.example.sadekahvefal.repository

import com.example.sadekahvefal.base.BaseRepository
import com.example.sadekahvefal.model.response.AuthResponse
import com.example.sadekahvefal.model.HomeRecyclerViewItem
import com.example.sadekahvefal.network.APIClientImpl
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class UserRepository @Inject constructor(private val apiServiceImp: APIClientImpl) :
    BaseRepository() {

    suspend fun registerUser(
        user: HomeRecyclerViewItem.User,
        scope: CoroutineScope,
        onSuccess : ((AuthResponse?) -> Unit),
        onErrorAction: ((String?) -> Unit)
    ) = sendRequest(
        scope = scope,
        client = {
            apiServiceImp.apiCollect.userRegister(
                user.user_name, user.first_name, user.last_name!!, user.email, user.phone!!, user.password)
        },
        onSuccess = {onSuccess(it.body())},
        onErrorAction = {onErrorAction(it)}
    )

    suspend fun loginUser(
        email: String,
        password : String,
        scope: CoroutineScope,
        onSuccess : ((AuthResponse?) -> Unit),
        onErrorAction: ((String?) -> Unit)
    ) = sendRequest(
        scope = scope,
        client = {
            apiServiceImp.apiCollect.userLogin(email, password)
        },
        onSuccess = {onSuccess(it.body())},
        onErrorAction = {onErrorAction(it)}
    )
}