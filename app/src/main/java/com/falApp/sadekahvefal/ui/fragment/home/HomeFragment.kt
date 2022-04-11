package com.falApp.sadekahvefal.ui.fragment.home

import android.content.Intent
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.falApp.sadekahvefal.base.BaseFragment
import com.falApp.sadekahvefal.databinding.FragmentHomeBinding
import com.falApp.sadekahvefal.model.HomeRecyclerViewItem
import com.falApp.sadekahvefal.ui.activity.MainActivity
import com.falApp.sadekahvefal.ui.activity.comment.CommentActivity
import com.falApp.sadekahvefal.utils.ApiState
import com.falApp.sadekahvefal.utils.ClickListeners
import com.falApp.sadekahvefal.utils.Constant.CommentItem.age
import com.falApp.sadekahvefal.utils.Constant.CommentItem.gender
import com.falApp.sadekahvefal.utils.Constant.CommentItem.image1
import com.falApp.sadekahvefal.utils.Constant.CommentItem.image2
import com.falApp.sadekahvefal.utils.Constant.CommentItem.image3
import com.falApp.sadekahvefal.utils.Constant.CommentItem.post_id
import com.falApp.sadekahvefal.utils.Constant.CommentItem.relation
import com.falApp.sadekahvefal.utils.Constant.CommentItem.userName
import com.falApp.sadekahvefal.utils.Constant.CommentItem.user_id
import com.falApp.sadekahvefal.utils.Constant.CommentItem.user_token
import com.falApp.sadekahvefal.utils.Constant.CommentItem.work
import com.falApp.sadekahvefal.utils.PrefUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>(), ClickListeners {
    @Inject
    lateinit var prefUtils: PrefUtils

    override val viewModel: HomeViewModel by viewModels()
    private val homeRecyclerViewAdapter = HomeRecyclerViewAdapter(this)

    override fun getViewBinding() = FragmentHomeBinding.inflate(layoutInflater)

    override fun onFragmentCreated() {
        binding.appbar.tvCoin.text = prefUtils.getUserGold().toString()

        lifecycleScope.launchWhenCreated {
            viewModel.onTopUserList.collect {
                when (it) {
                    ApiState.Empty -> {

                    }
                    is ApiState.Failure -> {
                        if (it.errorMessage.isNullOrEmpty()) {
                            Toast.makeText(requireContext(), "Bir sorun oluştu", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(requireContext(), it.errorMessage, Toast.LENGTH_SHORT).show()
                        }
                    }
                    ApiState.Loading -> { }
                    is ApiState.Success -> {
                        homeRecyclerViewAdapter.updateUserTopList(it.data!!.top_user)
                        (activity as MainActivity?)?.updateBadge(it.data.badgeCount)
                        (activity as MainActivity?)?.updateGold(it.data.gold)
                    }

                    is ApiState.SuccessMessage  -> Toast.makeText(
                        requireContext(),
                        it.successMessage,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        binding.homeRecycler.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = homeRecyclerViewAdapter
        }

        viewModel.postList.observe(this){
            homeRecyclerViewAdapter.submitData(lifecycle, it)
        }
    }

    override fun onButtonCLick(post: HomeRecyclerViewItem.Post) {
        val intent = Intent(requireContext(), CommentActivity::class.java)
        intent.putExtra(image1, post.image_1)
        intent.putExtra(image2, post.image_2)
        intent.putExtra(image3, post.image_3)
        intent.putExtra(userName, post.user_name)
        intent.putExtra(gender, post.gender)
        intent.putExtra(relation, post.relation)
        intent.putExtra(work, post.job)
        intent.putExtra(age, post.age)
        intent.putExtra(post_id, post.post_id)
        intent.putExtra(user_id, post.user_id)
        intent.putExtra(user_token, post.token)
        startActivity(intent)
    }


}