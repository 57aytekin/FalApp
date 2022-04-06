package com.falApp.sadekahvefal.model.response

import com.falApp.sadekahvefal.model.HomeRecyclerViewItem


data class AuthResponse(
    val login : HomeRecyclerViewItem.User?
) : StaticResponse()