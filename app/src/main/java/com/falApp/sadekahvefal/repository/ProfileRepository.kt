package com.falApp.sadekahvefal.repository

import com.falApp.sadekahvefal.base.BaseRepository
import com.falApp.sadekahvefal.model.response.StaticResponse
import com.falApp.sadekahvefal.model.response.UserProfileResponse
import com.falApp.sadekahvefal.network.APIClientImpl
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
        userId: Int, email : String, userFirstName : String, lastName : String, image : String, userName : String,
        scope: CoroutineScope, onSuccess: (StaticResponse) -> Unit, onErrorAction: (String?) -> Unit
    ) = sendRequest(scope, client = {apiClientImpl.apiCollect.updateUserProfile(userId, email, userFirstName, lastName, image, userName)},
        onSuccess = {onSuccess(it)} ,
        onErrorAction = {onErrorAction(it)}
    )
}