package com.example.sadekahvefal.utils

import com.example.sadekahvefal.model.Comment
import com.example.sadekahvefal.model.HomeRecyclerViewItem

interface BottomSheetClickListener {
    fun onSelectedItem(text : String, whichData : Int)
}
interface ClickListeners {
    fun onButtonCLick(post: HomeRecyclerViewItem.Post)
}

interface CommentListListener {
    fun onItemCLick(comment: Comment)
}