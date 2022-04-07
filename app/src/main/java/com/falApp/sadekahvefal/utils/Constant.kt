package com.falApp.sadekahvefal.utils

import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.*

object Constant {
    const val USERNAME = "UserName"
    const val USERID = "UserId"
    const val USERGOLD = "UserGold"
    const val IS_ADMIN = "is_admin"

    object CommentItem {
        const val image1 = "image1"
        const val image2 = "image2"
        const val image3 = "image3"
        const val userName = "userName"
        const val gender = "gender"
        const val age = "age"
        const val relation = "relation"
        const val work = "work"
        const val post_id = "post_id"
        const val user_id = "user_id"
        const val user_token = "user_token"
    }
    object CommentedItem{
        const val commentId = "commentId"
        const val commentatorId = "commentatorId"
        const val commentatorImage = "commentatorImage"
        const val commentatorName = "commentatorName"
        const val commentatorLastName = "commentatorLastName"
        const val commentatorUserName = "commentatorUserName"
        const val commentText = "commentText"
        const val rating = "rating"
        const val falDate = "falDate"
        const val commentDate = "commentDate"
        const val isShowing = "isShowing"
    }
}
fun BottomNavigationView.checkItem(actionId: Int) {
    menu.findItem(actionId)?.isChecked = true
}

fun <T> lazyDeffered(block: suspend CoroutineScope.() -> T) : Lazy<Deferred<T>>{
    return lazy {
        GlobalScope.async(start = CoroutineStart.LAZY) {
            block.invoke(this)
        }
    }
}