package com.falApp.sadekahvefal.model.response

import com.falApp.sadekahvefal.model.HomeItems

data class HomeResponse(
    val home : HomeItems
): StaticResponse()
