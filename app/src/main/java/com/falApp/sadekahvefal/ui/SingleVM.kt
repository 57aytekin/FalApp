package com.falApp.sadekahvefal.ui

import android.app.Application
import com.falApp.sadekahvefal.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SingleVM
@Inject constructor(myApp: Application) : BaseViewModel(app = myApp)