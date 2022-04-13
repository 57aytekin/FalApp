package com.falApp.sadekahvefal.utils

import com.falApp.sadekahvefal.model.Comment
import com.falApp.sadekahvefal.model.HomeRecyclerViewItem

interface BottomSheetClickListener {
    fun onSelectedItem(text : String, whichData : Int)
}
interface ClickListeners {
    fun onButtonCLick(post: HomeRecyclerViewItem.Post)
    fun onUserClick(userId : Int)
}

interface CommentListListener {
    fun onItemCLick(comment: Comment)
}
interface AdminItemClickListener {
    fun isConfirmItem(postId : Int, isConfirm : Int, token : String)
}
interface TopUserClickListener {
    fun onUserClick(userId : Int)
}