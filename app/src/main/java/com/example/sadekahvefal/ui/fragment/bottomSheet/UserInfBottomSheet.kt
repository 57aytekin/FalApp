package com.example.sadekahvefal.ui.fragment.bottomSheet

import androidx.lifecycle.MutableLiveData
import com.example.sadekahvefal.base.BaseBottomSheetFragment
import com.example.sadekahvefal.databinding.BottomSheetGenderBinding
import com.example.sadekahvefal.utils.BottomSheetClickListener

class UserInfBottomSheet(
    private val clickListener: BottomSheetClickListener
) : BaseBottomSheetFragment<BottomSheetGenderBinding>() {
    override fun getViewBinding() = BottomSheetGenderBinding.inflate(layoutInflater)

    private var liste  = listOf<String>()
    private var whichData = -1
    override fun onFragmentCreated() {
        binding.apply {
            numberPicker.minValue = 1;
            numberPicker.maxValue = liste.size
            numberPicker.displayedValues = liste.toTypedArray()
            numberPicker.value = 1
            numberPicker.wrapSelectorWheel = false;

            numberPicker.setOnValueChangedListener { picker, oldVal, newVal ->
                clickListener.onSelectedItem(liste[newVal-1], whichData)
            }
        }
    }

    fun updateData(lis: List<String>) {
        liste = lis
    }
    fun whichData(which: Int) {
        whichData = which
    }
}