package com.example.bosmobilefinance.ui.slideshow.ui.view.activity.customer

import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
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
import com.example.bosmobilefinance.databinding.ActivityMakePaymentPageBinding
import com.example.bosmobilefinance.ui.slideshow.constant.ConstantClass
import com.example.bosmobilefinance.ui.slideshow.constant.ConstantClass.createMultipartFromUri
import com.example.bosmobilefinance.ui.slideshow.constant.ConstantClass.getCurrentUtcTimestamp
import com.example.bosmobilefinance.ui.slideshow.constant.ConstantClass.isInternetAvailable
import com.example.bosmobilefinance.ui.slideshow.constant.ConstantClass.saveImageToCache
import com.example.bosmobilefinance.ui.slideshow.data.model.loginsignup.GetCustomerLoanDetailsReq
import com.example.bosmobilefinance.ui.slideshow.data.repository.AuthRepository
import com.example.bosmobilefinance.ui.slideshow.data.viewModelFactory.CommonViewModelFactory
import com.example.bosmobilefinance.ui.slideshow.localdb.SharedPreference
import com.example.bosmobilefinance.ui.slideshow.ui.view.model.MakePaymentDataModel
import com.example.bosmobilefinance.ui.slideshow.ui.viewmodel.AuthenticationViewModel
import com.example.bosmobilefinance.ui.slideshow.utils.ApiStatus
import com.google.gson.Gson
import kotlinx.coroutines.launch
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class MakePaymentPage : AppCompatActivity() {
    lateinit var binding : ActivityMakePaymentPageBinding
    private val CAMERA_REQUEST_CODE_FRONT = 1001
    private var photoUri: Uri? = null
    lateinit var preference : SharedPreference
    lateinit var viewModel: AuthenticationViewModel
    lateinit var receiptUri : Uri
    lateinit var api : ApiInterface
    var imagepath: String? = ""
    var makepaymentList : MutableList<MakePaymentDataModel> = mutableListOf()
    var emiAmount :Double = 0.0

    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                // Handle the photoUri, e.g., show image in ImageView
                binding.receiptPhoto.visibility = View.VISIBLE
                binding.cameraicon.visibility = View.GONE
                binding.receiptPhoto.setImageURI(photoUri)
                binding.clicktosealphoto1.text="Re-Send"
                receiptUri= photoUri!!
                imagepath = saveImageToCache(this,receiptUri,"ReceiptPhoto")!!.absolutePath

            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMakePaymentPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        preference = SharedPreference(this)
        api =  RetrofitClient.apiInterface
        viewModel = ViewModelProvider(this, CommonViewModelFactory(AuthRepository(RetrofitClient.apiInterface)))[AuthenticationViewModel::class.java]

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

        if(isInternetAvailable(this@MakePaymentPage)) {
            HitApiForEmiList()
        }
        setOnClickListner()

    }


    fun setDataForSpinner(loanCodelist: Array<String>){
        val companyAdapter = ArrayAdapter(this, R.layout.mobilenamelayout, loanCodelist )
        companyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.loanid.adapter = companyAdapter

        binding.loanid.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {

                val loancode = parent.getItemAtPosition(position).toString()
                val dueEmi = makepaymentList.firstOrNull { it.loanCode == loancode }?.due
                val emiamount = makepaymentList.firstOrNull { it.loanCode == loancode }?.emiAmount
                emiAmount = emiamount!!
                binding.paidamount.text= emiamount.toString()
                val emiOptions = ConstantClass.generateEMIOptions(dueEmi!!.toInt())
                setDataForEmi(emiOptions)

            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // nothing
            }

        }




    }

    fun setDataForEmi(emiOptions:Array<String>){
        val noOfEmiAdapter = ArrayAdapter(this, R.layout.mobilenamelayout, emiOptions )
        noOfEmiAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.noofemi.adapter = noOfEmiAdapter

        binding.noofemi.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {

                val selectedItem = parent.getItemAtPosition(position).toString().toInt()
                val totalEmiAmount = emiAmount * selectedItem
                binding.paidamount.text = totalEmiAmount.toString()

            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // nothing
            }

        }

    }


    fun setOnClickListner(){
        binding.back.setOnClickListener {
            finish()
        }

        binding.clicktosealphoto1.setOnClickListener {
            checkCameraPermissionAndOpenCamera()
        }

        binding.submitlayout.setOnClickListener {
            if(isInternetAvailable(this@MakePaymentPage)) {
                val paidAmountText = binding.paidamount.text.toString()

                // Validation for empty amount
                if (paidAmountText.isEmpty()) {
                    Toast.makeText(this,"Please enter paid amount",Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                if (imagepath!!.isBlank()) {
                    Toast.makeText(this, "Please upload receipt photo", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                HitApiForUploadReceipt()
            }
        }

    }

    private fun checkCameraPermissionAndOpenCamera() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            clickCameraForUploadDocument()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.CAMERA),
                CAMERA_REQUEST_CODE_FRONT
            )
        }
    }

    fun clickCameraForUploadDocument() {
        val photoFile = createImageFile()
        photoUri = FileProvider.getUriForFile(this, "${packageName}.fileprovider", photoFile)
        cameraLauncher.launch(photoUri!!)
    }

    private fun createImageFile(): File {
        val fileName = "IMG_${System.currentTimeMillis()}"
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(fileName, ".jpg", storageDir)
    }

    fun HitApiForEmiList(){
        var loanemireq = GetCustomerLoanDetailsReq(
            loancode = "",
            customercode = preference.getStringValue(ConstantClass.CustomerCode,"")
        )
        Log.d("customerloanEmireq", Gson().toJson(loanemireq))

        viewModel.getCustomerLoanEmiDetailsReq(loanemireq).observe(this) { resources ->
            resources.let {
                when (it.apiStatus) {
                    ApiStatus.SUCCESS -> {
                        it.data?.let { users ->
                            users.body()?.let {
                                    response ->
                                Log.d("customerLoanemiresp", response.toString())
                                if(ConstantClass.dialog!=null && ConstantClass.dialog.isShowing){
                                    ConstantClass.dialog.dismiss()
                                    var LoanEmiList = response.data
                                    val loanCodelist: Array<String> = LoanEmiList!!.map { it!!.loanCode }.toTypedArray()



                                    LoanEmiList.forEach{it->
                                        makepaymentList.add(MakePaymentDataModel(it!!.loanCode,it.tenure,it.emiAmount,it.duesEmi))
                                    }

                                    if(loanCodelist.isNotEmpty()){
                                        setDataForSpinner(loanCodelist)
                                    }
                                }
                            }
                        }

                    }

                    ApiStatus.ERROR -> {
                        if(ConstantClass.dialog!=null && ConstantClass.dialog.isShowing){
                            ConstantClass.dialog.dismiss()
                        }
                    }

                    ApiStatus.LOADING -> {
                        ConstantClass.OpenPopUpForVeryfyOTP(this)
                    }

                }
            }
        }

    }

    fun HitApiForUploadReceipt(){
        val custPhotoPart = createMultipartFromUri(this, receiptUri, "ReceiptImage_FileName","ReceiptPhoto")
        val file=saveImageToCache(this,receiptUri,"ReceiptPhoto")
        val firstName = preference.getStringValue(ConstantClass.FirstName, "").orEmpty()
        val lastName = preference.getStringValue(ConstantClass.LastName, "").orEmpty()
        val safeLastName = if (!lastName.isNullOrBlank() && lastName != "null") lastName else ""
        var createdBy = firstName.plus(" ").plus(safeLastName)
        var customerCode =  preference.getStringValue(ConstantClass.CustomerCode, "")

        var createdAt = getCurrentUtcTimestamp()
        val requestMap = hashMapOf(
            "CustomerCode" to customerCode.toRequestBody(),
            "LoanCode" to binding.loanid.selectedItem.toString().toRequestBody(),
            "PaidAmount" to binding.paidamount.text.toString().toRequestBody(),
            "ReceiptImage_Path" to file!!.name.toRequestBody(),
            "CreatedBy" to createdBy.toRequestBody(),
            "CreatedAt" to createdAt.toRequestBody(),
            "ActiveStatus" to "Active".toRequestBody(),
            "RecordStatus" to  "Pending".toRequestBody(),
            "PaidEMINo" to  binding.noofemi.selectedItem.toString().toRequestBody()
        )
        Log.d("RequestMakePayment",requestMap.toString())

        ConstantClass.OpenPopUpForVeryfyOTP(this)
        lifecycleScope.launch {
            try {
                val response = api.getCustomerReceiptUpload(
                    requestMap["CustomerCode"]!!,
                    requestMap["LoanCode"]!!,
                    requestMap["PaidAmount"]!!,
                    requestMap["ReceiptImage_Path"]!!,
                    requestMap["CreatedBy"]!!,
                    requestMap["CreatedAt"]!!,
                    requestMap["ActiveStatus"]!!,
                    requestMap["RecordStatus"]!!,
                    requestMap["PaidEMINo"]!!,
                    custPhotoPart!!
                )
                if (response.isSuccessful) {
                    val body = response.body()
                    Log.d("ResponseReceipt", body!!.message)
                    if(ConstantClass.dialog!=null && ConstantClass.dialog.isShowing){
                        ConstantClass.dialog.dismiss()
                    }
                    binding.paidamount.text=""
                    Toast.makeText(this@MakePaymentPage,body!!.message,Toast.LENGTH_SHORT).show()
                    finish()
                }
                else {
                    // Handle error
                    Log.e("API ERROR", response.errorBody()?.string() ?: "Unknown error")
                }
            }
            catch (e: Exception) {
                Log.e("API EXCEPTION", e.toString())
            }
        }
    }

}