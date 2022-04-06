package com.falApp.sadekahvefal.model.response

import com.falApp.sadekahvefal.model.HomeRecyclerViewItem

data class UserProfileResponse (
    val user_profile : HomeRecyclerViewItem.User,
) : StaticResponse()