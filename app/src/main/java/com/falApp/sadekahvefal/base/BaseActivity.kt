package com.falApp.sadekahvefal.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.falApp.sadekahvefal.R
import com.falApp.sadekahvefal.utils.NavigateFragmentParams

abstract class BaseActivity<BindingType : ViewBinding, ViewModelType : BaseViewModel> :
    AppCompatActivity() {

    lateinit var binding: BindingType
    abstract fun onActivityCreated()
    abstract fun observe()
    abstract fun navigateFragment(params: NavigateFragmentParams)
    abstract fun showHideProgress(isShow: Boolean)
    abstract fun getViewBinding(): BindingType
    protected abstract val viewModel: ViewModelType

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_SadeKahveFal)
        super.onCreate(savedInstanceState)
        binding = getViewBinding()
        setContentView(binding.root)
        onActivityCreated()
        observe()
    }

}