package com.example.sadekahvefal.ui.fragment.profile

import android.app.Application
import com.example.sadekahvefal.base.BaseViewModel
import com.example.sadekahvefal.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class ProfileViewModel @Inject constructor(
    application: Application,
    private val profileRepository: ProfileRepository
) : BaseViewModel(application) {

}