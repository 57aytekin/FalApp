package com.example.sadekahvefal.ui.fragment.profile

import android.annotation.SuppressLint
import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.example.sadekahvefal.R
import com.example.sadekahvefal.base.BaseFragment
import com.example.sadekahvefal.databinding.FragmentProfileBinding
import com.example.sadekahvefal.model.HomeRecyclerViewItem
import com.example.sadekahvefal.ui.activity.LoginActivity
import com.example.sadekahvefal.utils.ApiState
import com.example.sadekahvefal.utils.PrefUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding, ProfileViewModel>() {
    var navController: NavController? = null
    override val viewModel: ProfileViewModel by viewModels()
    @Inject
    lateinit var prefUtils: PrefUtils
    private val profilePostAdapter : ProfilePostAdapter by lazy {ProfilePostAdapter()}
    private lateinit var userInf : HomeRecyclerViewItem.User

    override fun getViewBinding() = FragmentProfileBinding.inflate(layoutInflater)

    @SuppressLint("SetTextI18n")
    override fun onFragmentCreated() {
        navController = Navigation.findNavController(requireActivity(), R.id.navHostFragment)
        binding.userProfileAppbar.rlExit.setOnClickListener {
            prefUtils.clear()
            Toast.makeText(requireContext(), "Çıkış yapıldı.", Toast.LENGTH_SHORT).show()
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            requireActivity().finish()
        }
        binding.ivProfileEdit.setOnClickListener {
            val action = ProfileFragmentDirections.
            actionProfileFragmentToEditProfileFragment(
                userInf.email, userInf.user_name, userInf.first_name, userInf.last_name!!, userInf.paths!!
            )
            navController?.navigate(action)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun observe() {
        lifecycleScope.launchWhenResumed {
            viewModel.onJobList.collect {
                when (it) {
                    ApiState.Empty -> {}
                    is ApiState.Failure -> {
                        binding.progressProfileFragment.visibility = View.GONE
                        if (it.errorMessage.isNullOrEmpty()) {
                            Toast.makeText(requireContext(), "Bir sorun oluştu", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(requireContext(), it.errorMessage, Toast.LENGTH_SHORT).show()
                        }
                    }
                    ApiState.Loading -> {
                        binding.progressProfileFragment.visibility = View.VISIBLE
                    }
                    is ApiState.Success -> {
                        binding.progressProfileFragment.visibility = View.GONE
                        it.data?.user_profile.apply {
                            if (!this?.paths.isNullOrEmpty())
                                Glide.with(requireContext()).load(this?.paths).into(binding.ivProfilePhoto)
                            binding.tvProfileName.text = this?.first_name + " " + this?.last_name
                            binding.tvUserScore.text = this?.score.toString() + " Puan"
                            binding.tvUserCoin.text = this?.gold.toString() + " Altın"
                            binding.tvProfileTotalComment.text = this?.comment_count.toString()
                            userInf = this!!
                        }
                        if (!it.data?.user_profile?.user_post.isNullOrEmpty()) {
                            binding.tvEmptyPost.visibility = View.GONE
                            val postList = it.data!!.user_profile.user_post
                            profilePostAdapter.submitList(postList)
                            binding.rvProfilePhotos.adapter = profilePostAdapter
                            binding.tvProfileTotalPost.text = it.data.user_profile.user_post.size.toString()
                        } else {
                            binding.tvEmptyPost.visibility = View.VISIBLE
                            binding.tvProfileTotalPost.text = "0"
                        }
                    }
                    is ApiState.SuccessMessage -> { }
                }
            }
        }
    }

}