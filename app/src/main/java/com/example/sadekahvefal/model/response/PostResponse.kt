package com.example.sadekahvefal.model.response

import com.example.sadekahvefal.model.HomeRecyclerViewItem

data class PostResponse(
    val post_list: MutableList<HomeRecyclerViewItem.Post>
) : StaticResponse()