package com.example.sadekahvefal.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.example.sadekahvefal.base.BaseRepository
import com.example.sadekahvefal.model.HomeRecyclerViewItem
import com.example.sadekahvefal.network.APIClientImpl
import com.example.sadekahvefal.utils.PrefUtils
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class HomeRepository @Inject constructor(
    private val apiClientImpl: APIClientImpl) : BaseRepository() {


    suspend fun getTopUsers(
        scope: CoroutineScope,
        onSuccess: ((List<HomeRecyclerViewItem.User>) -> Unit),
        onErrorAction: ((String?) -> Unit)
    ) = sendRequest(
        scope = scope,
        client = {apiClientImpl.apiCollect.getTopUsers()},
        onSuccess = {
            onSuccess(it.top_user)
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