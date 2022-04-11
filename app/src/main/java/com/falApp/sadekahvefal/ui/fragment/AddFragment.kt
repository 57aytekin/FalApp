package com.falApp.sadekahvefal.ui.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.falApp.sadekahvefal.R
import com.falApp.sadekahvefal.base.BaseFragment
import com.falApp.sadekahvefal.databinding.FragmentAddBinding
import com.falApp.sadekahvefal.model.HomeRecyclerViewItem
import com.falApp.sadekahvefal.ui.PostViewModel
import com.falApp.sadekahvefal.ui.fragment.bottomSheet.UserInfBottomSheet
import com.falApp.sadekahvefal.utils.*
import com.github.dhaval2404.imagepicker.ImagePicker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import javax.inject.Inject


@AndroidEntryPoint
class AddFragment : BaseFragment<FragmentAddBinding, PostViewModel>(), BottomSheetClickListener {
    @Inject
    lateinit var prefUtils: PrefUtils
    private var navController: NavController? = null
    override val viewModel : PostViewModel by viewModels()
    private var jobList = mutableListOf<String>()
    lateinit var resultLauncher : ActivityResultLauncher<Intent>
    //Bitmaps
    private var bitmapCoffee1: Bitmap? = null
    private var bitmapCoffee2: Bitmap? = null
    private var bitmapCoffee3: Bitmap? = null
    private var stringImage1 : String? = null
    private var stringImage2 : String? = null
    private var stringImage3 : String? = null
    private var clickedImage = 1
    lateinit var clickedCoffeeImage : MutableLiveData<Int>
    var genderList = listOf<String>()
    var relationList = listOf<String>()
    var ageLit = listOf<String>()
    private var currentGold = 0

    override fun getViewBinding() = FragmentAddBinding.inflate(layoutInflater)

