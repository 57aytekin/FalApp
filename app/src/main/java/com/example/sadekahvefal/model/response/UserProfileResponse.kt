package com.example.sadekahvefal.model.response

import com.example.sadekahvefal.model.HomeRecyclerViewItem

data class UserProfileResponse (
    val user_profile : HomeRecyclerViewItem.User,
) : StaticResponse()