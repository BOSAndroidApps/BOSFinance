package com.example.bosmobilefinance.ui.slideshow.ui.view.activity.retailer

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
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
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bos.payment.appName.network.ApiInterface
import com.bos.payment.appName.network.RetrofitClient
import com.example.bosmobilefinance.R
import com.example.bosmobilefinance.databinding.ActivityNewCustomerRegistrationPageBinding
import com.example.bosmobilefinance.ui.slideshow.constant.ConstantClass
import com.example.bosmobilefinance.ui.slideshow.constant.ConstantClass.AadharTransactionIdNo
import com.example.bosmobilefinance.ui.slideshow.constant.ConstantClass.CheckOnlineOrOffline
import com.example.bosmobilefinance.ui.slideshow.constant.ConstantClass.CustAlternateMobileNumber
import com.example.bosmobilefinance.ui.slideshow.constant.ConstantClass.CustAlternateMobileOTP
import com.example.bosmobilefinance.ui.slideshow.constant.ConstantClass.CustAlternateMobileVerified
import com.example.bosmobilefinance.ui.slideshow.constant.ConstantClass.CustAreaSector
import com.example.bosmobilefinance.ui.slideshow.constant.ConstantClass.CustCityName
import com.example.bosmobilefinance.ui.slideshow.constant.ConstantClass.CustCountry
import com.example.bosmobilefinance.ui.slideshow.constant.ConstantClass.CustCurrentAddress
import com.example.bosmobilefinance.ui.slideshow.constant.ConstantClass.CustFirstName
import com.example.bosmobilefinance.ui.slideshow.constant.ConstantClass.CustFlatNo
import com.example.bosmobilefinance.ui.slideshow.constant.ConstantClass.CustLastName
import com.example.bosmobilefinance.ui.slideshow.constant.ConstantClass.CustMiddleName
import com.example.bosmobilefinance.ui.slideshow.constant.ConstantClass.CustPhotoPath
import com.example.bosmobilefinance.ui.slideshow.constant.ConstantClass.CustPinCode
import com.example.bosmobilefinance.ui.slideshow.constant.ConstantClass.CustPrimaryMobileNumber
import com.example.bosmobilefinance.ui.slideshow.constant.ConstantClass.CustPrimaryMobileVerified
import com.example.bosmobilefinance.ui.slideshow.constant.ConstantClass.CustPrimaryOTP
import com.example.bosmobilefinance.ui.slideshow.constant.ConstantClass.CustStateName
import com.example.bosmobilefinance.ui.slideshow.constant.ConstantClass.CusteMailID
import com.example.bosmobilefinance.ui.slideshow.constant.ConstantClass.PanFrontImageUri
import com.example.bosmobilefinance.ui.slideshow.constant.ConstantClass.PanNumber
import com.example.bosmobilefinance.ui.slideshow.constant.ConstantClass.getCityStateFromPincode
import com.example.bosmobilefinance.ui.slideshow.constant.ConstantClass.iisAggrementVerified
import com.example.bosmobilefinance.ui.slideshow.constant.ConstantClass.isAggrementVerified
import com.example.bosmobilefinance.ui.slideshow.constant.ConstantClass.isInternetAvailable
import com.example.bosmobilefinance.ui.slideshow.constant.ConstantClass.isValidPinCode
import com.example.bosmobilefinance.ui.slideshow.constant.ConstantClass.uriToFile
import com.example.bosmobilefinance.ui.slideshow.constant.ConstantClass.validateLoginInput
import com.example.bosmobilefinance.ui.slideshow.data.model.loginsignup.verification.SendOtpReq
import com.example.bosmobilefinance.ui.slideshow.data.model.loginsignup.VerifyOTPReq
import com.example.bosmobilefinance.ui.slideshow.data.repository.AuthRepository
import com.example.bosmobilefinance.ui.slideshow.data.viewModelFactory.CommonViewModelFactory
import com.example.bosmobilefinance.ui.slideshow.localdb.SharedPreference
import com.example.bosmobilefinance.ui.slideshow.ui.viewmodel.AuthenticationViewModel
import com.example.bosmobilefinance.ui.slideshow.utils.ApiStatus
import com.google.gson.Gson
import kotlinx.coroutines.launch
import java.io.File


