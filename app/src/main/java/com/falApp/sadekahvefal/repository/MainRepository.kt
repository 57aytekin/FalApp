package com.falApp.sadekahvefal.repository

import com.falApp.sadekahvefal.base.BaseRepository
import com.falApp.sadekahvefal.model.response.HomeResponse
import com.falApp.sadekahvefal.model.response.StaticResponse
import com.falApp.sadekahvefal.network.APIClientImpl
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val apiClientImpl: APIClientImpl
) : BaseRepository() {

    suspend fun getHomeItem(
        userId : Int, scope: CoroutineScope, onSuccess: (HomeResponse) -> Unit, onErrorAction: (String?) -> Unit
    ) = sendRequest(
        scope,
        client = { apiClientImpl.apiCollect.getHomeItem(userId) },
        onSuccess = { onSuccess(it) } ,
        onErrorAction = { onErrorAction(it) }
    )

    suspend fun updateTokenAndLastLogin(
        userId : Int, token : String, scope: CoroutineScope, onSuccess: (StaticResponse) -> Unit, onErrorAction: (String?) -> Unit
    ) = sendRequest(
        scope,
        client = { apiClientImpl.apiCollect.updateTokenAndLastLogin(userId, token) },
        onSuccess = { onSuccess(it) } ,
        onErrorAction = { onErrorAction(it) }
    )
}