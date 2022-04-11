package com.falApp.sadekahvefal.ui.activity

import android.content.res.ColorStateList
import android.graphics.Color
import android.util.Patterns
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.falApp.sadekahvefal.R
import com.falApp.sadekahvefal.base.BaseActivity
import com.falApp.sadekahvefal.databinding.ActivityRegisterBinding
import com.falApp.sadekahvefal.model.HomeRecyclerViewItem
import com.falApp.sadekahvefal.ui.UsersViewModel
import com.falApp.sadekahvefal.utils.ApiState
import com.falApp.sadekahvefal.utils.Constant
import com.falApp.sadekahvefal.utils.NavigateFragmentParams
import com.falApp.sadekahvefal.utils.PrefUtils
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class RegisterActivity : BaseActivity<ActivityRegisterBinding, UsersViewModel>(){
    @Inject
    lateinit var prefUtils: PrefUtils

    override val viewModel: UsersViewModel by viewModels()
    override fun getViewBinding() = ActivityRegisterBinding.inflate(layoutInflater)

    override fun onActivityCreated() {
        binding.btnRegister.setOnClickListener {
            registerUser()
        }
        binding.tvLogin.setOnClickListener { finish() }
    }

    override fun observe() {
        lifecycleScope.launchWhenResumed {
            viewModel.user.collect {
                when (it) {
                    ApiState.Empty -> { }
                    is ApiState.Failure -> {
                        if (it.errorMessage.isNullOrEmpty()) {
                            Toast.makeText(this@RegisterActivity, "Bir sorun oluştu", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this@RegisterActivity, it.errorMessage, Toast.LENGTH_SHORT).show()
                        }
                    }
                    ApiState.Loading -> {
                        viewModel.loadingDetection.postValue(true)
                    }
                    is ApiState.Success -> {
                        prefUtils.save(Constant.USERNAME, it.data.login!!.user_name)
                        prefUtils.save(Constant.USERID, it.data.login.user_id!!)
                        //prefUtils.save(Constant.USERGOLD, it.data.login.gold!!)
                        Toast.makeText(this@RegisterActivity, it.data.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun registerUser() {
        val username = binding.etUserName.text.toString().trim()
        val name = binding.etName.text.toString().trim()
        val lastName = binding.etLastName.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val phone = binding.etPhone.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()
        val passwordTry = binding.etPasswordTry.text.toString().trim()
        if (checkValidation(username, name, lastName, email, phone, password, passwordTry)) {
            viewModel.registerAndGetUser(
                HomeRecyclerViewItem.User(user_name = username, first_name = name, last_name = lastName, email = email, phone = phone, password = password)
            )
        }
    }

    private fun checkValidation(
        userName: String,
        firstName: String,
        lastName: String,
        email: String,
        phone: String,
        password: String,
        password2: String
    ): Boolean {
        if (userName.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty() || password2.isEmpty()) {
            Toast.makeText(this, getString(R.string.fill_fields), Toast.LENGTH_SHORT).show()
            return false
        }
        if (checkTurkishCharacter(userName)) {
            binding.containerUserName.error = "Kullanıcı adında türkçe karakter(ı,ş,ç,ö,ü) ve boşluk olamaz "
            return false
        }
        if (userName.length < 5) {
            textInputSettings(binding.containerUserName, getString(R.string.valid_user_name))
            return false
        } else {
            binding.containerUserName.isErrorEnabled = false
        }

        if (firstName.length < 3) {
            textInputSettings(binding.containerName, getString(R.string.valid_first_name))
            return false
        } else {
            binding.containerName.isErrorEnabled = false
        }
        if (lastName.length < 2) {
            textInputSettings(binding.containerLastName, getString(R.string.valid_last_name))
            return false
        } else {
            binding.containerLastName.isErrorEnabled = false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            textInputSettings(binding.containerEmail, getString(R.string.valid_mail))
            return false
        } else {
            binding.containerEmail.isErrorEnabled = false
        }
        if (phone.length < 10) {
            textInputSettings(binding.containerPhone, getString(R.string.valid_phone))
            return false
        } else {
            binding.containerPhone.isErrorEnabled = false
        }
        if (password.length < 9) {
            textInputSettings(binding.containerPassword, getString(R.string.valid_password))
            return false
        } else {
            binding.containerPassword.isErrorEnabled = false
        }
        if (password != password2) {
            textInputSettings(binding.containerPassword, getString(R.string.match_password))
            return false
        } else {
            binding.containerPassword.isErrorEnabled = false
            return true
        }
    }

    companion object {
        fun checkTurkishCharacter(value: String): Boolean {
            return (value.contains(" ") || value.contains("ş") || value.contains("Ş") || value.contains("Ç")
                    || value.contains("ç") || value.contains("Ğ") || value.contains("ğ") || value.contains("ş")
                    || value.contains("ö") || value.contains("Ö") || value.contains("ü") || value.contains("Ü")
                    || value.contains("ı") || value.contains("İ"))
        }
        fun textInputSettings(txtInput: TextInputLayout, validationMessage: String) {
            txtInput.error = validationMessage
            txtInput.setErrorTextColor(ColorStateList.valueOf(Color.RED))
            txtInput.requestFocus()
        }
    }

    override fun navigateFragment(params: NavigateFragmentParams) {
        TODO("Not yet implemented")
    }

    override fun showHideProgress(isShow: Boolean) {
        TODO("Not yet implemented")
    }

}