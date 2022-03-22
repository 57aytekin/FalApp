package com.example.sadekahvefal.ui.fragment.profile

import androidx.fragment.app.viewModels
import com.example.sadekahvefal.base.BaseFragment
import com.example.sadekahvefal.databinding.FragmentProfileBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding, ProfileViewModel>() {
    override val viewModel: ProfileViewModel by viewModels()

    override fun getViewBinding() = FragmentProfileBinding.inflate(layoutInflater)

    override fun onFragmentCreated() {

    }

}