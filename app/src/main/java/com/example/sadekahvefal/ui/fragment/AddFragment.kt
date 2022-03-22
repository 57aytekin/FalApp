package com.example.sadekahvefal.ui.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64.*
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.example.sadekahvefal.R
import com.example.sadekahvefal.base.BaseFragment
import com.example.sadekahvefal.databinding.FragmentAddBinding
import com.example.sadekahvefal.model.HomeRecyclerViewItem
import com.example.sadekahvefal.ui.PostViewModel
import com.example.sadekahvefal.ui.fragment.bottomSheet.UserInfBottomSheet
import com.example.sadekahvefal.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import java.io.ByteArrayOutputStream
import java.lang.Byte.decode
import java.util.*
import javax.inject.Inject

private const val IMG_RESULT = 777

@AndroidEntryPoint
class AddFragment : BaseFragment<FragmentAddBinding, PostViewModel>(), BottomSheetClickListener {
    // TODO: Rename and change types of parameters
    @Inject
    lateinit var prefUtils: PrefUtils
    private var param1: String? = null
    private var param2: String? = null
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

    override fun getViewBinding() = FragmentAddBinding.inflate(layoutInflater)

    override fun onFragmentCreated() {

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
            if (validShare()) {
                val post = HomeRecyclerViewItem.Post(image_1 = stringImage1!!, image_2 = stringImage2!!, image_3 = stringImage3!!,
                    user_id = prefUtils.getUserId(), gender_id = getItemId(genderList, binding.tvGender.text.toString()),
                    job_id = getItemId(jobList, binding.tvJob.text.toString()),
                    relation_id = getItemId(relationList, binding.tvRelation.text.toString()),
                    age = binding.tvAge.text.toString().toInt(), ekstra_infromation = "" )

                val rnds = (0..1000).random()
                val name1 = prefUtils.getUserName()+"1"+rnds.toString()
                val name2 = prefUtils.getUserName()+"2"+rnds.toString()
                val name3 = prefUtils.getUserName()+"3"+rnds.toString()
                viewModel.savePost(post, name1, name2, name3)
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
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        resultLauncher.launch(intent)
        //startActivityForResult(intent, IMG_RESULT)
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
                        binding.progressAddFragment.visibility = View.GONE
                        binding.btnShare.isClickable = true
                        Toast.makeText(requireContext(), it.successMessage, Toast.LENGTH_SHORT).show()
                    }
                }
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