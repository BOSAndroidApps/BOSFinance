package com.example.theemiclub.ui.slideshow.activity

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bos.payment.appName.network.ApiInterface
import com.bos.payment.appName.network.RetrofitClient
import com.example.theemiclub.R
import com.example.theemiclub.databinding.ActivityLoginPageBinding
import com.example.theemiclub.ui.slideshow.constant.ConstantClass
import com.example.theemiclub.ui.slideshow.constant.ConstantClass.Retailer
import com.example.theemiclub.ui.slideshow.constant.ConstantClass.isInternetAvailable
import com.example.theemiclub.ui.slideshow.constant.ConstantClass.loginType
import com.example.theemiclub.ui.slideshow.data.model.loginsignup.LoginReq
import com.example.theemiclub.ui.slideshow.data.model.loginsignup.verification.SendOtpReq
import com.example.theemiclub.ui.slideshow.data.model.loginsignup.VerifyOTPReq
import com.example.theemiclub.ui.slideshow.data.repository.AuthRepository
import com.example.theemiclub.ui.slideshow.data.viewModelFactory.CommonViewModelFactory
import com.example.theemiclub.ui.slideshow.localdb.SharedPreference
import com.example.theemiclub.ui.slideshow.ui.view.activity.retailer.ForgetPasswordPage
import com.example.theemiclub.ui.slideshow.ui.view.activity.retailer.SignupPage
import com.example.theemiclub.ui.slideshow.ui.viewmodel.AuthenticationViewModel
import com.example.theemiclub.ui.slideshow.utils.ApiStatus
import com.google.gson.Gson
import kotlinx.coroutines.launch

class LoginPage : AppCompatActivity() {
    lateinit var binding: ActivityLoginPageBinding
    private lateinit var viewModel: AuthenticationViewModel
    lateinit var preference : SharedPreference
    lateinit var dialog : Dialog
    lateinit var countDownTimer: CountDownTimer
    lateinit var api : ApiInterface


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginPageBinding.inflate(layoutInflater)
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

