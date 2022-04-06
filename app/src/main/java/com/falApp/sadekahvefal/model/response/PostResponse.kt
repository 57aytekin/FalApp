package com.falApp.sadekahvefal.model.response

import com.falApp.sadekahvefal.model.HomeRecyclerViewItem

data class PostResponse(
    val post_list: MutableList<HomeRecyclerViewItem.Post>
) : StaticResponse()