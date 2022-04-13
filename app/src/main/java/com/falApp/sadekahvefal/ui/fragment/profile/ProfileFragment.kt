package com.falApp.sadekahvefal.ui.fragment.profile

import android.annotation.SuppressLint
import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.falApp.sadekahvefal.R
import com.falApp.sadekahvefal.base.BaseFragment
import com.falApp.sadekahvefal.databinding.FragmentProfileBinding
import com.falApp.sadekahvefal.model.HomeRecyclerViewItem
import com.falApp.sadekahvefal.ui.activity.LoginActivity
import com.falApp.sadekahvefal.utils.ApiState
import com.falApp.sadekahvefal.utils.PrefUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import java.lang.reflect.InvocationTargetException
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding, ProfileViewModel>() {
    private val args : ProfileFragmentArgs? by navArgs()
    override val viewModel: ProfileViewModel by viewModels()
    @Inject
    lateinit var prefUtils: PrefUtils
    private val profilePostAdapter : ProfilePostAdapter by lazy {ProfilePostAdapter()}
    private lateinit var userInf : HomeRecyclerViewItem.User

    override fun getViewBinding() = FragmentProfileBinding.inflate(layoutInflater)

    @SuppressLint("SetTextI18n")
    override fun onFragmentCreated() {
        var userId = prefUtils.getUserId()
        try { if (args != null) userId = args?.userId!!.toInt() }
        catch (e: InvocationTargetException) { userId = prefUtils.getUserId()}
        if (userId != prefUtils.getUserId()) {
            binding.ivProfileEdit.visibility = View.GONE
            binding.cvWinAndShare.visibility = View.GONE
        }

        viewModel.getUserProfile(userId)
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
            findMyNavController(this).navigate(action)
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