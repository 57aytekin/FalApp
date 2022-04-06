package com.falApp.sadekahvefal.model.response

import com.falApp.sadekahvefal.model.Comment

data class CommentResponse(
    val comment_list : List<Comment>
) : StaticResponse()