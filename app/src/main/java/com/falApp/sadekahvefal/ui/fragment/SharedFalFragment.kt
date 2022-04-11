package com.falApp.sadekahvefal.ui.fragment

import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.falApp.sadekahvefal.R
import com.falApp.sadekahvefal.base.BaseFragment
import com.falApp.sadekahvefal.base.BaseViewModel
import com.falApp.sadekahvefal.databinding.FragmentSharedFalBinding
import com.falApp.sadekahvefal.ui.activity.MainActivity
import com.falApp.sadekahvefal.utils.checkItem

class SharedFalFragment : BaseFragment<FragmentSharedFalBinding, BaseViewModel>() {

    override val viewModel: BaseViewModel by viewModels()

    override fun getViewBinding() = FragmentSharedFalBinding.inflate(layoutInflater)

    override fun onFragmentCreated() {
        val navController: NavController = Navigation.findNavController(requireActivity(), R.id.navHostFragment)
        binding.btnGoHome.setOnClickListener {
            navController.navigate(R.id.action_sharedFalFragment_to_homeFragment)
            (activity as MainActivity).binding.bottomNavigation.checkItem(R.id.nav_tab_home)
        }
    }
}