    override fun onFragmentCreated() {
        currentGold = prefUtils.getUserGold()
        navController = Navigation.findNavController(requireActivity(), R.id.navHostFragment)
        binding.appbar.tvCoin.text = prefUtils.getUserGold().toString()

        val genderBottomSheet = UserInfBottomSheet(this)
        genderList = resources.getStringArray(R.array.gender_array).asList()

        val jobBotomSheet = UserInfBottomSheet(this)
        val relationBottomSheet = UserInfBottomSheet(this)
        relationList = resources.getStringArray(R.array.reletion_array).asList()

        val ageBottomSheet = UserInfBottomSheet(this)
        ageLit = resources.getStringArray(R.array.age_array).asList()

        clickedCoffeeImage = MutableLiveData()
        clickedCoffeeImage.observe(viewLifecycleOwner) { clicked ->
            binding.tvPhotoError.visibility = View.GONE
            clickedImage = clicked
        }

        binding.btnShare.setOnClickListener {
            if (currentGold >= 5) {
                val updatedGold = currentGold -5
                if (validShare()) {
                    val post = HomeRecyclerViewItem.Post(image_1 = stringImage1!!, image_2 = stringImage2!!, image_3 = stringImage3!!,
                        user_id = prefUtils.getUserId(), gender_id = getItemId(genderList, binding.tvGender.text.toString()),
                        job_id = getItemId(jobList, binding.tvJob.text.toString()),
                        relation_id = getItemId(relationList, binding.tvRelation.text.toString()),
                        age = binding.tvAge.text.toString().toInt(), ekstra_infromation = binding.etEkstraInf.text.toString() )

                    val rnds = (0..10000).random()
                    val name1 = prefUtils.getUserName()+"1"+rnds.toString()
                    val name2 = prefUtils.getUserName()+"2"+rnds.toString()
                    val name3 = prefUtils.getUserName()+"3"+rnds.toString()
                    viewModel.savePost(post, name1, name2, name3, updatedGold)
                }
            }else {
                showDialog()
            }

        }

        binding.cvCoffeeImage1.setOnClickListener {
            clickedCoffeeImage.postValue(1)
            selectImage()
        }
        binding.cvCoffeeImage2.setOnClickListener {
            clickedCoffeeImage.postValue(2)
            selectImage()
        }
        binding.cvCoffeeImage3.setOnClickListener {
            clickedCoffeeImage.postValue(3)
            selectImage()
        }

        //Gender
        binding.tvGender.setOnClickListener {
            genderBottomSheet.updateData(genderList)
            genderBottomSheet.whichData(GetSelectedBottomSheet.Gender.ordinal)
            fragmentManager?.let { it1 -> genderBottomSheet.show(it1,"gender") }
            binding.tvGenderError.visibility = View.GONE
        }
        //Job
        binding.tvJob.setOnClickListener {
            jobBotomSheet.updateData(jobList)
            jobBotomSheet.whichData(GetSelectedBottomSheet.Job.ordinal)
            fragmentManager?.let { it1 -> jobBotomSheet.show(it1,"asd1") }
            binding.tvJobError.visibility = View.GONE
        }
        //Relation
        binding.tvRelation.setOnClickListener {
            relationBottomSheet.whichData(GetSelectedBottomSheet.Relation.ordinal)
            relationBottomSheet.updateData(relationList)
            fragmentManager?.let { it1 -> relationBottomSheet.show(it1,"asd2") }
            binding.tvRelationError.visibility = View.GONE
        }
        //Age
        binding.tvAge.setOnClickListener {
            ageBottomSheet.whichData(GetSelectedBottomSheet.Age.ordinal)
            ageBottomSheet.updateData(ageLit)
            fragmentManager?.let { it1 -> ageBottomSheet.show(it1,"asd3") }
            binding.tvAgeError.visibility = View.GONE
        }

        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val path = data?.data
                //bitmap = MediaStore.Images.Media.getBitmap(contentResolver, path)
                when (clickedImage) {
                    1 -> {
                        bitmapCoffee1 = handleSamplingAndRotationBitmap(requireContext(), path!!)
                        val bitmap1 = scaleDown(bitmapCoffee1!!, 700f, true )
                        binding.ivCoffee1.setImageBitmap(bitmap1)
                        stringImage1 = Converters().fromBitmap(bitmap1)
                    }
                    2 -> {
                        bitmapCoffee2 = handleSamplingAndRotationBitmap(requireContext(), path!!)
                        val bitmap1 = scaleDown(bitmapCoffee2!!, 700f, true )
                        binding.ivCoffee2.setImageBitmap(bitmap1)
                        stringImage2 = Converters().fromBitmap(bitmap1)
                    }
                    3 -> {
                        bitmapCoffee3 = handleSamplingAndRotationBitmap(requireContext(), path!!)
                        val bitmap1 = scaleDown(bitmapCoffee3!!, 700f, true )
                        binding.ivCoffee3.setImageBitmap(bitmap1)
                        stringImage3 = Converters().fromBitmap(bitmap1)
                    }
                }

            } else if (result.resultCode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(requireContext(), ImagePicker.getError(result.data), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getItemId(list: List<String>, value: String): Int {
        var indexValue = -1
        list.forEachIndexed { index, s ->
            if (s == value) {
                indexValue = index
            }
        }
        return indexValue
    }

    private fun validShare() : Boolean {
        if (stringImage1.isNullOrEmpty() || stringImage2.isNullOrEmpty() || stringImage3.isNullOrEmpty()) {
            binding.tvPhotoError.visibility = View.VISIBLE
            return false
        }
        if (binding.tvGender.text == genderList[0]) {
            binding.tvGenderError.visibility = View.VISIBLE
            return false
        }
        if (binding.tvJob.text == jobList[0]) {
            binding.tvJobError.visibility = View.VISIBLE
            return false
        }
        if (binding.tvRelation.text == relationList[0]) {
            binding.tvRelationError.visibility = View.VISIBLE
            return false
        }
        if (binding.tvAge.text == ageLit[0]) {
            binding.tvAgeError.visibility = View.VISIBLE
            return false
        }

        return true
    }

    private fun selectImage() {
        /*val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        resultLauncher.launch(intent)*/
        //startActivityForResult(intent, IMG_RESULT)*/

        ImagePicker.with(requireActivity())
            .crop(1f,1f)
            .compress(1024)
            .maxResultSize(1080, 1080)
            .createIntent {
                resultLauncher.launch(it)
            }
    }

    override fun observe() {
        lifecycleScope.launchWhenResumed {
            viewModel.onJobList.collect {
                when (it) {
                    ApiState.Empty -> {
                        binding.progressAddFragment.visibility = View.GONE
                        binding.btnShare.isClickable = true
                    }
                    is ApiState.Failure -> {
                        binding.progressAddFragment.visibility = View.GONE
                        binding.btnShare.isClickable = true
                        if (it.errorMessage.isNullOrEmpty()) {
                            Toast.makeText(requireContext(), "Bir sorun oluştu", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(requireContext(), it.errorMessage, Toast.LENGTH_SHORT).show()
                        }
                    }
                    ApiState.Loading -> {
                        binding.progressAddFragment.visibility = View.VISIBLE
                        binding.btnShare.isClickable = false
                    }
                    is ApiState.Success -> {
                        binding.progressAddFragment.visibility = View.GONE
                        binding.btnShare.isClickable = true
                        jobList.addAll(it.data!!.map { item ->
                            item.name_tr
                        })
                    }
                    is ApiState.SuccessMessage -> {
                        //update local gold
                        binding.progressAddFragment.visibility = View.GONE
                        binding.btnShare.isClickable = true
                        navController?.navigate(R.id.action_addFragment_to_sharedFalFragment)
                    }
                }
            }
        }
    }
    private fun showDialog() {
        val dialog = CustomDialog(requireContext(), "",null, null)
        dialog.show()
        dialog.apply {
            setYesListener {
                //Video açılacak ve gold update edilecek.
                Toast.makeText(requireContext(), "open video and update coin", Toast.LENGTH_SHORT)
                    .show()
                dismiss()
            }
            setNoListener {
                dismiss()
            }
        }
    }

    override fun onSelectedItem(text: String, whichData: Int) {
        when (whichData) {
            GetSelectedBottomSheet.Gender.ordinal -> { binding.tvGender.text = text}
            GetSelectedBottomSheet.Job.ordinal -> { binding.tvJob.text = text }
            GetSelectedBottomSheet.Age.ordinal -> { binding.tvAge.text = text }
            GetSelectedBottomSheet.Relation.ordinal -> { binding.tvRelation.text = text }
        }
    }

}