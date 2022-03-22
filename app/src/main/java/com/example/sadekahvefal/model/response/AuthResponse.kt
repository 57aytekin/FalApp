package com.example.sadekahvefal.model.response

import com.example.sadekahvefal.model.HomeRecyclerViewItem


data class AuthResponse(
    val login : HomeRecyclerViewItem.User?
) : StaticResponse()