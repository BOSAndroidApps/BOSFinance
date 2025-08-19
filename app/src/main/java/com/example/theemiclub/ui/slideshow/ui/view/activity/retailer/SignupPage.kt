package com.example.theemiclub.ui.slideshow.ui.view.activity.retailer

import android.annotation.TargetApi
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.InputFilter
import android.text.InputType
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.Window
import android.view.inputmethod.EditorInfo
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.ViewModelProvider
import com.bos.payment.appName.network.RetrofitClient
import com.example.theemiclub.R
import com.example.theemiclub.databinding.ActivitySignupPageBinding
import com.example.theemiclub.ui.slideshow.activity.LoginPage
import com.example.theemiclub.ui.slideshow.constant.ConstantClass
import com.example.theemiclub.ui.slideshow.constant.ConstantClass.disableCopyPaste
import com.example.theemiclub.ui.slideshow.constant.ConstantClass.iisAggrementVerified
import com.example.theemiclub.ui.slideshow.constant.ConstantClass.isInternetAvailable
import com.example.theemiclub.ui.slideshow.data.model.loginsignup.RegistrationReq
import com.example.theemiclub.ui.slideshow.data.repository.AuthRepository
import com.example.theemiclub.ui.slideshow.data.viewModelFactory.CommonViewModelFactory
import com.example.theemiclub.ui.slideshow.localdb.SharedPreference
import com.example.theemiclub.ui.slideshow.ui.viewmodel.AuthenticationViewModel
import com.example.theemiclub.ui.slideshow.utils.ApiStatus
import com.google.gson.Gson

class SignupPage : AppCompatActivity() {
    lateinit var binding : ActivitySignupPageBinding
    lateinit var viewModel: AuthenticationViewModel
    lateinit var preference : SharedPreference
    lateinit var dialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

         binding = ActivitySignupPageBinding.inflate(layoutInflater)
         setContentView(binding.root)

         ViewCompat.setOnApplyWindowInsetsListener(binding.main) { view, insets ->
            val systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            view.setPadding(
                systemBarsInsets.left,
                systemBarsInsets.top,
                systemBarsInsets.right,
                systemBarsInsets.bottom
            )

            WindowInsetsCompat.CONSUMED
        }

         setclickListner()

