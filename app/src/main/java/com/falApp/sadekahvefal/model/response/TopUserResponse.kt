package com.falApp.sadekahvefal.model.response

import com.falApp.sadekahvefal.model.HomeRecyclerViewItem

data class TopUserResponse (
    val top_user: List<HomeRecyclerViewItem.User>,
    val badgeCount: Int,
    val gold : Int
): StaticResponse()