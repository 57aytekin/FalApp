package com.falApp.sadekahvefal.ui.activity

import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.falApp.sadekahvefal.R
import com.falApp.sadekahvefal.base.BaseActivity
import com.falApp.sadekahvefal.base.BaseViewModel
import com.falApp.sadekahvefal.databinding.ActivityMainBinding
import com.falApp.sadekahvefal.utils.NavigateFragmentParams
import com.falApp.sadekahvefal.utils.PrefUtils
import com.falApp.sadekahvefal.utils.checkItem
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>() {
    @Inject
    lateinit var prefUtils: PrefUtils
    override val viewModel: MainViewModel by viewModels()
    var navController: NavController? = null

    val mutableBadgeCount: MutableLiveData<Int> = MutableLiveData()

    override fun getViewBinding() = ActivityMainBinding.inflate(layoutInflater)

    override fun onActivityCreated() {
        //update is_last_login and token
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) return@addOnCompleteListener
            val token = task.result
            viewModel.updateTokenAndLastLogin(token)
        }
        navController = Navigation.findNavController(this, R.id.navHostFragment)
        mutableBadgeCount.observe(this){count ->
            if (count > 0) binding.bottomNavigation.getOrCreateBadge(R.id.nav_tab_comments).number = count
            else binding.bottomNavigation.removeBadge(R.id.nav_tab_comments)
        }
        if (prefUtils.getIsAdmin() == 0) {
            binding.bottomNavigation.menu.removeItem(R.id.nav_tab_admin)
        }

    }

    fun updateBadge(count : Int) {
        mutableBadgeCount.postValue(count)
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
                R.id.nav_tab_admin -> {
                    navController?.navigate(R.id.adminFragment)
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
            R.id.adminFragment -> {
                navController!!.navigate(R.id.homeFragment)
                binding.bottomNavigation.checkItem(R.id.nav_tab_home)
            }
            R.id.homeFragment -> {
                finish()
            }
            R.id.editProfileFragment -> {
                navController!!.navigate(R.id.profileFragment)
            }
        }
    }
}