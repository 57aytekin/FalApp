package com.falApp.sadekahvefal.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.bumptech.glide.load.HttpException
import com.falApp.sadekahvefal.model.HomeRecyclerViewItem
import com.falApp.sadekahvefal.network.APIClientImpl
import java.io.IOException

private const val STARTING_PAGE_INDEX = 1
class PostPagingSource(
    private val apiClientImpl: APIClientImpl
) : PagingSource<Int, HomeRecyclerViewItem.Post>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, HomeRecyclerViewItem.Post> {
        val position = params.key ?: STARTING_PAGE_INDEX
        return try {
            val response = apiClientImpl.apiCollect.getPost(position, 10)
            var postList = response.post_list
            if (postList.isNullOrEmpty()) postList = mutableListOf()
            else {
                if (position == 1) postList.add(0, postList[0])
            }

            LoadResult.Page(
                data = postList,
                prevKey = if (position == STARTING_PAGE_INDEX) null else position-1,
                nextKey = if (postList.isNullOrEmpty()) null else position+1
            )

        }catch(exception : IOException){
            LoadResult.Error(exception)
        }catch (exception : HttpException){
            LoadResult.Error(exception)
        }
    }


    override fun getRefreshKey(state: PagingState<Int, HomeRecyclerViewItem.Post>): Int? {
        TODO("Not yet implemented")
    }
}