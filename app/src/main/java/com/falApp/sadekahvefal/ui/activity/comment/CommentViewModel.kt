package com.falApp.sadekahvefal.ui.activity.comment

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.falApp.sadekahvefal.base.BaseViewModel
import com.falApp.sadekahvefal.model.Comment
import com.falApp.sadekahvefal.repository.CommentRepository
import com.falApp.sadekahvefal.utils.ApiState
import com.falApp.sadekahvefal.utils.PrefUtils
import com.falApp.sadekahvefal.utils.lazyDeffered
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CommentViewModel @Inject constructor(
    application: Application,
    private val commentRepository: CommentRepository,
    private val prefUtils: PrefUtils
    ) : BaseViewModel(application) {

    private val _onJobList = MutableStateFlow<ApiState<List<Comment>?>>(ApiState.Empty)
    val onJobList : StateFlow<ApiState<List<Comment>?>> = _onJobList

    var mutableComment = MutableLiveData<List<Comment>>()

    fun saveComment( postId : Int, commentorId : Int, postOwnerId : Int, comment : String) =
        viewModelScope.launch {
            _onJobList.value = ApiState.Loading
            commentRepository.saveComment(
                postId, commentorId, postOwnerId, comment,
                scope = viewModelScope,
                onSuccess = {
                    loadingDetection.postValue(false)
                    if (it.isSuccessful!!) {
                        _onJobList.value = ApiState.SuccessMessage(it.message)
                    } else {
                        _onJobList.value = ApiState.Failure(it.message)
                    }
                },
                onErrorAction = {
                    loadingDetection.postValue(false)
                    _onJobList.value = ApiState.Failure(it)
                }
            )
        }

    val getComment by lazyDeffered {
        commentRepository.getCommen(prefUtils.getUserId())
    }

    fun getComment(postOwnerId : Int) =
        viewModelScope.launch {
            _onJobList.value = ApiState.Loading
            commentRepository.getComment(
                postOwnerId,
                scope = viewModelScope,
                onSuccess = {
                    if (it.isSuccessful!!)
                        _onJobList.value = ApiState.Success(it.comment_list)
                    //mutableComment.postValue(it.comment_list)
                    else
                        _onJobList.value = ApiState.Empty
                    loadingDetection.postValue(false)

                },
                onErrorAction = {
                    loadingDetection.postValue(false)
                    _onJobList.value = ApiState.Failure(it)
                }
            )
        }

    fun updateIsShowing(commentId : Int) = viewModelScope.launch {
        _onJobList.value = ApiState.Loading
        commentRepository.updateIsShowing(commentId, viewModelScope,
            onSuccess = {
                loadingDetection.postValue(false)
                if (!it.isSuccessful!!) _onJobList.value = ApiState.Failure(it.message)
            },
            onErrorAction = {
                loadingDetection.postValue(false)
                _onJobList.value = ApiState.Failure(it)
            }
        )
    }
    fun updateRatingAndScore(commentId : Int, commentatorId : Int, rating : Int) = viewModelScope.launch {
        _onJobList.value = ApiState.Loading
        commentRepository.updateRatingAndScore(commentId, commentatorId, rating, viewModelScope,
            onSuccess = {
                loadingDetection.postValue(false)
                if (!it.isSuccessful!!) _onJobList.value = ApiState.Failure(it.message)
            },
            onErrorAction = {
                loadingDetection.postValue(false)
                _onJobList.value = ApiState.Failure(it)
            }
        )
    }
}