package com.falApp.sadekahvefal.ui.fragment

import android.annotation.SuppressLint
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.falApp.sadekahvefal.R
import com.falApp.sadekahvefal.base.BaseFragment
import com.falApp.sadekahvefal.base.BaseViewModel
import com.falApp.sadekahvefal.databinding.FragmentSharedFalBinding
import com.falApp.sadekahvefal.ui.activity.MainActivity
import com.falApp.sadekahvefal.utils.Commentator
import com.falApp.sadekahvefal.utils.checkItem

class SharedFalFragment : BaseFragment<FragmentSharedFalBinding, BaseViewModel>() {

    override val viewModel: BaseViewModel by viewModels()

    override fun getViewBinding() = FragmentSharedFalBinding.inflate(layoutInflater)

    @SuppressLint("SetTextI18n")
    override fun onFragmentCreated() {
        val args : SharedFalFragmentArgs by navArgs()
        if (args.commentator == Commentator.Everyone.name)
            binding.tvText.text = "Falınız kısa bir süre sonra onaylanacaktır. Onaylandığında biz sana bildireceğiz."
        else
            binding.tvText.text = "Falınız yoğunluğa bağlı olarak kısa bir sürede yorumlanacaktır." +
                    " Onaylandığında biz sana bildireceğiz."

        binding.btnGoHome.setOnClickListener {
            findMyNavController(this).navigate(R.id.action_sharedFalFragment_to_homeFragment)
            (activity as MainActivity).binding.bottomNavigation.checkItem(R.id.nav_tab_home)
        }
    }
}