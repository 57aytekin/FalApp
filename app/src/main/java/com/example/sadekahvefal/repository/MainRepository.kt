package com.example.sadekahvefal.repository

import com.example.sadekahvefal.base.BaseRepository
import com.example.sadekahvefal.model.response.HomeResponse
import com.example.sadekahvefal.network.APIClientImpl
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
}