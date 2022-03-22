package com.example.sadekahvefal.repository

import androidx.lifecycle.LiveData
import com.example.sadekahvefal.base.BaseRepository
import com.example.sadekahvefal.model.Comment
import com.example.sadekahvefal.model.response.CommentResponse
import com.example.sadekahvefal.model.response.StaticResponse
import com.example.sadekahvefal.network.APIClientImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CommentRepository @Inject constructor(private val apiClientImpl: APIClientImpl): BaseRepository() {
    suspend fun saveComment(
        postId : Int, commentorId : Int, postOwnerId : Int, comment : String,
        scope: CoroutineScope,
        onSuccess: ((StaticResponse) -> Unit),
        onErrorAction: ((String?) -> Unit)
    ) = sendRequest(
        scope = scope,
        client = {
            apiClientImpl.apiCollect
                .saveComment(postId, commentorId, postOwnerId, comment)
        },
        onSuccess = {
            onSuccess(it.body()!!)
        },
        onErrorAction = {
            onErrorAction(it)
        }
    )

    suspend fun getComment(
        postOwnerId : Int,
        scope: CoroutineScope,
        onSuccess: ((CommentResponse) -> Unit),
        onErrorAction: ((String?) -> Unit)
    ) = sendRequest(
        scope = scope,
        client = {
            apiClientImpl.apiCollect
                .getComment(postOwnerId)
        },
        onSuccess = {
            onSuccess(it)
        },
        onErrorAction = {
            onErrorAction(it)
        }
    )

    suspend fun getCommen(id : Int) : LiveData<CommentResponse> {
        return withContext(Dispatchers.Main){
            apiClientImpl.apiCollect.getCommentLive(id)
        }
    }

    suspend fun updateIsShowing(
        commentId : Int, scope: CoroutineScope, onSuccess: (StaticResponse) -> Unit, onErrorAction: (String?) -> Unit
    ) = sendRequest(
        scope,
        client = { apiClientImpl.apiCollect.updateIsShowing(commentId) },
        onSuccess = { onSuccess(it) } ,
        onErrorAction = { onErrorAction(it) }
    )

    suspend fun updateRatingAndScore(
        commentId : Int, commentatorId : Int, rating : Int, scope: CoroutineScope,
        onSuccess: (StaticResponse) -> Unit, onErrorAction: (String?) -> Unit
    ) = sendRequest(
        scope,
        client = { apiClientImpl.apiCollect.updateRatingAndScore(commentId, commentatorId, rating) },
        onSuccess = { onSuccess(it) } ,
        onErrorAction = { onErrorAction(it) }
    )

}