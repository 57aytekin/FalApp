package com.example.sadekahvefal.model.response

import com.example.sadekahvefal.model.Comment

data class CommentResponse(
    val comment_list : List<Comment>
) : StaticResponse()