        viewModel = ViewModelProvider(this, CommonViewModelFactory(AuthRepository(RetrofitClient.apiInterface)))[AuthenticationViewModel::class.java]
        preference = SharedPreference(this)
        DisableCopyPaste()
    }

    fun setclickListner(){

        binding.loginText.setOnClickListener {
            finish()
        }

        binding.createaccount.setOnClickListener {
            var firstName = binding.firstName.text.toString().trim()
            var lastName = binding.lastName.text.toString().trim()
            var mobilenumber = binding.mobileNumber.text.toString().trim()
            var emailId = binding.emailId.text.toString().trim()
            var password = binding.password.text.toString().trim()
            var confirmpass = binding.confirmpassword.text.toString().trim()
            var pannumber = binding.panEditText.text.toString().trim()
            var aadharnumber = binding.aadharnumber.text.toString().trim()
            var address = binding.address.text.toString().trim()

            if(validateForm(aadharnumber,pannumber,mobilenumber,emailId,password,confirmpass,this@SignupPage)){
              if(isInternetAvailable(this@SignupPage))  {
                  hitApiForRegistration(firstName,lastName,mobilenumber,emailId,password,confirmpass,pannumber,aadharnumber,address)
              }
              else{
                  Toast.makeText(this,"Please check your internet connection!!",Toast.LENGTH_SHORT).show()
              }

            }

        }

        binding.acceptTermConditionCheck.setOnClickListener{
            OpenPopUpForTermCondition()
        }

    }


    fun DisableCopyPaste(){

         binding.firstName.disableCopyPaste()
         binding.lastName.disableCopyPaste()

         binding.aadharnumber.filters = arrayOf(InputFilter { source, start, end, dest, dstart, dend ->
            val newText = StringBuilder(dest).apply {
                replace(dstart, dend, source?.substring(start, end) ?: "")
            }.toString()

            // Block if non-digit characters are present
            if (source != null && source.any { !it.isDigit() }) {
                return@InputFilter ""
            }

            // If user pastes while field is empty, only allow exactly 12 digits
            if (dest.isEmpty() && source != null && source.length > 1) {
                return@InputFilter if (source.length == 12 && source.all { it.isDigit() }) source else ""
            }

            // Prevent length > 12
            if (newText.length > 12) {
                return@InputFilter ""
            }

            null // Accept input
        })

         binding.mobileNumber.filters= arrayOf(InputFilter{
                 source, start, end, dest, dstart, dend ->
             val newText = StringBuilder(dest).apply {
                 replace(dstart, dend, source?.substring(start, end) ?: "")
             }.toString()

             // Allow only digits
             if (source != null && source.any { !it.isDigit() }) {
                 return@InputFilter ""
             }

             // If user pastes while field is empty, only allow exactly 10 digits
             if (dest.isEmpty() && source != null && source.length > 1) {
                 return@InputFilter if (source.length == 10 && source.all { it.isDigit() }) source else ""
             }

             // Prevent length > 10
             if (newText.length > 10) {
                 return@InputFilter ""
             }

             null // Accept input
         })

        val emailAllowedChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789@._-"

        binding.emailId.filters = arrayOf(InputFilter { source, start, end, dest, dstart, dend ->
            val newText = StringBuilder(dest).apply {
                replace(dstart, dend, source?.substring(start, end) ?: "")
            }.toString()

            // Block invalid characters
            if (source != null && source.any { it !in emailAllowedChars }) {
                return@InputFilter ""
            }

            // Paste handling when field is empty
            if (dest.isEmpty() && source != null && source.length > 1) {
                val pasted = source.toString()
                return@InputFilter if (android.util.Patterns.EMAIL_ADDRESS.matcher(pasted).matches()) pasted else ""
            }

            // Limit max length (optional)
            if (newText.length > 50) {
                return@InputFilter ""
            }

            null // Accept valid input
        })

        val panPattern = Regex("^[A-Z]{5}[0-9]{4}[A-Z]{1}$")

        binding.panEditText.filters = arrayOf(InputFilter { source, start, end, dest, dstart, dend ->
            val newText = StringBuilder(dest).apply {
                replace(dstart, dend, source?.substring(start, end) ?: "")
            }.toString().uppercase() // Always uppercase

            // Block invalid characters
            if (source != null && source.any { !(it.isDigit() || it.isLetter()) }) {
                return@InputFilter ""
            }

            // Force uppercase input
            if (source != null && source.any { it.isLowerCase() }) {
                return@InputFilter source.toString().uppercase()
            }

            // Limit to 10 chars
            if (newText.length > 10) {
                return@InputFilter ""
            }

            // Paste handling
            if (source != null && source.length > 1) {
                return@InputFilter if (panPattern.matches(newText)) newText else ""
            }

            null // Accept valid input
        })


        binding.address.filters = arrayOf(InputFilter { source, _, _, _, _, _ ->
            val allowedPattern = Regex("[A-Za-z0-9 ,./#\\-\\n]+") // \n = allow line breaks
            if (source.isNullOrEmpty()) {
                null // allow deletes
            } else if (allowedPattern.matches(source)) {
                null // allow valid input
            } else {
                "" // block invalid chars (including from paste)
            }
        })


    }


    fun hitApiForRegistration( firstName:String,lastName:String,mobNumber:String,emailId:String,password:String,confrmPassword:String,panNumber:String,aadharNumber:String,address:String){
        var registationRequest = RegistrationReq(
            firstName = firstName,
            lastName = lastName,
            mobileNumber = mobNumber,
            emailId = emailId,
            password = password,
            confrmpassword = confrmPassword,
            address = address,
            aadharnumber = aadharNumber,
            panNumber = panNumber
        )
        Log.d("RegisReq", Gson().toJson(registationRequest))

        viewModel.getRegistration(registationRequest).observe(this){
            resources->resources.let {
                when(it.apiStatus){
                    ApiStatus.SUCCESS ->{
                        it.data?.let { users ->
                            users.body()?.let { response ->
                                ConstantClass.dialog.dismiss()
                                Log.d("AirPortListResponse", Gson().toJson(response))
                                if(response.statuss){
                                    preference.setStringValue(ConstantClass.CustomerCode, response.customerCode.toString())
                                    preference.setStringValue(ConstantClass.CustomerMobileNumber, response.mobileNumber.toString())
                                    preference.setStringValue(ConstantClass.CustomerEmailID, response.emailID.toString())
                                    Toast.makeText(this, response?.message, Toast.LENGTH_SHORT).show()
                                    startActivity(Intent(this, LoginPage::class.java))
                                    finish()
                                    emptyAboveField()
                                }
                                else{
                                    Toast.makeText(this, response?.message, Toast.LENGTH_SHORT).show()
                                }

                            }
                        }
                    }
                    ApiStatus.ERROR -> {
                        ConstantClass.dialog.dismiss()
                    }

                    ApiStatus.LOADING -> {
                        ConstantClass.OpenPopUpForVeryfyOTP(this)
                    }

                }
         }
        }


    }


    fun validateForm(
        aadharNumber: String,
        panNumber: String,
        mobile: String,
        email: String,
        password: String,
        confirmPassword: String,
        context: Context): Boolean {

        if (mobile.isBlank() || mobile.length != 10 || !mobile.all { it.isDigit() }) {
            Toast.makeText(context, "Enter a valid 10-digit mobile number", Toast.LENGTH_SHORT).show()
            return false
        }

        if (email.isBlank() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(context, "Enter a valid email address", Toast.LENGTH_SHORT).show()
            return false
        }


        // Aadhaar validation
        if (aadharNumber.isBlank() || aadharNumber.length != 12 || !aadharNumber.all { it.isDigit() }) {
            Toast.makeText(context, "Enter a valid 12-digit Aadhaar number", Toast.LENGTH_SHORT).show()
            return false
        }

        // PAN validation (Regex: 5 letters, 4 digits, 1 letter)
        val panRegex = Regex("[A-Z]{5}[0-9]{4}[A-Z]{1}")
        if (panNumber.isBlank() || !panRegex.matches(panNumber.uppercase())) {
            Toast.makeText(context, "Enter a valid PAN number (e.g., ABCDE1234F)", Toast.LENGTH_SHORT).show()
            return false
        }


        if (password.length < 6) {
            Toast.makeText(context, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
            return false
        }

        if (confirmPassword != password) {
            Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
            return false
        }

        return true // All fields are valid
    }

    fun emptyAboveField(){
        binding.firstName.text!!.clear()
        binding.lastName.text.clear()
        binding.mobileNumber.text!!.clear()
        binding.emailId.text.clear()
        binding.password.text.clear()
        binding.confirmpassword.text.clear()
        binding.address.text.clear()
        binding.panEditText.text.clear()
        binding.aadharnumber.text!!.clear()

    }


    fun OpenPopUpForTermCondition() {

        dialog = Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.term_condition_layout)

        dialog.setCanceledOnTouchOutside(false)


        val verifyButton = dialog.findViewById<LinearLayout>(R.id.btnAccept)


        verifyButton.setOnClickListener {
            iisAggrementVerified = true
            binding.acceptTermConditionCheck.isChecked = true
            dialog.dismiss()
        }

        dialog.setOnDismissListener {
            // Called when dialog is dismissed by back press or programmatically
            if (iisAggrementVerified) {

            } else {
                binding.acceptTermConditionCheck.isChecked = false
                iisAggrementVerified = false
            }


        }

        dialog.show()

    }


}