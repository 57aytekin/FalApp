package com.example.sadekahvefal.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.example.sadekahvefal.R
import com.example.sadekahvefal.base.BaseActivity
import com.example.sadekahvefal.base.BaseViewModel
import com.example.sadekahvefal.databinding.ActivityMainBinding
import com.example.sadekahvefal.utils.NavigateFragmentParams
import com.example.sadekahvefal.utils.checkItem
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding, BaseViewModel>() {
    var navController: NavController? = null

    override fun getViewBinding() = ActivityMainBinding.inflate(layoutInflater)

    override fun onActivityCreated() {
        navController = Navigation.findNavController(this, R.id.navHostFragment)

    }

    override fun observe() {
        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId){
                R.id.nav_tab_home -> {
                    navController?.navigate(R.id.homeFragment)
                    true
                }
                R.id.nav_tab_comments -> {
                    navController?.navigate(R.id.commentListFragment)
                    true
                }

                R.id.nav_tab_add -> {
                    navController?.navigate(R.id.addFragment)
                    //finish()
                    true
                }
                R.id.nav_tab_profile -> {
                    navController?.navigate(R.id.profileFragment)
                    true
                }
                else -> {
                    navController!!.navigate(R.id.homeFragment)
                    true
                }
            }
        }
    }

    override fun navigateFragment(params: NavigateFragmentParams) {
        TODO("Not yet implemented")
    }

    override fun showHideProgress(isShow: Boolean) {
        if (isShow) binding.progressMain.visibility = View.VISIBLE
        else binding.progressMain.visibility = View.GONE
    }

    override val viewModel: BaseViewModel
        get() = TODO("Not yet implemented")

    override fun onBackPressed() {
        when(navController?.currentDestination!!.id) {
            R.id.profileFragment -> {
                navController!!.navigate(R.id.homeFragment)
                binding.bottomNavigation.checkItem(R.id.nav_tab_home)
            }
            R.id.addFragment -> {
                navController!!.navigate(R.id.homeFragment)
                binding.bottomNavigation.checkItem(R.id.nav_tab_home)
            }
            R.id.homeFragment -> {
                finish()
            }
        }
    }
}