class NewCustomerRegistrationPage : AppCompatActivity() {
    lateinit var binding: ActivityNewCustomerRegistrationPageBinding
    lateinit var dialog: Dialog
    lateinit var api: ApiInterface
    lateinit var countDownTimer: CountDownTimer
    private val CAMERA_REQUEST_CODE_FRONT = 1001
    private var photoUri: Uri? = null
    var mobileveryfied: Boolean = false
    var alternatemobileveryfied: Boolean = false
    var emailIdveryfied: Boolean = false
    lateinit var viewModel: AuthenticationViewModel
    lateinit var preference: SharedPreference
    var clickmobile: Boolean = false
    var clickalternatemobile: Boolean = false
    var clickemailId: Boolean = false
    var EmailId: String = ""
    var customerImagePath: String? = ""

    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                // Handle the photoUri, e.g., show image in ImageView
                binding.userimage.visibility = View.VISIBLE
                binding.lockimage.visibility = View.GONE
                binding.userimage.setImageURI(photoUri)
                val imageFile = uriToFile(photoUri!!, this)
                customerImagePath = imageFile!!.absolutePath
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNewCustomerRegistrationPageBinding.inflate(layoutInflater)
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

        viewModel = ViewModelProvider(
            this,
            CommonViewModelFactory(AuthRepository(RetrofitClient.apiInterface))
        )[AuthenticationViewModel::class.java]
        api = RetrofitClient.apiInterfaceSMS
        preference = SharedPreference(this)
        setOnClickListner()
        setDataInUI()

    }

    fun setDataInUI() {

        binding.mobileNumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if (s!!.length == 10) {
                    binding.verifymobilenumber.visibility = View.VISIBLE
                } else {
                    binding.verifymobilenumber.visibility = View.GONE
                }
            }

        })

        binding.alternatemobileNumber.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if (s!!.length == 10) {
                    if (mobileveryfied) {
                        binding.verifymobilenumber.visibility = View.GONE
                    } else {
                        binding.verifymobilenumber.visibility = View.VISIBLE
                    }
                    binding.alternateverifymobilenumber.visibility = View.VISIBLE
                } else {
                    binding.alternateverifymobilenumber.visibility = View.GONE
                }
            }

        })


        binding.emailId.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if (s!!.length > 0) {
                    if (mobileveryfied) {
                        binding.verifymobilenumber.visibility = View.GONE
                    } else {
                        binding.verifymobilenumber.visibility = View.VISIBLE
                    }

                    if (alternatemobileveryfied) {
                        binding.alternateverifymobilenumber.visibility = View.GONE
                    } else {
                        binding.alternateverifymobilenumber.visibility = View.VISIBLE
                    }

                    binding.verifyEmailId.visibility = View.VISIBLE

                } else {
                    binding.verifyEmailId.visibility = View.GONE
                }
            }

        })


        binding.pincode.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if (isValidPinCode(s.toString())) {
                    getCityStateFromPincode(
                        this@NewCustomerRegistrationPage,
                        s.toString()
                    ) { city, state, country ->
                        if (city != null && state != null) {
                            binding.cityname.setText(city)
                            binding.statename.setText(state)
                            CustCountry = country
                        } else {
                            binding.cityname.text.clear()
                            binding.statename.text.clear()
                            Toast.makeText(
                                this@NewCustomerRegistrationPage,
                                "Invalid PIN or network error",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }


                }

            }

        })


    }


    fun setOnClickListner() {

        binding.acceptTermConditionCheck.setOnClickListener {
            /*if(!iisAggrementVerified){

            }else{
                binding.acceptTermConditionCheck.isEnabled=false
            }*/
            OpenPopUpForTermCondition()

        }

        binding.cameraicon.setOnClickListener {
            checkCameraPermissionAndOpenCamera()
        }

        binding.back.setOnClickListener {
            finish()
            onBackPressed()
        }

        binding.verifymobilenumber.setOnClickListener {
            clickemailId = false
            clickalternatemobile = false
            clickmobile = true
            var mobnumber = binding.mobileNumber.text.toString()

            if (validateLoginInput(mobnumber, this) && !binding.firstName.text.toString()
                    .isNullOrBlank() && !binding.lastName.text.toString().isNullOrBlank()
            ) {
                if (isInternetAvailable(this@NewCustomerRegistrationPage)) {
                    hitApiForSendOTP(mobnumber, "Mobile")
                } else {
                    Toast.makeText(
                        this,
                        "Please check your internet connection!!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                if (binding.firstName.text.toString()
                        .isNullOrBlank() || binding.lastName.text.toString().isNullOrBlank()
                ) {
                    Toast.makeText(
                        this,
                        "Please enter customer first or last name",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        }

        binding.alternateverifymobilenumber.setOnClickListener {
            clickemailId = false
            clickalternatemobile = true
            clickmobile = false
            var altmobnumber = binding.alternatemobileNumber.text.toString()
            if (validateLoginInput(altmobnumber, this) && !binding.firstName.text.toString()
                    .isNullOrBlank() && !binding.lastName.text.toString().isNullOrBlank()
            ) {
                if (isInternetAvailable(this@NewCustomerRegistrationPage)) {
                    hitApiForSendOTP(altmobnumber, "Alter Mobile")
                } else {
                    Toast.makeText(
                        this,
                        "Please check your internet connection!!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                if (binding.firstName.text.toString()
                        .isNullOrBlank() || binding.lastName.text.toString().isNullOrBlank()
                ) {
                    Toast.makeText(
                        this,
                        "Please enter customer first or last name",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        }

        binding.verifyEmailId.setOnClickListener {
            clickemailId = true
            clickalternatemobile = false
            clickmobile = false
            var mailId = binding.emailId.text.toString()
            EmailId = mailId
            if (validateLoginInput(mailId, this)) {
                if (isInternetAvailable(this@NewCustomerRegistrationPage)) {
                    hitApiForSendOTP(mailId, "Email")
                } else {
                    Toast.makeText(
                        this,
                        "Please check your internet connection!!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        binding.createaccount.setOnClickListener {
            val (isValid, errorMessage) = isValidForm(
                firstName = binding.firstName.text.toString().trim(),
                middleName = binding.middleName.text.toString().trim(),
                lastName = binding.lastName.text.toString().trim(),
                mobileNumber = binding.mobileNumber.text.toString().trim(),
                alternateMobile = binding.alternatemobileNumber.text.toString().trim(),
                emailId = binding.emailId.text.toString().trim(),
                houseNumber = binding.flathouseno.text.toString().trim(),
                areaSector = binding.areasector.text.toString().trim(),
                pinCode = binding.pincode.text.toString().trim(),
                currentAddress = binding.currentaddress.text.toString().trim(),
                state = binding.statename.text.toString(),
                city = binding.cityname.text.toString(),
                imagepath = customerImagePath,
                isAccepted = binding.acceptTermConditionCheck.isChecked
            )

            if (!isValid) {
                Toast.makeText(this@NewCustomerRegistrationPage, errorMessage, Toast.LENGTH_SHORT)
                    .show()
            } else {
                CustPhotoPath = photoUri
                CustFirstName = binding.firstName.text.toString().trim()
                CustMiddleName = binding.middleName.text.toString().trim()
                CustLastName = binding.lastName.text.toString().trim()
                CustPrimaryMobileNumber = binding.mobileNumber.text.toString().trim()
                CustAlternateMobileNumber = binding.alternatemobileNumber.text.toString().trim()
                isAggrementVerified = "yes"
                CusteMailID = binding.emailId.text.toString().trim()
                CustFlatNo = binding.flathouseno.text.toString().trim()
                CustAreaSector = binding.areasector.text.toString().trim()
                CustCurrentAddress = binding.currentaddress.text.toString().trim()
                CustPinCode = binding.pincode.text.toString().trim()
                CustStateName = binding.statename.text.toString()
                CustCityName = binding.cityname.text.toString()
                ConstantClass.ClickOnCardDashboard = "Customer"
                startActivity(Intent(this@NewCustomerRegistrationPage, MobileSelectionActivity::class.java))

            }

        }

    }



    fun OpenPopUpForVeryfyOTP(EmailID: String, otp: String) {
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
        val title = dialog.findViewById<TextView>(R.id.text_subtitle)

        startOtpTimer(resendtxt, timer)

        if (clickemailId) {
            title.text = "Enter four digit OTP send on your registered email id"
        } else {
            title.text = "Enter four digit OTP send on your registered mobile number"
        }

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

            if (validateLoginInput(EmailID, this)) {
                if (isInternetAvailable(this@NewCustomerRegistrationPage)) {
                    if (clickemailId) {
                        hitApiForReSendOTP(EmailID, "Mail")
                    }
                    if (clickmobile) {
                        hitApiForReSendOTP(EmailID, "Mobile")
                    }
                    if (clickalternatemobile) {
                        hitApiForReSendOTP(EmailID, "Alter Mobile")
                    }
                    startOtpTimer(resendtxt, timer)
                } else {
                    Toast.makeText(
                        this,
                        "Please check your internet connection!!",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }

        }

        verifyButton.setOnClickListener {
            val enteredOTP =
                OTP1.text.toString() + OTP2.text.toString() + OTP3.text.toString() + OTP4.text.toString()
            if (enteredOTP.length == 4) {
                if (clickemailId) {
                    hitApiForOTPVerify(EmailID, enteredOTP, "Mail id verify")
                }
                if (clickmobile) {
                    hitApiForOTPVerify(EmailID, enteredOTP, "Mobile verify")
                }

                if (clickalternatemobile) {
                    hitApiForOTPVerify(EmailID, enteredOTP, "Alter Mobile verify")
                }

            } else {
                Toast.makeText(this, "Please enter complete OTP", Toast.LENGTH_SHORT).show()
            }

        }

        dialog.show()

    }

    fun startOtpTimer(resendtxt: TextView, timer: TextView) {
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

    private fun createImageFile(): File {
        val fileName = "IMG_${System.currentTimeMillis()}"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(fileName, ".jpg", storageDir)
    }

    fun clickCameraForUploadDocument() {
        val photoFile = createImageFile()
        photoUri = FileProvider.getUriForFile(this, "${packageName}.fileprovider", photoFile)
        cameraLauncher.launch(photoUri!!)
    }

    private fun checkCameraPermissionAndOpenCamera() {
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            clickCameraForUploadDocument()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.CAMERA),
                CAMERA_REQUEST_CODE_FRONT
            )
        }
    }

    fun hitApiForReSendOTP(mailidormobile: String, type: String) {
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
                                if (clickemailId) {
                                    Toast.makeText(this, response.message, Toast.LENGTH_SHORT)
                                        .show()
                                } else {
                                    if (clickmobile || clickalternatemobile) {
                                        var otp = response.value
                                        var firstName = binding.firstName.text.toString()
                                        var lastName = binding.lastName.text.toString()
                                        var customerName = firstName.plus(" ").plus(lastName)
                                        hitApiForResendMobVerify(mailidormobile, customerName, otp)
                                    }
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


    fun hitApiForSendOTP(mailidormobile: String, type: String) {
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
                                Log.d("SendRes", response.message)
                                var otp = response.value
                                Log.d("OTP", otp)

                                if (response.statuss.equals("True")) {
                                    if (clickmobile || clickalternatemobile) {
                                        var firstName = binding.firstName.text.toString()
                                        var lastName = binding.lastName.text.toString()
                                        var customerName = firstName.plus(" ").plus(lastName)
                                        hitApiForMobVerify(mailidormobile, customerName, otp)
                                    }

                                    if (clickemailId) {
                                        ConstantClass.dialog.dismiss()
                                        Toast.makeText(this, response.message, Toast.LENGTH_SHORT)
                                            .show()
                                        OpenPopUpForVeryfyOTP(mailidormobile, "")
                                    }
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


    fun hitApiForOTPVerify(mobileOrEmailID: String, otp: String, message: String) {
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
                                    if (clickemailId) {
                                        EmailId = mobileOrEmailID
                                        emailIdveryfied = true
                                        binding.emailId.isEnabled = false
                                        binding.verifyiconemailId.visibility = View.VISIBLE
                                        binding.verifyEmailId.visibility = View.GONE
                                    }

                                    if (clickmobile) {
                                        CustPrimaryOTP = otp
                                        CustPrimaryMobileVerified = "yes"
                                        mobileveryfied = true
                                        binding.mobileNumber.isEnabled = false
                                        binding.verifyiconphonenumber.visibility = View.VISIBLE
                                        binding.verifymobilenumber.visibility = View.GONE
                                    }

                                    if (clickalternatemobile) {
                                        CustAlternateMobileOTP = otp
                                        CustAlternateMobileVerified = "yes"
                                        alternatemobileveryfied = true
                                        binding.alternatemobileNumber.isEnabled = false
                                        binding.alternateverifyiconphonenumber.visibility =
                                            View.VISIBLE
                                        binding.alternateverifymobilenumber.visibility = View.GONE
                                    }

                                    if (dialog != null && dialog.isShowing) {
                                        dialog.dismiss()
                                    }
                                } else {
                                    binding.emailId.isEnabled = true
                                    binding.verifyiconemailId.visibility = View.GONE
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


    fun isValidForm(
        firstName: String,
        middleName: String,
        lastName: String,
        mobileNumber: String,
        alternateMobile: String,
        emailId: String,
        houseNumber: String,
        areaSector: String,
        pinCode: String,
        currentAddress: String,
        state: String,
        city: String,
        imagepath: String?,
        isAccepted: Boolean
    ): Pair<Boolean, String?> {
        if (firstName.isBlank()) return Pair(false, "Enter first name")

        if (lastName.isBlank()) return Pair(false, "Enter last name")

        if (!mobileNumber.matches(Regex("^[6-9]\\d{9}$"))) return Pair(
            false,
            "Enter valid mobile number"
        )

        if (alternateMobile.isNotBlank() && !alternateMobile.matches(Regex("^[6-9]\\d{9}$")))
            return Pair(false, "Enter valid alternate mobile number")

        if (!emailId.matches(Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")))
            return Pair(false, "Enter valid email address")

        if (houseNumber.isBlank()) return Pair(false, "Enter house number")

        if (areaSector.isBlank()) return Pair(false, "Enter area/sector")

        if (!pinCode.matches(Regex("^[1-9][0-9]{5}$"))) return Pair(
            false,
            "Enter valid 6-digit PIN code"
        )

        if (currentAddress.isBlank()) return Pair(false, "Enter current address")

        if (state.isBlank()) return Pair(false, "Select state")

        if (city.isBlank()) return Pair(false, "Select city")

        if (imagepath!!.isBlank()) return Pair(false, "Upload customer photo")

        if (!isAccepted) return Pair(false, "Please accept the terms and conditions")

        return Pair(true, null)
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


    fun hitApiForMobVerify(mobnumber: String, customerName: String, OTP: String) {
        // hint: Dear  Naim Khan, Your OTP for Verification is 1234. Please Do Not Share the OTP With Anyone. Thanks For Using BOSOQ BOS CENTER
        var message =
            "Dear $customerName, Your OTP for Verification is $OTP. Please Do Not Share the OTP With Anyone. Thanks For Using BOSOQ BOS CENTER"

        lifecycleScope.launch {
            try {
                val response = api.sendSMSForVerifyMob(
                    apikey = ConstantClass.SMS_API_KEY,
                    senderid = ConstantClass.SMS_SENDER_ID,
                    templateid = ConstantClass.SMS_TEMPLATE_ID,
                    mobnumber = mobnumber,
                    message = message,
                )

                if (response!!.isSuccessful) {
                    Toast.makeText(
                        this@NewCustomerRegistrationPage,
                        "Otp sent on your mobile number!!",
                        Toast.LENGTH_SHORT
                    ).show()
                    val loanData = response.body()

                    if (ConstantClass.dialog != null && ConstantClass.dialog.isShowing) {
                        ConstantClass.dialog.dismiss()
                        OpenPopUpForVeryfyOTP(mobnumber, OTP)
                    }

                    Log.d("API_SUCCESS", loanData.toString())
                } else {
                    Log.e("API_ERROR", response.errorBody()?.string() ?: "Unknown error")
                }

            } catch (e: Exception) {
                Log.e("API_EXCEPTION", e.toString())
            }
        }
    }


    fun hitApiForResendMobVerify(mobnumber: String, customerName: String, OTP: String) {
        // hint: Dear  Naim Khan, Your OTP for Verification is 1234. Please Do Not Share the OTP With Anyone. Thanks For Using BOSOQ BOS CENTER
        var message =
            "Dear $customerName, Your OTP for Verification is $OTP. Please Do Not Share the OTP With Anyone. Thanks For Using BOSOQ BOS CENTER "

        lifecycleScope.launch {
            try {
                val response = api.sendSMSForVerifyMob(
                    apikey = ConstantClass.SMS_API_KEY,
                    senderid = ConstantClass.SMS_SENDER_ID,
                    templateid = ConstantClass.SMS_TEMPLATE_ID,
                    mobnumber = mobnumber,
                    message = message
                )

                if (response!!.isSuccessful) {
                    Toast.makeText(
                        this@NewCustomerRegistrationPage,
                        "Otp sent on your mobile number!!",
                        Toast.LENGTH_SHORT
                    ).show()
                    val loanData = response.body()

                    if (ConstantClass.dialog != null && ConstantClass.dialog.isShowing) {
                        ConstantClass.dialog.dismiss()
                    }
                    Log.d("API_SUCCESS", loanData.toString())
                } else {
                    Log.e("API_ERROR", response.errorBody()?.string() ?: "Unknown error")
                }
            } catch (e: Exception) {
                Log.e("API_EXCEPTION", e.toString())
            }
        }

    }


    override fun onBackPressed() {
        PanNumber=""
        PanFrontImageUri= null
        ConstantClass.AadharVerified=""
        CheckOnlineOrOffline =""
        AadharTransactionIdNo =""
        iisAggrementVerified = false
        isAggrementVerified = ""
        super.onBackPressed()
    }


}