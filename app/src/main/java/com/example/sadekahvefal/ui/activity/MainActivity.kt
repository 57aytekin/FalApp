package com.example.sadekahvefal.ui.activity

import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.sadekahvefal.R
import com.example.sadekahvefal.base.BaseActivity
import com.example.sadekahvefal.base.BaseViewModel
import com.example.sadekahvefal.databinding.ActivityMainBinding
import com.example.sadekahvefal.utils.NavigateFragmentParams
import com.example.sadekahvefal.utils.PrefUtils
import com.example.sadekahvefal.utils.checkItem
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding, BaseViewModel>() {
    @Inject
    lateinit var prefUtils: PrefUtils
    override val viewModel: BaseViewModel by viewModels()
    var navController: NavController? = null

    val mutableBadgeCount: MutableLiveData<Int> = MutableLiveData()

    override fun getViewBinding() = ActivityMainBinding.inflate(layoutInflater)

    override fun onActivityCreated() {
        navController = Navigation.findNavController(this, R.id.navHostFragment)
        mutableBadgeCount.observe(this){count ->
            if (count > 0) binding.bottomNavigation.getOrCreateBadge(R.id.nav_tab_comments).number = count
            else binding.bottomNavigation.removeBadge(R.id.nav_tab_comments)
            //prefUtils.save(BADGECOUNT, count)
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
            R.id.homeFragment -> {
                finish()
            }
            R.id.editProfileFragment -> {
                navController!!.navigate(R.id.profileFragment)
            }
        }
    }
}