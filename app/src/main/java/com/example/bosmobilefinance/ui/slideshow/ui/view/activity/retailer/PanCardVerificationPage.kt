package com.example.theemiclub.ui.slideshow.ui.view.activity.retailer

import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.ViewModelProvider
import com.bos.payment.appName.network.RetrofitClient
import com.example.theemiclub.R
import com.example.theemiclub.databinding.ActivityPanCardVerificationPageBinding
import com.example.theemiclub.ui.slideshow.constant.ConstantClass
import com.example.theemiclub.ui.slideshow.constant.ConstantClass.CheckOnlineOrOffline
import com.example.theemiclub.ui.slideshow.constant.ConstantClass.PanFrontImageUri
import com.example.theemiclub.ui.slideshow.constant.ConstantClass.PanNumber
import com.example.theemiclub.ui.slideshow.constant.ConstantClass.PanNumberVerified
import com.example.theemiclub.ui.slideshow.constant.ConstantClass.saveImageToCache
import com.example.theemiclub.ui.slideshow.data.model.loginsignup.verification.PanVerificationReq
import com.example.theemiclub.ui.slideshow.data.repository.AuthRepository
import com.example.theemiclub.ui.slideshow.data.viewModelFactory.CommonViewModelFactory
import com.example.theemiclub.ui.slideshow.localdb.SharedPreference
import com.example.theemiclub.ui.slideshow.ui.viewmodel.AuthenticationViewModel
import com.example.theemiclub.ui.slideshow.utils.ApiStatus
import com.google.gson.Gson
import java.io.File

class PanCardVerificationPage : AppCompatActivity() {
    lateinit var binding: ActivityPanCardVerificationPageBinding
    lateinit var viewModel: AuthenticationViewModel
    lateinit var preference: SharedPreference
    private val CAMERA_REQUEST_CODE_FRONT = 1001
    private var photoFrontUri: Uri? = null


    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                // Handle the photoUri, e.g., show image in ImageView
                binding.frontcardimage.visibility = View.VISIBLE
                binding.frontcardicon.visibility = View.GONE
                binding.frontcardimage.setImageURI(photoFrontUri)
                binding.uploadtextfront.text = "Re- Upload"
                // binding.frontcardimage.setImageURI(Uri.fromFile(saveImageToCache(this@AadharCardVerificationPage,photoFrontUri!!,"AadharFrontImage")))
                Log.d(
                    "ImageCache",
                    "File saved at: ${
                        saveImageToCache(
                            this@PanCardVerificationPage,
                            photoFrontUri!!,
                            "PanFrontImage"
                        )!!.absolutePath
                    }, size: ${
                        saveImageToCache(
                            this@PanCardVerificationPage,
                            photoFrontUri!!,
                            "PanFrontImage"
                        )!!.length()
                    } bytes"
                )

            }


        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPanCardVerificationPageBinding.inflate(layoutInflater)
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

        viewModel = ViewModelProvider(this, CommonViewModelFactory(AuthRepository(RetrofitClient.apiInterfacePAN)))[AuthenticationViewModel::class.java]
        preference = SharedPreference(this)

        OnClickListner()


    }


    fun OnClickListner() {

        if (ConstantClass.CheckOnlineOrOffline.equals(ConstantClass.online)) {
            binding.photolayout.visibility = View.GONE
        } else {
            binding.photolayout.visibility = View.VISIBLE
        }

        binding.back.setOnClickListener {
           onBackPressed()
        }

        binding.verifybuttonlayout.setOnClickListener {
            var panNumber = binding.pannumber.text.toString()
            // PAN validation (Regex: 5 letters, 4 digits, 1 letter)
            val panRegex = Regex("[A-Z]{5}[0-9]{4}[A-Z]{1}")

            if (panNumber.isBlank() || !panRegex.matches(panNumber.uppercase())) {
                Toast.makeText(this, "Enter a valid PAN number (e.g., ABCDE1234F)", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (CheckOnlineOrOffline.equals(ConstantClass.offline)) {
                // Image URI validation
                if (photoFrontUri == null || photoFrontUri == null) {
                    Toast.makeText(this, "Please upload  Pan image", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                PanNumber = panNumber
                PanNumberVerified = "no"
                PanFrontImageUri = photoFrontUri
                finish()
            }
            else {
                hitApiForPanVerification(panNumber)
            }


        }


        binding.uploadtextfront.setOnClickListener {
            checkCameraPermissionAndOpenCamera()
        }


    }


    private fun checkCameraPermissionAndOpenCamera() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            clickCameraForUploadDocument()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), CAMERA_REQUEST_CODE_FRONT)
        }
    }


    private fun createImageFile(): File {
        val fileName = "IMG_${System.currentTimeMillis()}"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(fileName, ".jpg", storageDir)
    }


    fun clickCameraForUploadDocument() {
        val photoFile = createImageFile()
        photoFrontUri = FileProvider.getUriForFile(this, "${packageName}.fileprovider", photoFile)
        cameraLauncher.launch(photoFrontUri!!)
    }


    fun hitApiForPanVerification(pannumber: String) {
        val firstName = preference.getStringValue(ConstantClass.FirstName, "").orEmpty()
        var panverificationreq = PanVerificationReq(
            panNumber = pannumber,
            firstName = firstName,
            registrationId = ConstantClass.PAN_VERIFICATION_REGISTRATION_ID,
        )
        Log.d("PanVerificationreq", Gson().toJson(panverificationreq))

        viewModel.getPanVerificationReq(panverificationreq).observe(this) { resources ->
            resources.let {
                when (it.apiStatus) {
                    ApiStatus.SUCCESS -> {
                        it.data.let { users ->
                            users!!.body().let { response ->
                                ConstantClass.dialog.dismiss()
                                Log.d("PanVerificationResp", Gson().toJson(response))

                                if (response!!.httpResponseCode == 205) {
                                    Toast.makeText(this@PanCardVerificationPage, "Please enter valid pan number!!", Toast.LENGTH_SHORT).show()
                                }

                                if (response!!.httpResponseCode == 0) {
                                    Toast.makeText(this@PanCardVerificationPage, response.message, Toast.LENGTH_SHORT).show()
                                }

                                if (response!!.result.status.equals("Active")) {
                                    PanNumber = pannumber
                                    PanNumberVerified = "yes"
                                    PanFrontImageUri = null
                                    finish()
                                }
                                else {
                                    Toast.makeText(this@PanCardVerificationPage, "Please enter valid pan number!!", Toast.LENGTH_SHORT).show()
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


    override fun onBackPressed() {
        PanNumber=""
        PanNumberVerified=""
        PanFrontImageUri=null
        super.onBackPressed()
    }


}