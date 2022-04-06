package com.falApp.sadekahvefal.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.falApp.sadekahvefal.base.BaseRepository
import com.falApp.sadekahvefal.model.response.TopUserResponse
import com.falApp.sadekahvefal.network.APIClientImpl
import com.falApp.sadekahvefal.utils.PrefUtils
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class HomeRepository @Inject constructor(
    private val apiClientImpl: APIClientImpl,
    private val prefUtils: PrefUtils
    ) : BaseRepository() {


    suspend fun getTopUsers(
        scope: CoroutineScope,
        onSuccess: ((TopUserResponse) -> Unit),
        onErrorAction: ((String?) -> Unit)
    ) = sendRequest(
        scope = scope,
        client = {apiClientImpl.apiCollect.getTopUsers(prefUtils.getUserId())},
        onSuccess = {
            onSuccess(it)
        },
        onErrorAction = {
            onErrorAction(it)
        }
    )

    fun getPost() = Pager(
        config = PagingConfig(
            pageSize = 10,
            enablePlaceholders = false
        ),
        pagingSourceFactory = {PostPagingSource(apiClientImpl)}
    ).liveData
}