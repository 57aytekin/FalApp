package com.example.sadekahvefal.repository

import com.example.sadekahvefal.base.BaseRepository
import com.example.sadekahvefal.model.response.StaticResponse
import com.example.sadekahvefal.model.response.UserProfileResponse
import com.example.sadekahvefal.network.APIClientImpl
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class ProfileRepository @Inject constructor (
    private val apiClientImpl: APIClientImpl
) : BaseRepository() {

    suspend fun getUserProfile(
        userId : Int, scope: CoroutineScope, onSuccess: (UserProfileResponse) -> Unit, onErrorAction: (String?) -> Unit
    ) = sendRequest(
        scope,
        client = { apiClientImpl.apiCollect.getUserProfile(userId) },
        onSuccess = { onSuccess(it) } ,
        onErrorAction = { onErrorAction(it) }
    )
    suspend fun updateProfile(
        userId: Int, userName : String, userFirstName : String, lastName : String, image : String,
        scope: CoroutineScope, onSuccess: (StaticResponse) -> Unit, onErrorAction: (String?) -> Unit
    ) = sendRequest(scope, client = {apiClientImpl.apiCollect.updateUserProfile(userId, userName, userFirstName, lastName, image)},
        onSuccess = {onSuccess(it)} ,
        onErrorAction = {onErrorAction(it)}
    )
}