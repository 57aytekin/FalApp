package com.example.sadekahvefal.ui.fragment

import android.content.Intent
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sadekahvefal.base.BaseFragment
import com.example.sadekahvefal.databinding.FragmentHomeBinding
import com.example.sadekahvefal.model.HomeRecyclerViewItem
import com.example.sadekahvefal.ui.activity.comment.CommentActivity
import com.example.sadekahvefal.ui.fragment.home.HomeRecyclerViewAdapter
import com.example.sadekahvefal.ui.fragment.home.HomeViewModel
import com.example.sadekahvefal.utils.ApiState
import com.example.sadekahvefal.utils.ClickListeners
import com.example.sadekahvefal.utils.Constant.CommentItem.age
import com.example.sadekahvefal.utils.Constant.CommentItem.gender
import com.example.sadekahvefal.utils.Constant.CommentItem.image1
import com.example.sadekahvefal.utils.Constant.CommentItem.image2
import com.example.sadekahvefal.utils.Constant.CommentItem.image3
import com.example.sadekahvefal.utils.Constant.CommentItem.post_id
import com.example.sadekahvefal.utils.Constant.CommentItem.relation
import com.example.sadekahvefal.utils.Constant.CommentItem.userName
import com.example.sadekahvefal.utils.Constant.CommentItem.user_id
import com.example.sadekahvefal.utils.Constant.CommentItem.work
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>(), ClickListeners {


    override val viewModel: HomeViewModel by viewModels()
    private val homeRecyclerViewAdapter = HomeRecyclerViewAdapter(this)

    override fun getViewBinding() = FragmentHomeBinding.inflate(layoutInflater)

    override fun onFragmentCreated() {

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
                        homeRecyclerViewAdapter.updateUserTopList(it.data!!)
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
        startActivity(intent)
    }


}