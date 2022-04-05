package com.example.sadekahvefal.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.example.sadekahvefal.base.BaseRepository
import com.example.sadekahvefal.model.HomeRecyclerViewItem
import com.example.sadekahvefal.model.response.TopUserResponse
import com.example.sadekahvefal.network.APIClientImpl
import com.example.sadekahvefal.utils.PrefUtils
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