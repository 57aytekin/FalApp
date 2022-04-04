package com.example.sadekahvefal.ui.fragment.profile.edit_profile

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.sadekahvefal.R
import com.example.sadekahvefal.base.BaseFragment
import com.example.sadekahvefal.base.BaseViewModel
import com.example.sadekahvefal.databinding.FragmentEditProfileBinding
import com.example.sadekahvefal.ui.activity.RegisterActivity.Companion.checkTurkishCharacter
import com.example.sadekahvefal.ui.activity.RegisterActivity.Companion.textInputSettings
import com.example.sadekahvefal.ui.fragment.profile.ProfileViewModel
import com.example.sadekahvefal.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class EditProfileFragment : BaseFragment<FragmentEditProfileBinding, ProfileViewModel>() {
    @Inject
    lateinit var prefUtils: PrefUtils
    private val args : EditProfileFragmentArgs by navArgs()
    private var navController: NavController? = null
    override val viewModel: ProfileViewModel by viewModels()
    private var userImage: Bitmap? = null
    private lateinit var resultLauncher : ActivityResultLauncher<Intent>
    private var stringImage1 : String? = null

    override fun getViewBinding() = FragmentEditProfileBinding.inflate(layoutInflater)

    override fun onFragmentCreated() {
        navController = Navigation.findNavController(requireActivity(), R.id.navHostFragment)
        binding.etEditName.setText(args.firstName)
        binding.etEditLastName.setText(args.lastName)
        binding.etEditUserName.setText(args.userName)

        if (!args.path.isNullOrEmpty()) {
            Glide.with(this).load(args.path).into(binding.ivEditProfilePhoto)
        }
        binding.ivEditProfilePhoto.setOnClickListener { selectImage() }
        binding.tvEditProfilePhoto.setOnClickListener { selectImage() }
        binding.appbar.tvBack.setOnClickListener {
            navController?.navigate(R.id.profileFragment)
        }
        binding.appbar.rlConfirm.setOnClickListener {
            //Call api
            val userName = binding.etEditUserName.text?.trim().toString()
            val lastName = binding.etEditLastName.text?.trim().toString()
            val firstName = binding.etEditName.text?.trim().toString()
            if (checkValidation(userName,firstName,lastName)){
                viewModel.updateProfile(prefUtils.getUserId(), userName, firstName, lastName, stringImage1 ?: "null")
            }
        }

        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){ result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val path = data?.data
                userImage = handleSamplingAndRotationBitmap(requireContext(), path!!)
                val bitmap1 = scaleDown(userImage!!, 700f, true )
                binding.ivEditProfilePhoto.setImageBitmap(bitmap1)
                stringImage1 = Converters().fromBitmap(bitmap1)
            }
        }
    }

    override fun observe() {
        lifecycleScope.launchWhenResumed {
            viewModel.onJobList.collect {
                when (it) {
                    ApiState.Empty -> {}
                    is ApiState.Failure -> {
                        Toast.makeText(requireContext(), it.errorMessage, Toast.LENGTH_SHORT).show()
                        binding.progressEditProfileFragment.visibility = View.GONE
                    }
                    ApiState.Loading -> {
                        binding.progressEditProfileFragment.visibility = View.VISIBLE
                    }
                    is ApiState.Success -> {
                        binding.progressEditProfileFragment.visibility = View.GONE
                    }
                    is ApiState.SuccessMessage -> {
                        Toast.makeText(requireContext(), it.successMessage, Toast.LENGTH_SHORT).show()
                        binding.progressEditProfileFragment.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun selectImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        resultLauncher.launch(intent)
        //startActivityForResult(intent, IMG_RESULT)
    }
    private fun checkValidation(
        userName: String,
        firstName: String,
        lastName: String
    ): Boolean {
        if (userName.isEmpty() || firstName.isEmpty() || lastName.isEmpty() ) {
            Toast.makeText(requireContext(), getString(R.string.fill_fields), Toast.LENGTH_SHORT).show()
            return false
        }
        if (checkTurkishCharacter(userName)) {
            binding.containerEditUserName.error = "Kullanıcı adında türkçe karakter(ı,ş,ç,ö,ü) ve boşluk olamaz "
            return false
        }
        if (userName.length < 5) {
            textInputSettings(binding.containerEditUserName, getString(R.string.valid_user_name))
            return false
        } else {
            binding.containerEditUserName.isErrorEnabled = false
        }
        if (firstName.length < 3) {
            textInputSettings(binding.containerEditName, getString(R.string.valid_first_name))
            return false
        } else {
            binding.containerEditName.isErrorEnabled = false
        }
        if (lastName.length < 2) {
            textInputSettings(binding.containerEditLastName, getString(R.string.valid_last_name))
            return false
        } else {
            binding.containerEditLastName.isErrorEnabled = false
            return true
        }
    }
}