package com.example.sadekahvefal.ui.fragment.comment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.sadekahvefal.base.BaseFragment
import com.example.sadekahvefal.databinding.FragmentCommentListBinding
import com.example.sadekahvefal.model.Comment
import com.example.sadekahvefal.ui.activity.comment.CommentViewModel
import com.example.sadekahvefal.ui.activity.commented.CommentedActivity
import com.example.sadekahvefal.utils.ApiState
import com.example.sadekahvefal.utils.CommentListListener
import com.example.sadekahvefal.utils.Constant
import com.example.sadekahvefal.utils.PrefUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@AndroidEntryPoint
class CommentListFragment : BaseFragment<FragmentCommentListBinding, CommentViewModel>(), CommentListListener{
    @Inject
    lateinit var prefUtils: PrefUtils
    override val viewModel: CommentViewModel by viewModels()
    override fun getViewBinding() = FragmentCommentListBinding.inflate(layoutInflater)
    var commentList = listOf<Comment>()
    var mutableList = MutableLiveData<List<Comment>>()
    lateinit var commentAdapter : CommentListAdapter
    lateinit var resultLauncher: ActivityResultLauncher<Intent>

    @SuppressLint("NotifyDataSetChanged")
    override fun onFragmentCreated() {
        viewModel.getComment(prefUtils.getUserId())
        /*CoroutineScope(Dispatchers.Main).launch {
            try {
                val commentList = viewModel.getComment.await()
                commentList.observe(viewLifecycleOwner) { liste ->
                    binding.tvEmpty.visibility = View.GONE
                    commentAdapter.submitList(liste.comment_list)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }*/
        mutableList.observe(viewLifecycleOwner){list ->
            commentAdapter.submitList(list)
            commentAdapter.notifyDataSetChanged()
            commentList = list

        }
        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val commentId = data?.getIntExtra("comment_id",-1)
                val isShowing = data?.getIntExtra("isShowing",-1)
                val rating = data?.getIntExtra("rating",-1)
                commentList.forEach {
                    if (it.comment_id == commentId) {
                        if (isShowing != -1) it.is_showing = isShowing!!
                        if (rating != -1) it.rate = rating!!
                    }
                }
                mutableList.postValue(commentList)
            }
        }
        commentAdapter = CommentListAdapter(this)
        binding.rvCommentList.adapter = commentAdapter

        lifecycleScope.launchWhenResumed {
            viewModel.onJobList.collect {
                when (it) {
                    ApiState.Empty -> {
                        binding.tvEmpty.visibility = View.VISIBLE
                    }
                    is ApiState.Failure -> {
                        if (it.errorMessage.isNullOrEmpty()) {
                            Toast.makeText(requireContext(), "Bir sorun oluştu", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(requireContext(), it.errorMessage, Toast.LENGTH_SHORT).show()
                        }
                    }
                    ApiState.Loading -> {}
                    is ApiState.Success -> {
                        binding.tvEmpty.visibility = View.GONE
                        mutableList.postValue(it.data!!)
                    }
                    is ApiState.SuccessMessage -> {}
                }
            }
        }
    }

    override fun onItemCLick(comment: Comment) {
        val intent = Intent(requireContext(), CommentedActivity::class.java)
        intent.putExtra(Constant.CommentedItem.commentId, comment.comment_id)
        intent.putExtra(Constant.CommentedItem.commentatorId, comment.commentor_id)
        intent.putExtra(Constant.CommentedItem.commentatorName, comment.first_name)
        intent.putExtra(Constant.CommentedItem.commentatorLastName, comment.last_name)
        intent.putExtra(Constant.CommentedItem.commentatorUserName, comment.user_name)
        intent.putExtra(Constant.CommentedItem.commentatorImage, comment.paths)
        intent.putExtra(Constant.CommentedItem.commentText, comment.comment)
        intent.putExtra(Constant.CommentedItem.rating, comment.rate)
        intent.putExtra(Constant.CommentedItem.falDate, comment.post_date)
        intent.putExtra(Constant.CommentedItem.commentDate, comment.comment_date)
        intent.putExtra(Constant.CommentedItem.isShowing, comment.is_showing)

        resultLauncher.launch(intent)
        //startActivity(intent)
    }

}