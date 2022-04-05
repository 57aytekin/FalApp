package com.example.sadekahvefal.repository

import com.example.sadekahvefal.base.BaseRepository
import com.example.sadekahvefal.model.*
import com.example.sadekahvefal.model.response.StaticResponse
import com.example.sadekahvefal.network.APIClientImpl
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class PostRepository @Inject constructor(private val apiServiceImp : APIClientImpl) : BaseRepository() {
    suspend fun getJob(
        scope: CoroutineScope,
        onSuccess: ((List<FalJobs>) -> Unit),
        onErrorAction: ((String?) -> Unit)
    ) = sendRequest(
        scope = scope,
        client = {apiServiceImp.apiCollect.getJob()},
        onSuccess = {
            onSuccess(it.job_list)
        },
        onErrorAction = {
            onErrorAction(it)
        }
    )

    suspend fun savePost(
        name1 : String,
        name2 : String,
        name3 : String,
        gold : Int,
        post: HomeRecyclerViewItem.Post,
        scope: CoroutineScope,
        onSuccess: ((StaticResponse) -> Unit),
        onErrorAction: ((String?) -> Unit)
    ) = sendRequest(
        scope = scope,
        client = {
            apiServiceImp.apiCollect
                .savePost(
                    post.image_1,name1, post.image_2, name2, post.image_3, name3, post.user_id,
                    post.gender_id!!, post.job_id!!, post.relation_id!!, post.age!!, post.ekstra_infromation!!, gold)
                 },
        onSuccess = {
            onSuccess(it.body()!!)
        },
        onErrorAction = {
            onErrorAction(it)
        }
    )
}