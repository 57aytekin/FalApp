package com.falApp.sadekahvefal.ui.activity.comment

import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.falApp.sadekahvefal.base.BaseActivity
import com.falApp.sadekahvefal.databinding.ActivityCommentBinding
import com.falApp.sadekahvefal.ui.fragment.SliderAdapter
import com.falApp.sadekahvefal.utils.ApiState
import com.falApp.sadekahvefal.utils.Constant
import com.falApp.sadekahvefal.utils.NavigateFragmentParams
import com.falApp.sadekahvefal.utils.PrefUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class CommentActivity : BaseActivity<ActivityCommentBinding, CommentViewModel>() {

    override val viewModel: CommentViewModel by viewModels()
    @Inject
    lateinit var prefUtils: PrefUtils
    override fun getViewBinding() = ActivityCommentBinding.inflate(layoutInflater)

    override fun onActivityCreated() {
        val sliderAdapter: SliderAdapter by lazy { SliderAdapter() }
        val pagerSnapHelper = PagerSnapHelper()
        pagerSnapHelper.attachToRecyclerView(binding.rcSliderImage)

        binding.rcSliderImage.adapter = sliderAdapter
        val userName = intent.getStringExtra(Constant.CommentItem.userName)
        val gender = intent.getStringExtra(Constant.CommentItem.gender)
        val relation = intent.getStringExtra(Constant.CommentItem.relation)
        val work = intent.getStringExtra(Constant.CommentItem.work)
        val age = intent.getIntExtra(Constant.CommentItem.age, 0)
        val postId = intent.getIntExtra(Constant.CommentItem.post_id, 0)
        val userId = intent.getIntExtra(Constant.CommentItem.user_id, 0)
        val token = intent.getStringExtra(Constant.CommentItem.user_token)
        val image1 = intent.getStringExtra(Constant.CommentItem.image1)
        val image2 = intent.getStringExtra(Constant.CommentItem.image2)
        val image3 = intent.getStringExtra(Constant.CommentItem.image3)
        val urlList = mutableListOf(image1!!, image2!!, image3!!)
        sliderAdapter.submitList(urlList)

        binding.btnCommentShare.setOnClickListener {
            val comment = binding.etComment.text
            if (comment.isNotEmpty())
                viewModel.saveComment(postId, prefUtils.getUserId(), userId, comment.toString(), token!!)
            else
                Toast.makeText(this, "Lütfen bir yorum giriniz", Toast.LENGTH_SHORT).show()
        }

        //Set textview
        binding.apply {
            tvUserName.text = userName
            tvAge.text = age.toString()
            tvGender.text = gender
            tvRelation.text = relation
            tvWork.text = work
        }
    }

    override fun observe() {
        binding.rcSliderImage.setOnScrollChangeListener { _, _, _, _, _ ->
            var position = 0
            if (binding.rcSliderImage.layoutManager != null)
                position = (binding.rcSliderImage.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
            binding.indicator.selection = position
        }

        lifecycleScope.launchWhenResumed {
            viewModel.onJobList.collect {
                when (it) {
                    ApiState.Empty -> {
                        binding.progressAddFragment.visibility = View.GONE
                        binding.btnCommentShare.isClickable = true
                    }
                    is ApiState.Failure -> {
                        binding.progressAddFragment.visibility = View.GONE
                        binding.btnCommentShare.isClickable = true
                        if (it.errorMessage.isNullOrEmpty()) {
                            Toast.makeText(this@CommentActivity, "Bir sorun oluştu", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this@CommentActivity, it.errorMessage, Toast.LENGTH_SHORT).show()
                        }
                    }
                    ApiState.Loading -> {
                        binding.progressAddFragment.visibility = View.VISIBLE
                        binding.btnCommentShare.isClickable = false
                    }
                    is ApiState.Success -> {
                        binding.progressAddFragment.visibility = View.GONE
                        binding.btnCommentShare.isClickable = true
                    }
                    is ApiState.SuccessMessage -> {
                        binding.progressAddFragment.visibility = View.GONE
                        binding.btnCommentShare.isClickable = true
                        Toast.makeText(this@CommentActivity, it.successMessage, Toast.LENGTH_SHORT).show()
                        this@CommentActivity.finish()
                    }
                }
            }
        }

    }

    override fun navigateFragment(params: NavigateFragmentParams) {
        TODO("Not yet implemented")
    }

    override fun showHideProgress(isShow: Boolean) {
        TODO("Not yet implemented")
    }
}