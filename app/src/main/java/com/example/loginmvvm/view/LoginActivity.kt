package com.example.loginmvvm.view

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.text.*
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.loginmvvm.R
import com.example.loginmvvm.databinding.ActivityLoginBinding
import com.example.loginmvvm.factory.LoginViewModelFactory
import com.example.loginmvvm.model.UserRequest
import com.example.loginmvvm.repository.LoginRepository
import com.example.loginmvvm.service.APIService
import com.example.loginmvvm.util.Constant
import com.example.loginmvvm.viewmodel.LoginViewModel

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel
    private var checkBoxOk = false
    private var emailOk = false
    private var passwordOk = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingView()
    }

    private fun bindingView() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.lifecycleOwner = this
        val apiService = APIService.service
        val repository = LoginRepository(apiService)
        val viewModelFactory = LoginViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory)[LoginViewModel::class.java]
        binding.viewModel = viewModel
        paddingCheckbox()
        action()
        callback()
        highlightText()
    }

    private fun callback() {
        viewModel.loginResult.observe(this, Observer {
            if (it != null) {
                val pref = getSharedPreferences("PREF", MODE_PRIVATE)
                pref.edit().putString(Constant.KEY_TOKEN_LOGIN, it.accessToken).apply()
                startActivity(Intent(this, CategoriesActivity::class.java))
            }
        })

        viewModel.loginError.observe(this){
            Toast.makeText(this,it,Toast.LENGTH_SHORT).show()
        }

        viewModel.sigUpError.observe(this){
            Toast.makeText(this,it,Toast.LENGTH_SHORT).show()
        }
    }

    private fun paddingCheckbox() {
        var scale = this.resources.displayMetrics.density;
        binding.actLoginCbxOverYearsOfAge.setPadding(
            (binding.actLoginCbxOverYearsOfAge.paddingLeft + (10.0f * scale + 0.5f)).toInt(),
            binding.actLoginCbxOverYearsOfAge.paddingTop,
            binding.actLoginCbxOverYearsOfAge.paddingRight,
            binding.actLoginCbxOverYearsOfAge.paddingBottom
        )
    }

    private fun action(){
        binding.actLoginBtnLogin.setOnClickListener {
            if (enableButtonLogin()){
                val email = binding.actLoginEdtEmail.text.toString()
                val password = binding.actLoginEdtPassword.text.toString()
                viewModel.signup(UserRequest(email, password,"test","test"))
            }else{
                Toast.makeText(this,"Vui lòng nhập thông tin đăng nhập đúng quy định",Toast.LENGTH_SHORT).show()
            }
        }

        binding.actLoginEdtEmail.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                emailOk = s.trim().toString().isNotEmpty()
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })

        binding.actLoginEdtPassword.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus){
                binding.actLoginViewPassword.visibility = View.GONE
            }else{
                if (binding.actLoginEdtPassword.text?.trim().toString().isEmpty()){
                    binding.actLoginViewPassword.visibility = View.VISIBLE
                }
            }
        }

        binding.actLoginEdtPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                var hasLowerCase =false
                var hasUpperCase =false
                var hasDigit =false
                var hasSpecialChar =false
                if (s.trim().isNotEmpty()) {

                    //check for lowercase letters
                    hasLowerCase = s.any { it.isLowerCase() }

                    //check for uppercase letters
                    hasUpperCase = s.any { it.isUpperCase() }

                    //check for digit
                    hasDigit = s.any { it.isDigit() }

                    //check for special characters
                    hasSpecialChar =
                        s.contains(Regex("[!#\$%&(){|}~:;<=>?@*+,./^_`\\'\\\" \\t\\r\\n\\f-]+"))

                }
                calculateStrength(s, hasLowerCase, hasUpperCase, hasDigit, hasSpecialChar)
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })

        binding.actLoginCbxOverYearsOfAge.setOnCheckedChangeListener { buttonView, isChecked ->
            checkBoxOk = isChecked
        }
    }

    private fun highlightText(){
        val replaceWith = "<span style='text color:#576fe0'>" + "Terms of Service"+"</span>"+ "<span style='text color:#6f6f6f'>" + " and "+"</span>" +"<span style='text color:#576fe0'>" + "Privacy Policy"+"</span>"
        val originalText:String = binding.actLoginTvTitleByClick.text.toString()
        val modifiedText = originalText.replace("Terms of Service and Privacy Policy",replaceWith)
        binding.actLoginTvTitleByClick.text = Html.fromHtml(modifiedText)
    }

    private fun enableButtonLogin():Boolean{
        return checkBoxOk && emailOk && passwordOk
    }

    private fun calculateStrength(
        charSequence: CharSequence,
        hasLowerCase: Boolean,
        hasUpperCase: Boolean,
        hasDigit: Boolean,
        hasSpecialChar: Boolean
    ) {

        if (charSequence.toString().trim().length in 7..17) {
            updateStatusUI(
                R.string.tv_week,
                ContextCompat.getColor(this, R.color.Week),
                ColorStateList.valueOf(Color.RED),
                25
            )
            if (hasLowerCase && hasUpperCase && !hasDigit && !hasSpecialChar) {
                updateStatusUI(
                    R.string.tv_fair,
                    ContextCompat.getColor(this, R.color.Fair),
                    ColorStateList.valueOf(Color.YELLOW),
                    50
                )
            } else if (hasLowerCase && hasUpperCase && hasDigit && !hasSpecialChar || hasLowerCase && hasUpperCase && !hasDigit && hasSpecialChar) {
                updateStatusUI(
                    R.string.tv_good,
                    ContextCompat.getColor(this, R.color.Good),
                    ColorStateList.valueOf(Color.BLUE),
                    75
                )
            } else if (hasLowerCase && hasUpperCase && hasDigit && hasSpecialChar) {
                updateStatusUI(
                    R.string.tv_strong,
                    ContextCompat.getColor(this, R.color.Strong),
                    ColorStateList.valueOf(Color.GREEN),
                    100
                )
            }
            passwordOk = true
        } else if (charSequence.trim().toString().isEmpty()) {
            updateStatusUI(
                R.string.tv_too_short,
                ContextCompat.getColor(this, R.color.grey),
                ColorStateList.valueOf(Color.GRAY),
                0
            )
            passwordOk = false
        } else {
            updateStatusUI(
                R.string.tv_week,
                ContextCompat.getColor(this, R.color.Week),
                ColorStateList.valueOf(Color.RED),
                25
            )
            passwordOk = false
        }
    }

    private fun updateStatusUI(tvFair: Int, color: Int, valueOf: ColorStateList, i: Int) {
        binding.actLoginTvStrength.text = resources.getString(tvFair)
        binding.actLoginTvStrength.setTextColor(color)
        binding.actLoginPbPassword.progressTintList = valueOf
        binding.actLoginPbPassword.progress = i
    }
}