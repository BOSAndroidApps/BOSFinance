package com.example.bosmobilefinance.ui.slideshow.ui.view.activity.retailer

import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.EditText
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
import com.bos.payment.appName.network.RetrofitClient
import com.example.bosmobilefinance.databinding.ActivityAadharCardVerificationPageBinding
import com.example.bosmobilefinance.ui.slideshow.constant.ConstantClass
import com.example.bosmobilefinance.ui.slideshow.constant.ConstantClass.AadharBackImageUri
import com.example.bosmobilefinance.ui.slideshow.constant.ConstantClass.AadharFrontImageUri
import com.example.bosmobilefinance.ui.slideshow.constant.ConstantClass.AadharNumber
import com.example.bosmobilefinance.ui.slideshow.constant.ConstantClass.saveImageToCache
import com.example.bosmobilefinance.ui.slideshow.data.model.loginsignup.verification.AadharVerificationReq
import com.example.bosmobilefinance.ui.slideshow.data.repository.AuthRepository
import com.example.bosmobilefinance.ui.slideshow.data.viewModelFactory.CommonViewModelFactory
import com.example.bosmobilefinance.ui.slideshow.localdb.SharedPreference
import com.example.bosmobilefinance.ui.slideshow.ui.view.activity.retailer.AadharCardWebViewDIGILockerPage.Companion.digilockerLink
import com.example.bosmobilefinance.ui.slideshow.ui.viewmodel.AuthenticationViewModel
import com.example.bosmobilefinance.ui.slideshow.utils.ApiStatus
import com.google.gson.Gson
import java.io.File

class AadharCardVerificationPage : AppCompatActivity() {
    lateinit var binding : ActivityAadharCardVerificationPageBinding
    private val CAMERA_REQUEST_CODE_FRONT = 1001
    private  var photoFrontUri: Uri? = null
    private  var photoBackUri: Uri?=null
    var front: Boolean = false
    var back: Boolean = false
    lateinit var dialog : Dialog
    lateinit var countDownTimer: CountDownTimer