        viewModel = ViewModelProvider(this, CommonViewModelFactory(AuthRepository(RetrofitClient.apiInterface)))[AuthenticationViewModel::class.java]
        preference = SharedPreference(this)
        api =  RetrofitClient.apiInterfaceSMS
        setLayoutForLogin()
        setonclicklistner()

    }

    fun setLayoutForLogin(){
        if(loginType.equals(Retailer)){
            binding.retailerLogin.visibility= View.VISIBLE
            binding.customerLogin.visibility=View.GONE
        }
        else{
            binding.retailerLogin.visibility= View.GONE
            binding.customerLogin.visibility=View.VISIBLE
        }
    }


    fun setonclicklistner(){
        binding.signupText.setOnClickListener{
            val mainIntent = Intent(this@LoginPage, SignupPage::class.java)
            startActivity(mainIntent)
        }

        binding.loginlayout.setOnClickListener {
            var emailOfMobile = binding.emailormobilenumber.text.toString().trim()
            var password = binding.password.text.toString().trim()

           if(validateLoginInput(emailOfMobile,password,this)){
               if(isInternetAvailable(this@LoginPage)) {
                   hitApiForLogin(emailOfMobile,password)
               }
               else{
                   Toast.makeText(this,"Please check your internet connection!!",Toast.LENGTH_SHORT).show()
               }
           }


        }

        binding.forgetpageLayout.setOnClickListener {
            val mainIntent = Intent(this@LoginPage, ForgetPasswordPage::class.java)
            startActivity(mainIntent)
        }

        binding.back.setOnClickListener {
            finish()
        }

        binding.customerloginlayout.setOnClickListener {
            var mobnumber = binding.mobilenumber.text.toString()
            if (ConstantClass.validateLoginInput(mobnumber, this) ) {
                if (isInternetAvailable(this@LoginPage)) {
                    hitApiForSendOTP(mobnumber,"Mobile")
                } else {
                    Toast.makeText(this, "Please check your internet connection!!", Toast.LENGTH_SHORT).show()
                }
            }else{

            }

        }



    }

    fun hitApiForLogin(emailOfMobile:String,password:String){
        var loginRequest = LoginReq(
            mobileormailid = emailOfMobile,
            password = password,
            logintype = loginType
        )
        Log.d("LoginReq", Gson().toJson(loginRequest))

        viewModel.getLogin(loginRequest).observe(this){
                resources->resources.let {
            when(it.apiStatus){
                ApiStatus.SUCCESS ->{
                    it.data?.let { users ->
                        users.body()?.let { response ->
                            Log.d("LoginResponse", Gson().toJson(response))

                            ConstantClass.dialog.dismiss()

                            if(response.statuss){
                                preference.setStringValue(ConstantClass.CustomerCode, response.customerCode.toString())
                                preference.setStringValue(ConstantClass.RetailerCode, response.retailerCode.toString())
                                preference.setStringValue(ConstantClass.FirstName, response.firstName.toString())
                                preference.setStringValue(ConstantClass.LastName, response.lastName.toString())
                                preference.setStringValue(ConstantClass.CustomerMobileNumber, response.mobileno.toString())
                                preference.setStringValue(ConstantClass.CustomerEmailID, response.emailID.toString())
                                preference.setBooleanValue(ConstantClass.LoggedIn,true)
                                preference.setStringValue(ConstantClass.LoginType,loginType)

                                val intent = Intent(this@LoginPage, DashBoard::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(intent)
                                finish()

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


    fun validateLoginInput(mobileOrEmailID: String, password: String, context: Context): Boolean {
        val isMobile = mobileOrEmailID.all { it.isDigit() } && mobileOrEmailID.length == 10
        val isEmail = Patterns.EMAIL_ADDRESS.matcher(mobileOrEmailID).matches()

        if (!isMobile && !isEmail) {
            Toast.makeText(context, "Enter a valid 10-digit mobile number or valid email address", Toast.LENGTH_SHORT).show()
            return false
        }

        if (password.isBlank() || password.length < 6) {
            Toast.makeText(context, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
            return false
        }

        return true // Input is valid
    }


    // for customer login flow
    fun OpenPopUpForVeryfyOTP(MobileNumber: String, otp:String){
        dialog = Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.verifyforgetpasswordotplayour)

        dialog.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)

            statusBarColor = Color.TRANSPARENT
            navigationBarColor = Color.TRANSPARENT
        }

        dialog.setCanceledOnTouchOutside(false)

        // Access views inside the custom layout
        val OTP1 = dialog.findViewById<EditText>(R.id.otp1)
        val OTP2 = dialog.findViewById<EditText>(R.id.otp2)
        val OTP3 = dialog.findViewById<EditText>(R.id.otp3)
        val OTP4 = dialog.findViewById<EditText>(R.id.otp4)

        val verifyButton = dialog.findViewById<LinearLayout>(R.id.verifylayout)
        val cancel = dialog.findViewById<ImageView>(R.id.cancel)
        val resendlayout = dialog.findViewById<RelativeLayout>(R.id.resendlayout)
        val resendtxt = dialog.findViewById<TextView>(R.id.resendtxt)
        val timer = dialog.findViewById<TextView>(R.id.timer)
        val subtitle = dialog.findViewById<TextView>(R.id.text_subtitle)

        subtitle.text = "Enter four digit OTP send on your registered mobile number"

        startOtpTimer(resendtxt,timer)

        OTP1.requestFocus()
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(OTP1, InputMethodManager.SHOW_IMPLICIT)

        setupOTPFocus(OTP1, OTP2, null)
        setupOTPFocus(OTP2, OTP3, OTP1)
        setupOTPFocus(OTP3, OTP4, OTP2)
        setupOTPFocus(OTP4, null, OTP3)

        cancel.setOnClickListener {
            dialog.dismiss()
        }

        resendlayout.setOnClickListener {
                  if(isInternetAvailable(this@LoginPage)) {
                      hitApiForReSendOTP(MobileNumber,"Mobile")
                      startOtpTimer(resendtxt,timer)
                  }
                  else{
                      Toast.makeText(this,"Please check your internet connection!!", Toast.LENGTH_SHORT).show()
                  }


        }

        verifyButton.setOnClickListener {
            val enteredOTP = OTP1.text.toString() + OTP2.text.toString() + OTP3.text.toString() + OTP4.text.toString()
            if (enteredOTP.length == 4) {
                 hitApiForOTPVerify(MobileNumber, enteredOTP,"Mobile verify")
                 var customerMobile = binding.mobilenumber.text.toString().trim()

                // Toast.makeText(this, "Thanks for your input! The next flow is under development and will be available soon.", Toast.LENGTH_SHORT).show()

            } else {
                Toast.makeText(this, "Please enter complete OTP", Toast.LENGTH_SHORT).show()
            }

        }

        dialog.show()

    }

    fun startOtpTimer( resendtxt:TextView, timer:TextView) {
        resendtxt.visibility = View.INVISIBLE
        timer.visibility = View.VISIBLE

        countDownTimer = object : CountDownTimer(5 * 60 * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes = (millisUntilFinished / 1000) / 60
                val seconds = (millisUntilFinished / 1000) % 60
                timer.text = String.format("%02d:%02d", minutes, seconds)
            }

            override fun onFinish() {
                timer.text = "00:00"
                timer.visibility = View.INVISIBLE
                resendtxt.visibility = View.VISIBLE
            }
        }
        countDownTimer.start()
    }


    private fun setupOTPFocus(current: EditText, next: EditText?, previous: EditText? = null) {
        current.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.length == 1) {
                    next?.requestFocus()
                }
            }
            override fun afterTextChanged(s: Editable?) {}
        })


        current.setOnKeyListener { v, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL && current.text.isEmpty()) {
                previous?.requestFocus()
                previous?.setSelection(previous.text.length) // move cursor to end
                return@setOnKeyListener true
            }
            false
        }

    }


    fun hitApiForSendOTP(mailidormobile: String,type : String) {
        var sendOtpReq = SendOtpReq(
            mobileoremailId = mailidormobile,
            otpType = type)
        Log.d("SendOTPREQ", Gson().toJson(sendOtpReq))

        viewModel.sendOTPReq(sendOtpReq).observe(this) { resources ->
            resources.let {
                when (it.apiStatus) {
                    ApiStatus.SUCCESS -> {
                        it.data?.let { users ->
                            users.body()?.let { response ->
                                Log.d("SendRes", response.message)
                                var otp = response.value
                                Log.d("OTP", otp)

                                if (response.statuss.equals("True")) {
                                    var firstName = "Customer"
                                    var customerName = firstName
                                    hitApiForMobVerify(mailidormobile,customerName,otp)
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

    fun hitApiForMobVerify(mobnumber:String,customerName:String,OTP:String){
        // hint: Dear  Naim Khan, Your OTP for Verification is 1234. Please Do Not Share the OTP With Anyone. Thanks For Using BOSOQ BOS CENTER
        var message = "Dear $customerName, Your OTP for Verification is $OTP. Please Do Not Share the OTP With Anyone. Thanks For Using BOSOQ BOS CENTER"

        lifecycleScope.launch{
            try {
                val response =api.sendSMSForVerifyMob(
                    apikey = ConstantClass.SMS_API_KEY,
                    senderid = ConstantClass.SMS_SENDER_ID,
                    templateid = ConstantClass.SMS_TEMPLATE_ID,
                    mobnumber = mobnumber,
                    message = message,
                )

                if (response!!.isSuccessful) {
                    Toast.makeText(this@LoginPage,"Otp sent on your mobile number!!" , Toast.LENGTH_SHORT).show()
                    val loanData = response.body()

                    if(ConstantClass.dialog!=null && ConstantClass.dialog.isShowing){
                        ConstantClass.dialog.dismiss()
                        OpenPopUpForVeryfyOTP(mobnumber,OTP)
                    }

                    Log.d("API_SUCCESS", loanData.toString())
                }
                else {
                    Log.e("API_ERROR", response.errorBody()?.string() ?: "Unknown error")
                }

            } catch (e: Exception) {
                Log.e("API_EXCEPTION", e.toString())
            }
        }
    }

    fun hitApiForOTPVerify(mobileOrEmailID: String, otp: String,message: String) {
        var verifyotpreq = VerifyOTPReq(
            mobileormailid = mobileOrEmailID,
            otp = otp,
            logintype = message
        )
        Log.d("VerifyOTPReq", Gson().toJson(verifyotpreq))
        viewModel.verifyOTPReq(verifyotpreq).observe(this) { resources ->
            resources.let {
                when (it.apiStatus) {
                    ApiStatus.SUCCESS -> {
                        it.data?.let { users ->
                            users.body()?.let { response ->
                                ConstantClass.dialog.dismiss()
                                Log.d("VerifyOTPRes", response.message)
                                if (response.statuss.equals("True")) {

                                    hitApiForLogin(mobileOrEmailID,"")

                                    if (dialog != null && dialog.isShowing) {
                                        dialog.dismiss()
                                    }
                                }

                                Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
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

    fun hitApiForReSendOTP(mailidormobile: String,type : String) {
        var sendOtpReq = SendOtpReq(
            mobileoremailId = mailidormobile,
            otpType = type
        )
        Log.d("SendOTPREQ", Gson().toJson(sendOtpReq))
        viewModel.sendOTPReq(sendOtpReq).observe(this) { resources ->
            resources.let {
                when (it.apiStatus) {
                    ApiStatus.SUCCESS -> {
                        it.data?.let { users ->
                            users.body()?.let { response ->
                                ConstantClass.dialog.dismiss()
                                Log.d("SendRes", response.message)
                                        var otp = response.value
                                        var customerName = "Customer"
                                        hitApiForResendMobVerify(mailidormobile,customerName,otp)
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

fun hitApiForResendMobVerify(mobnumber:String,customerName:String,OTP:String){
    // hint: Dear  Naim Khan, Your OTP for Verification is 1234. Please Do Not Share the OTP With Anyone. Thanks For Using BOSOQ BOS CENTER
    var message = "Dear $customerName, Your OTP for Verification is $OTP. Please Do Not Share the OTP With Anyone. Thanks For Using BOSOQ BOS CENTER "

    lifecycleScope.launch{
        try {
            val response =api.sendSMSForVerifyMob(
                apikey = ConstantClass.SMS_API_KEY,
                senderid = ConstantClass.SMS_SENDER_ID,
                templateid = ConstantClass.SMS_TEMPLATE_ID,
                mobnumber = mobnumber,
                message = message)

            if (response!!.isSuccessful) {
                Toast.makeText(this@LoginPage,"Otp sent on your mobile number!!" , Toast.LENGTH_SHORT).show()
                val loanData = response.body()

                if(ConstantClass.dialog!=null && ConstantClass.dialog.isShowing){
                    ConstantClass.dialog.dismiss()
                }
                Log.d("API_SUCCESS", loanData.toString())
            }
            else {
                Log.e("API_ERROR", response.errorBody()?.string() ?: "Unknown error")
            }
        }
        catch (e: Exception) {
            Log.e("API_EXCEPTION", e.toString())
        }
    }

}



}