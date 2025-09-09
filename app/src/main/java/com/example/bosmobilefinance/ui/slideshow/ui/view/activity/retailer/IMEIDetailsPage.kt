package com.example.bosmobilefinance.ui.slideshow.ui.view.activity.retailer

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bosmobilefinance.databinding.ActivityImeidetailsPageBinding
import com.example.bosmobilefinance.ui.slideshow.constant.ConstantClass.ImeiNumber1
import com.example.bosmobilefinance.ui.slideshow.constant.ConstantClass.ImeiNumber1SealPhotoPath
import com.example.bosmobilefinance.ui.slideshow.constant.ConstantClass.ImeiNumber2
import com.example.bosmobilefinance.ui.slideshow.constant.ConstantClass.ImeiNumber2SealPhotoPath
import com.example.bosmobilefinance.ui.slideshow.constant.ConstantClass.ImeiNumberPhotoPath
import com.example.bosmobilefinance.ui.slideshow.constant.ConstantClass.Invoive_Path
import java.io.File

class IMEIDetailsPage : AppCompatActivity() {
    lateinit var binding : ActivityImeidetailsPageBinding
    private val CAMERA_REQUEST_CODE_FRONT = 1001
    private  var imei1photoUri: Uri? = null
    private  var imei2photoUri: Uri?=null
    private  var invoicePhotoUri: Uri? = null
    private  var ImeiPhotoUri: Uri?=null
    var imei1photo: Boolean = false
    var imei2photo: Boolean = false
    var invoicePhoto: Boolean = false
    var ImeiPhoto: Boolean = false
    var back: Boolean = false



    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            if(imei1photo){
                // Handle the photoUri, e.g., show image in ImageView
                binding.imei1photo.visibility= View.VISIBLE
                binding.imei1photoicon.visibility= View.GONE
                binding.imei1photo.setImageURI(imei1photoUri)
                binding.clicktosealphoto1.text= "Re- Upload"
            }
            if(imei2photo){
                // Handle the photoUri, e.g., show image in ImageView
                binding.imei2photo.visibility= View.VISIBLE
                binding.imei2photoicon.visibility= View.GONE
                binding.imei2photo.setImageURI(imei2photoUri)
                binding.clicktosealphoto2.text= "Re- Upload"
            }

            if(ImeiPhoto){
                // Handle the photoUri, e.g., show image in ImageView
                binding.ImeiPhoto.visibility= View.VISIBLE
                binding.imeiicon.visibility= View.GONE
                binding.ImeiPhoto.setImageURI(ImeiPhotoUri)
                binding.clicktoimeiphoto.text= "Re- Upload"
            }

            if(invoicePhoto){
                // Handle the photoUri, e.g., show image in ImageView
                binding.invoicePhoto.visibility= View.VISIBLE
                binding.invoiceicon.visibility= View.GONE
                binding.invoicePhoto.setImageURI(invoicePhotoUri)
                binding.clicktoinvoice.text= "Re- Upload"
            }



        }


    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityImeidetailsPageBinding.inflate(layoutInflater)
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

        seonClickListner()

    }


    fun seonClickListner(){
        binding.back.setOnClickListener {
            finish()
        }

        binding.nextlayout.setOnClickListener {
            val (isValid, errorMessage) = isValidForm(
                IMEINumber1 = binding.IMEINumber1.text.toString().trim(),
                IMEINumber2 = binding.IMEInumber2.text.toString().trim(),
                imei1photoUri = imei1photoUri,
                imei2photoUri = imei2photoUri,
                invoicePhotoUri = invoicePhotoUri,
                ImeiPhotoUri = ImeiPhotoUri
            )
            if (!isValid) {
                Toast.makeText(this@IMEIDetailsPage, errorMessage, Toast.LENGTH_SHORT).show()
            }
            else{
                ImeiNumber1 = binding.IMEINumber1.text.toString().trim()
                ImeiNumber2 = binding.IMEINumber1.text.toString().trim()
                ImeiNumber1SealPhotoPath = imei1photoUri
                ImeiNumber2SealPhotoPath = imei2photoUri
                Invoive_Path = invoicePhotoUri
                ImeiNumberPhotoPath = ImeiPhotoUri
                startActivity(Intent(this@IMEIDetailsPage, QRCodePage::class.java))
            }

        }


        binding.clicktosealphoto1.setOnClickListener {
            imei1photo=true
            imei2photo=false
            ImeiPhoto=false
            invoicePhoto=false
            checkCameraPermissionAndOpenCamera()
        }


        binding.clicktosealphoto2.setOnClickListener {
            imei1photo=false
            imei2photo=true
            ImeiPhoto=false
            invoicePhoto=false
            checkCameraPermissionAndOpenCamera()

        }

        binding.clicktoimeiphoto.setOnClickListener {
            imei1photo=false
            imei2photo=false
            ImeiPhoto=true
            invoicePhoto=false
            checkCameraPermissionAndOpenCamera()

        }

        binding.clicktoinvoice.setOnClickListener {
            imei1photo=false
            imei2photo=false
            ImeiPhoto=false
            invoicePhoto=true
            checkCameraPermissionAndOpenCamera()

        }



    }

    private fun createImageFile(): File {
        val fileName = "IMG_${System.currentTimeMillis()}"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(fileName, ".jpg", storageDir)
    }

    fun clickCameraForUploadDocument(){
        val photoFile = createImageFile()
        if(imei1photo){
            imei1photoUri = FileProvider.getUriForFile(this, "${packageName}.fileprovider", photoFile)
            cameraLauncher.launch(imei1photoUri!!)
        }

        if(imei2photo){
            imei2photoUri = FileProvider.getUriForFile(this, "${packageName}.fileprovider", photoFile)
            cameraLauncher.launch(imei2photoUri!!)
        }

        if(ImeiPhoto){
            ImeiPhotoUri = FileProvider.getUriForFile(this, "${packageName}.fileprovider", photoFile)
            cameraLauncher.launch(ImeiPhotoUri!!)
        }

        if(invoicePhoto){
            invoicePhotoUri = FileProvider.getUriForFile(this, "${packageName}.fileprovider", photoFile)
            cameraLauncher.launch(invoicePhotoUri!!)
        }

    }

    private fun checkCameraPermissionAndOpenCamera() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            clickCameraForUploadDocument()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), CAMERA_REQUEST_CODE_FRONT)
        }
    }

    fun isValidForm(
        IMEINumber1: String,
        IMEINumber2: String,
        imei1photoUri: Uri?,
        imei2photoUri: Uri?,
        invoicePhotoUri: Uri?,
        ImeiPhotoUri: Uri?
    ): Pair<Boolean, String?> {

        if (IMEINumber1.isBlank() || IMEINumber1.length != 15 || !IMEINumber1.all { it.isDigit() }) {
            return Pair(false, "Enter a valid 15-digit IMEI Number 1")
        }

        if (IMEINumber2.isBlank() || IMEINumber2.length != 15 || !IMEINumber2.all { it.isDigit() }) {
            return Pair(false, "Enter a valid 15-digit IMEI Number 2")
        }

        if (imei1photoUri == null) {
            return Pair(false, "Upload IMEI 1 seal photo")
        }

        if (imei2photoUri == null) {
            return Pair(false, "Upload IMEI 2 seal photo")
        }

        if (invoicePhotoUri == null) {
            return Pair(false, "Upload invoice photo")
        }

        if (ImeiPhotoUri == null) {
            return Pair(false, "Upload combined IMEI photo")
        }

        return Pair(true, null)
    }


}