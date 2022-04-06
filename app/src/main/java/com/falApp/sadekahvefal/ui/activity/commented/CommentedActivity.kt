package com.falApp.sadekahvefal.ui.activity.commented

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.falApp.sadekahvefal.base.BaseActivity
import com.falApp.sadekahvefal.databinding.ActivityCommentedBinding
import com.falApp.sadekahvefal.ui.activity.comment.CommentViewModel
import com.falApp.sadekahvefal.utils.ApiState
import com.falApp.sadekahvefal.utils.Constant
import com.falApp.sadekahvefal.utils.NavigateFragmentParams
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class CommentedActivity : BaseActivity<ActivityCommentedBinding, CommentViewModel>() {
    override val viewModel: CommentViewModel by viewModels()
    override fun getViewBinding() = ActivityCommentedBinding.inflate(layoutInflater)
    private var commentId = -1
    private var changingRating = -1
    private var isDoShowing = false

    @SuppressLint("SetTextI18n")
    override fun onActivityCreated() {
        val name = intent.getStringExtra(Constant.CommentedItem.commentatorName)
        commentId = intent.getIntExtra(Constant.CommentedItem.commentId, -1)
        val commentatorId = intent.getIntExtra(Constant.CommentedItem.commentatorId, -1)
        val lastName = intent.getStringExtra(Constant.CommentedItem.commentatorLastName)
        val userName = intent.getStringExtra(Constant.CommentedItem.commentatorUserName)
        val image = intent.getStringExtra(Constant.CommentedItem.commentatorImage)
        val comment = intent.getStringExtra(Constant.CommentedItem.commentText)
        val falDate = intent.getStringExtra(Constant.CommentedItem.falDate)
        val commentDate = intent.getStringExtra(Constant.CommentedItem.commentDate)
        val rating = intent.getIntExtra(Constant.CommentedItem.rating,0).toFloat()
        val isShowing = intent.getIntExtra(Constant.CommentedItem.isShowing, 0)

        binding.apply {
            if (image?.isNotEmpty() == true)
                Glide.with(this@CommentedActivity).load(image).into(ivCommentatorImage)
            tvCommentatorName.text = "$name $lastName"
            tvCommentatorUserName.text = "@$userName"
            tvComment.text = comment
            ratingBar.rating = rating
            tvFalDate.text = falDate
            tvCommentDate.text = commentDate
            ratingBar.setOnRatingBarChangeListener { ratingBar, fl, b ->
                if (fl != rating) {
                    viewModel.updateRatingAndScore(commentId, commentatorId, fl.toInt())
                    changingRating = fl.toInt()
                }
            }
        }
        if (isShowing == 0) {
            viewModel.updateIsShowing(commentId)
            isDoShowing = true
        }
    }

    override fun observe() {
        lifecycleScope.launchWhenResumed {
            viewModel.onJobList.collect {
                when (it) {
                    ApiState.Empty -> {}
                    is ApiState.Failure -> {
                        if (it.errorMessage.isNullOrEmpty()) {
                            Toast.makeText(this@CommentedActivity, "Bir sorun oluştu", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this@CommentedActivity, it.errorMessage, Toast.LENGTH_SHORT).show()
                        }
                    }
                    ApiState.Loading -> {
                    }
                    is ApiState.Success -> {

                    }
                    is ApiState.SuccessMessage -> {}
                }
            }
        }
    }

    override fun navigateFragment(params: NavigateFragmentParams) {
        TODO("Not yet implemented")
    }

    override fun showHideProgress(isShow: Boolean) {
        if (isShow) binding.progressCommented.visibility = View.VISIBLE
        else binding.progressCommented.visibility = View.GONE
    }


    override fun onBackPressed() {
        val intent = Intent()
        intent.putExtra("comment_id", commentId)
        if (isDoShowing)
            intent.putExtra("isShowing",1)
        if (changingRating != -1)
            intent.putExtra("rating",changingRating)
        setResult(Activity.RESULT_OK, intent)
        super.onBackPressed()
    }


}