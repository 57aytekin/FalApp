package com.example.sadekahvefal.model

data class Comment(
    val comment_id : Int,
    val post_id : Int,
    val commentor_id : Int,
    val comment : String,
    val comment_date : String,
    var rate : Int,
    var is_showing : Int,
    val first_name : String,
    val last_name : String,
    val user_name : String,
    val paths : String,
    val post_date : String,
    val diff_date : DiffDate
)
