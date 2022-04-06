package com.falApp.sadekahvefal.ui.fragment.bottomSheet

import com.falApp.sadekahvefal.base.BaseBottomSheetFragment
import com.falApp.sadekahvefal.databinding.BottomSheetGenderBinding
import com.falApp.sadekahvefal.utils.BottomSheetClickListener

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