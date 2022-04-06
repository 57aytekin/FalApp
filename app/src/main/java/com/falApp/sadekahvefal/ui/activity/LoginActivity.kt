package com.falApp.sadekahvefal.ui.activity

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.Patterns
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.falApp.sadekahvefal.R
import com.falApp.sadekahvefal.base.BaseActivity
import com.falApp.sadekahvefal.databinding.ActivityLoginBinding
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
class LoginActivity : BaseActivity<ActivityLoginBinding, UsersViewModel>() {
    @Inject
    lateinit var prefUtils: PrefUtils

    override fun getViewBinding() = ActivityLoginBinding.inflate(layoutInflater)
    override val viewModel: UsersViewModel by viewModels()

    override fun onActivityCreated() {
        if (prefUtils.getUserName()?.isNotEmpty() == true){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        binding.tvRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
        binding.btnRegister.setOnClickListener {
            val email = binding.tvEmail.text.toString().trim()
            val password = binding.tvPassword.text.toString().trim()
            if (checkValidation(email, password)) {
                viewModel.loginUser(email, password)
            }
        }
    }
    override fun observe() {
        lifecycleScope.launchWhenResumed {
            viewModel.user.collect {
                when (it) {
                    ApiState.Empty -> { }
                    is ApiState.Failure -> {
                        if (it.errorMessage.isNullOrEmpty()) {
                            Toast.makeText(this@LoginActivity, "Bir sorun oluştu", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this@LoginActivity, it.errorMessage, Toast.LENGTH_SHORT).show()
                        }
                    }
                    ApiState.Loading -> {}
                    is ApiState.Success -> {
                        prefUtils.save(Constant.USERNAME, it.data.login!!.user_name)
                        prefUtils.save(Constant.USERID, it.data.login.user_id!!)
                        prefUtils.save(Constant.USERGOLD, it.data.login.gold!!)
                        prefUtils.save(Constant.IS_ADMIN, it.data.login.is_admin!!)
                        Toast.makeText(this@LoginActivity, it.data.message, Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()
                    }
                    is ApiState.SuccessMessage -> {
                        Toast.makeText(this@LoginActivity, it.successMessage, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun checkValidation(email: String, password: String) : Boolean {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            textInputSettings(binding.containerEmail, getString(R.string.valid_mail))
            return false
        }else {
            binding.containerEmail.isErrorEnabled = false
        }
        if (password.length < 9) {
            textInputSettings(binding.containerPassword, getString(R.string.valid_password))
            return false
        } else {
            binding.containerPassword.isErrorEnabled = false
        }
        return true
    }

    private fun textInputSettings(txtInput: TextInputLayout, validationMessage: String) {
        txtInput.error = validationMessage
        txtInput.setErrorTextColor(ColorStateList.valueOf(Color.RED))
        txtInput.requestFocus()
    }



    override fun navigateFragment(params: NavigateFragmentParams) {
    }

    override fun showHideProgress(isShow: Boolean) {
    }

}