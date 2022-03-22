package com.example.sadekahvefal.base

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.AndroidViewModel
import androidx.navigation.NavOptions
import androidx.navigation.fragment.FragmentNavigator
import com.example.sadekahvefal.utils.NavigateFragmentParams
import com.example.sadekahvefal.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
open class BaseViewModel
@Inject constructor(
    app: Application
) : AndroidViewModel(app) {

    val navigateFragmentDetection by lazy { SingleLiveEvent<NavigateFragmentParams>() }
    val loadingDetection by lazy { SingleLiveEvent<Boolean>() }


    fun navigateFragment(
        navAction: Int,
        bundle: Bundle? = null,
        navOptions: NavOptions? = null,
        extras: FragmentNavigator.Extras? = null
    ) {
        val params = NavigateFragmentParams(navAction, bundle, navOptions, extras)
        navigateFragmentDetection.postValue(params)
    }
}