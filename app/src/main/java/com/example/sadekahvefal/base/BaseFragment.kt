package com.example.sadekahvefal.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<BindingType : ViewBinding, ViewModelType : BaseViewModel> :
    Fragment() {

    private val baseActivity by lazy { activity as BaseActivity<*, *>? }

    lateinit var binding: BindingType
    protected abstract val viewModel: ViewModelType
    abstract fun getViewBinding(): BindingType
    abstract fun onFragmentCreated()
    open fun observe() {}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = getViewBinding()
        onFragmentCreated()
        observe()
        observeActions()
        return binding.root
    }

    private fun observeActions() {
        viewModel.navigateFragmentDetection.observe(viewLifecycleOwner) {
            baseActivity?.navigateFragment(it)
        }
        viewModel.loadingDetection.observe(viewLifecycleOwner) {
            baseActivity?.showHideProgress(it)
        }
    }


}