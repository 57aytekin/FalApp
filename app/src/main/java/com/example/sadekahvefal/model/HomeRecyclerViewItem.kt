package com.example.sadekahvefal.model

import android.os.Parcelable
import androidx.annotation.NonNull
import com.google.gson.annotations.SerializedName

sealed class HomeRecyclerViewItem {
    class User(
        @NonNull
        val user_id : Int? = null,
        val user_name : String,
        val first_name : String,
        val last_name : String? = null,
        val last_login : String? = null,
        val email : String,
        val paths : String? = null,
        val phone : String? = null,
        val password : String,
        val is_social_account : Int? = null,
        val who_is_talking : Int? = null,
        val register_date : String? = null,
        val is_deleted : Int? = null,
        val score : Int? = null,
        val gold : Int? = null,
        val comment_count : Int? = null,
        val badgeCount : Int? = null,
        val user_post : List<Post?> = listOf()
    ): HomeRecyclerViewItem()


    class Post (
        val post_id : Int? = null,
        val first_name : String? = null,
        val last_name : String? = null,
        val user_name : String? = null,
        val share_date : String? = null,
        val image_1 : String,
        val image_2 : String,
        val image_3 : String,
        val user_id : Int,
        val gender : String? = null,
        val gender_id : Int? = null,
        val job_id : Int? = null,
        val job : String? = null,
        val relation : String? = null,
        val relation_id : Int? = null,
        val age : Int? = null,
        val ekstra_infromation : String? = null,
        val commentator : Int = 0,
        val is_deleted : Int = 0,
        val is_confirmed : Int = 0,
        val token : String? = null,
        val paths : String? = null,
        val diff_date: DiffDate? = null
    ): HomeRecyclerViewItem()
}
