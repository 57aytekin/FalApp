package com.falApp.sadekahvefal.ui.fragment.admin

import android.content.Intent
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.falApp.sadekahvefal.R
import com.falApp.sadekahvefal.base.BaseFragment
import com.falApp.sadekahvefal.databinding.FragmentAdminBinding
import com.falApp.sadekahvefal.model.HomeRecyclerViewItem
import com.falApp.sadekahvefal.ui.activity.comment.CommentActivity
import com.falApp.sadekahvefal.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class AdminFragment : BaseFragment<FragmentAdminBinding, AdminViewModel>(), AdminItemClickListener {
    override fun getViewBinding() = FragmentAdminBinding.inflate(layoutInflater)
    @Inject
    lateinit var prefUtils: PrefUtils
    private val adminAdapter = AdminRecyclerAdapter(this)

    override val viewModel: AdminViewModel by viewModels()

    override fun onFragmentCreated() {

    }

    override fun observe() {
        lifecycleScope.launchWhenResumed {
            viewModel.onJobList.collect {
                when (it) {
                    ApiState.Empty -> { }
                    is ApiState.Failure -> {
                        val liste = listOf<HomeRecyclerViewItem.Post>()
                        adminAdapter.submitList(liste)
                        Toast.makeText(requireContext(), it.errorMessage, Toast.LENGTH_SHORT).show()
                    }
                    ApiState.Loading -> { }
                    is ApiState.Success -> {
                        adminAdapter.submitList(it.data!!.post_list)
                        binding.rvAdmin.adapter = adminAdapter
                    }
                    is ApiState.SuccessMessage -> {
                        Toast.makeText(requireContext(), it.successMessage, Toast.LENGTH_SHORT).show()
                        viewModel.getIsConfirmPost()
                    }
                }
            }
        }
    }
    private fun setIntentValue(post: HomeRecyclerViewItem.Post) {
        val intent = Intent(requireContext(), CommentActivity::class.java)
        intent.putExtra(Constant.CommentItem.image1, post.image_1)
        intent.putExtra(Constant.CommentItem.image2, post.image_2)
        intent.putExtra(Constant.CommentItem.image3, post.image_3)
        intent.putExtra(Constant.CommentItem.userName, post.user_name)
        intent.putExtra(Constant.CommentItem.gender, post.gender)
        intent.putExtra(Constant.CommentItem.relation, post.relation)
        intent.putExtra(Constant.CommentItem.work, post.job)
        intent.putExtra(Constant.CommentItem.age, post.age)
        intent.putExtra(Constant.CommentItem.post_id, post.post_id)
        intent.putExtra(Constant.CommentItem.user_id, post.user_id)
        intent.putExtra(Constant.CommentItem.user_token, post.token)
        intent.putExtra(Constant.CommentItem.commentator, post.commentator)
        startActivity(intent)
    }

    override fun isConfirmItem(post: HomeRecyclerViewItem.Post, isConfirm: Int, token: String, commentator: Int?) {
        //call api set post is_confirm value
        if (commentator == Commentator.Everyone.ordinal){
            viewModel.updatePostConfirmed(post.post_id!!, isConfirm,token)
        } else {
            val userId = prefUtils.getUserId()
            if (userId != 1 && userId != 2 && userId != 3) {
                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle("Dikkat!")
                    .setMessage("Fali yorumlamak için falci hesaplarından birinde olmanız gerekiyor!")
                builder.setPositiveButton("Devam Et") { _, _ ->
                    setIntentValue(post)
                }
                builder.setNegativeButton("İptal") { _, _ -> }
                builder.show()
            } else {
                setIntentValue(post)
            }
        }
    }


}