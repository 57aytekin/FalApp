package com.example.sadekahvefal.repository

import com.example.sadekahvefal.base.BaseRepository
import com.example.sadekahvefal.model.response.PostResponse
import com.example.sadekahvefal.model.response.StaticResponse
import com.example.sadekahvefal.network.APIClientImpl
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class AdminRepository @Inject constructor(
    private val apiClientImpl: APIClientImpl
) : BaseRepository() {

    suspend fun getConfirmPost(
        scope: CoroutineScope, onSuccess: (PostResponse) -> Unit, onErrorAction: (String?) -> Unit
    ) = sendRequest(
        scope,
        client = { apiClientImpl.apiCollect.getConfirmPost(1, 20) },
        onSuccess = { onSuccess(it) } ,
        onErrorAction = { onErrorAction(it) }
    )

    suspend fun updateIsConfirm(
        postId : Int, isConfirm : Int, scope: CoroutineScope, onSuccess: (StaticResponse) -> Unit, onErrorAction: (String?) -> Unit
    ) = sendRequest(
        scope,
        client = { apiClientImpl.apiCollect.updateIsConfirmed(postId, isConfirm) },
        onSuccess = { onSuccess(it) } ,
        onErrorAction = { onErrorAction(it) }
    )
}