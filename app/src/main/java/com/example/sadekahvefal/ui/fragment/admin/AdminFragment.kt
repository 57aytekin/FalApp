package com.example.sadekahvefal.ui.fragment.admin

import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.sadekahvefal.base.BaseFragment
import com.example.sadekahvefal.databinding.FragmentAdminBinding
import com.example.sadekahvefal.model.HomeRecyclerViewItem
import com.example.sadekahvefal.utils.AdminItemClickListener
import com.example.sadekahvefal.utils.ApiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class AdminFragment : BaseFragment<FragmentAdminBinding, AdminViewModel>(), AdminItemClickListener {
    override fun getViewBinding() = FragmentAdminBinding.inflate(layoutInflater)
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

    override fun isConfirmItem(postId: Int, isConfirm: Int) {
        //call api set post is_confirm value
        viewModel.updatePostConfirmed(postId, isConfirm)
    }


}