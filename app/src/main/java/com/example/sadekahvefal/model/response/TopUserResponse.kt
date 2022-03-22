package com.example.sadekahvefal.model.response

import com.example.sadekahvefal.model.HomeRecyclerViewItem

data class TopUserResponse (
    val top_user: List<HomeRecyclerViewItem.User>
): StaticResponse()