    lateinit var viewModel: AuthenticationViewModel
    lateinit var preference : SharedPreference


    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            if(front){
                // Handle the photoUri, e.g., show image in ImageView
                binding.frontcardimage.visibility= View.VISIBLE
                binding.frontcardicon.visibility= View.GONE
                binding.frontcardimage.setImageURI(photoFrontUri)
                binding.uploadtextfront.text= "Re- Upload"
                // binding.frontcardimage.setImageURI(Uri.fromFile(saveImageToCache(this@AadharCardVerificationPage,photoFrontUri!!,"AadharFrontImage")))
                Log.d("ImageCache", "File saved at: ${saveImageToCache(this@AadharCardVerificationPage,photoFrontUri!!,"AadharFrontImage")!!.absolutePath}, size: ${saveImageToCache(this@AadharCardVerificationPage,photoFrontUri!!,"AadharFrontImage")!!.length()} bytes")

            }
            else{
                // Handle the photoUri, e.g., show image in ImageView
                binding.backcardimage.visibility= View.VISIBLE
                binding.camerabackicon.visibility= View.GONE
                binding.backcardimage.setImageURI(photoBackUri)
                binding.uploadtextback.text= "Re- Upload"
            }

        }


    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAadharCardVerificationPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, CommonViewModelFactory(AuthRepository(RetrofitClient.apiInterfacePAN)))[AuthenticationViewModel::class.java]
        preference = SharedPreference(this)

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

        setOnClickListner()

        setDataOnUI()

    }

    fun setDataOnUI(){

    }


    fun setOnClickListner(){

        binding.back.setOnClickListener {
            finish()
        }

        binding.uploadtextfront.setOnClickListener {
            front= true
            back = false
            checkCameraPermissionAndOpenCamera()
        }

        binding.uploadtextback.setOnClickListener {
            front= false
            back = true
            checkCameraPermissionAndOpenCamera()
        }

        binding.verifybuttonlayout.setOnClickListener {
            val aadharNumber = binding.aadharnumberEdittxt.text.toString().trim()

            // Aadhaar validation
            if (aadharNumber.isBlank() || aadharNumber.length != 12 || !aadharNumber.all { it.isDigit() }) {
                Toast.makeText(this, "Enter a valid 12-digit Aadhaar number", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Image URI validation
            if (photoFrontUri == null || photoBackUri == null) {
                Toast.makeText(this, "Please upload both front and back Aadhaar images", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            /*if(CheckOnlineOrOffline.equals(ConstantClass.offline)){*/
                // doing shortcut ........................
                  AadharFrontImageUri = photoFrontUri
                  AadharBackImageUri = photoBackUri
                  AadharNumber = aadharNumber
                  ConstantClass.AadharVerified = "no"
                  val intent = Intent(this, NewCustomerRegistrationPage::class.java)
                  startActivity(intent)
           /* }
            else{
               // hitApiForAadharVerification(aadharNumber)
            }*/

        }



    }

    private fun createImageFile(): File {
        val fileName = "IMG_${System.currentTimeMillis()}"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(fileName, ".jpg", storageDir)
    }

    fun clickCameraForUploadDocument(){
        val photoFile = createImageFile()
        if(front){
            photoFrontUri = FileProvider.getUriForFile(this, "${packageName}.fileprovider", photoFile)
            cameraLauncher.launch(photoFrontUri!!)
        }else{
            photoBackUri = FileProvider.getUriForFile(this, "${packageName}.fileprovider", photoFile)
            cameraLauncher.launch(photoBackUri!!)
        }

    }

    private fun checkCameraPermissionAndOpenCamera() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            clickCameraForUploadDocument()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), CAMERA_REQUEST_CODE_FRONT)
        }
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



    fun hitApiForAadharVerification(aadharNumber:String){
        val firstName = preference.getStringValue(ConstantClass.FirstName, "").orEmpty()
        val lastName = preference.getStringValue(ConstantClass.LastName, "").orEmpty()
        val emailId = preference.getStringValue(ConstantClass.CustomerEmailID, "").orEmpty()
        val mob = preference.getStringValue(ConstantClass.CustomerMobileNumber, "").orEmpty()

        var aadharverificationreq = AadharVerificationReq(
            firstName = firstName,
            lastName = lastName,
            mobileNumber = mob,
            emailId = emailId,
            registrationId = ConstantClass.PAN_VERIFICATION_REGISTRATION_ID,
        )

        Log.d("AadharVerificationreq", Gson().toJson(aadharverificationreq))

        viewModel.getAadharVerificationReq(aadharverificationreq).observe(this){
                resources->resources.let{
            when(it.apiStatus){
                ApiStatus.SUCCESS->{
                    it.data.let {
                            users -> users!!.body().let { response ->
                        ConstantClass.dialog.dismiss()
                        Log.d("AadharVerificationResp", Gson().toJson(response))
                        if(response!!.code==null){
                            Toast.makeText(this@AadharCardVerificationPage,response.message,Toast.LENGTH_SHORT).show()
                        }
                        if(response!!.code.equals("200")){
                            digilockerLink = response!!.model.url
                            // If all validations pass, proceed
                            AadharNumber = aadharNumber
                            AadharFrontImageUri = photoFrontUri
                            AadharBackImageUri = photoBackUri
                            //OpenPopUpForVeryfyOTP()
                            startActivity(Intent(this@AadharCardVerificationPage,AadharCardWebViewDIGILockerPage::class.java))
                            finish()
                        }
                        else{
                            Toast.makeText(this@AadharCardVerificationPage,response.message,Toast.LENGTH_SHORT).show()
                        }

                    }
                    }
                }
                ApiStatus.ERROR->{
                    ConstantClass.dialog.dismiss()
                }

                ApiStatus.LOADING->{
                    ConstantClass.OpenPopUpForVeryfyOTP(this)
                }
            }
        }
        }

    }




}