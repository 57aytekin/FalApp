package com.example.sadekahvefal.ui

import android.app.Application
import com.example.sadekahvefal.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SingleVM
@Inject constructor(myApp: Application) : BaseViewModel(app = myApp)