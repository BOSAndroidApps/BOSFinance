package com.example.theemiclub.ui.slideshow.ui.view.activity.retailer

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
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
import com.example.theemiclub.CongratulationPage
import com.example.theemiclub.R
import com.example.theemiclub.databinding.ActivityQrcodePageBinding
import com.example.theemiclub.ui.slideshow.activity.DashBoard
import com.example.theemiclub.ui.slideshow.constant.ConstantClass
import com.example.theemiclub.ui.slideshow.constant.ConstantClass.AadharBackImageUri
import com.example.theemiclub.ui.slideshow.constant.ConstantClass.AadharFrontImageUri
import com.example.theemiclub.ui.slideshow.constant.ConstantClass.AadharNumber
import com.example.theemiclub.ui.slideshow.constant.ConstantClass.AccountNumber
import com.example.theemiclub.ui.slideshow.constant.ConstantClass.AccountType
import com.example.theemiclub.ui.slideshow.constant.ConstantClass.BankIFSCCode
import com.example.theemiclub.ui.slideshow.constant.ConstantClass.BankName
import com.example.theemiclub.ui.slideshow.constant.ConstantClass.BranchName
import com.example.theemiclub.ui.slideshow.constant.ConstantClass.BrandName
import com.example.theemiclub.ui.slideshow.constant.ConstantClass.CustAlternateMobileNumber
import com.example.theemiclub.ui.slideshow.constant.ConstantClass.CustAlternateMobileOTP
import com.example.theemiclub.ui.slideshow.constant.ConstantClass.CustAlternateMobileVerified
import com.example.theemiclub.ui.slideshow.constant.ConstantClass.CustAreaSector
import com.example.theemiclub.ui.slideshow.constant.ConstantClass.CustCityName
import com.example.theemiclub.ui.slideshow.constant.ConstantClass.CustCountry
import com.example.theemiclub.ui.slideshow.constant.ConstantClass.CustCurrentAddress
import com.example.theemiclub.ui.slideshow.constant.ConstantClass.CustFirstName
import com.example.theemiclub.ui.slideshow.constant.ConstantClass.CustFlatNo
import com.example.theemiclub.ui.slideshow.constant.ConstantClass.CustLastName
import com.example.theemiclub.ui.slideshow.constant.ConstantClass.CustMiddleName
import com.example.theemiclub.ui.slideshow.constant.ConstantClass.CustPhotoPath
import com.example.theemiclub.ui.slideshow.constant.ConstantClass.CustPinCode
import com.example.theemiclub.ui.slideshow.constant.ConstantClass.CustPrimaryMobileNumber
import com.example.theemiclub.ui.slideshow.constant.ConstantClass.CustPrimaryMobileVerified
import com.example.theemiclub.ui.slideshow.constant.ConstantClass.CustPrimaryOTP
import com.example.theemiclub.ui.slideshow.constant.ConstantClass.CustStateName
import com.example.theemiclub.ui.slideshow.constant.ConstantClass.CusteMailID
import com.example.theemiclub.ui.slideshow.constant.ConstantClass.Customer
import com.example.theemiclub.ui.slideshow.constant.ConstantClass.EmiAmount
import com.example.theemiclub.ui.slideshow.constant.ConstantClass.ImeiNumber1
import com.example.theemiclub.ui.slideshow.constant.ConstantClass.ImeiNumber1SealPhotoPath
import com.example.theemiclub.ui.slideshow.constant.ConstantClass.ImeiNumber2
import com.example.theemiclub.ui.slideshow.constant.ConstantClass.ImeiNumber2SealPhotoPath
import com.example.theemiclub.ui.slideshow.constant.ConstantClass.ImeiNumberPhotoPath
import com.example.theemiclub.ui.slideshow.constant.ConstantClass.InterestRate
import com.example.theemiclub.ui.slideshow.constant.ConstantClass.Invoive_Path
import com.example.theemiclub.ui.slideshow.constant.ConstantClass.ModelColor
import com.example.theemiclub.ui.slideshow.constant.ConstantClass.ModelName
import com.example.theemiclub.ui.slideshow.constant.ConstantClass.ModelVarient
import com.example.theemiclub.ui.slideshow.constant.ConstantClass.PanFrontImageUri
import com.example.theemiclub.ui.slideshow.constant.ConstantClass.PanNumber
import com.example.theemiclub.ui.slideshow.constant.ConstantClass.PanNumberVerified
import com.example.theemiclub.ui.slideshow.constant.ConstantClass.RefAddress
import com.example.theemiclub.ui.slideshow.constant.ConstantClass.RefName
import com.example.theemiclub.ui.slideshow.constant.ConstantClass.RefRelationShip
import com.example.theemiclub.ui.slideshow.constant.ConstantClass.RefmobileNo
import com.example.theemiclub.ui.slideshow.constant.ConstantClass.Tenure
import com.example.theemiclub.ui.slideshow.constant.ConstantClass.ToBePaidAmount
import com.example.theemiclub.ui.slideshow.constant.ConstantClass.calculateEmiEndDateFromNow
import com.example.theemiclub.ui.slideshow.constant.ConstantClass.createMultipartFromUri
import com.example.theemiclub.ui.slideshow.constant.ConstantClass.dialog
import com.example.theemiclub.ui.slideshow.constant.ConstantClass.getCurrentStartDate
import com.example.theemiclub.ui.slideshow.constant.ConstantClass.iisAggrementVerified
import com.example.theemiclub.ui.slideshow.constant.ConstantClass.isAggrementVerified

import com.example.theemiclub.ui.slideshow.constant.ConstantClass.isInternetAvailable
import com.example.theemiclub.ui.slideshow.data.model.loginsignup.LoanCreatedReq
import com.example.theemiclub.ui.slideshow.data.repository.AuthRepository
import com.example.theemiclub.ui.slideshow.data.viewModelFactory.CommonViewModelFactory
import com.example.theemiclub.ui.slideshow.localdb.SharedPreference
import com.example.theemiclub.ui.slideshow.ui.viewmodel.AuthenticationViewModel
import com.example.theemiclub.ui.slideshow.utils.ApiStatus
import com.google.gson.Gson
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody

class QRCodePage : AppCompatActivity() {
    lateinit var binding : ActivityQrcodePageBinding
    lateinit var viewModel: AuthenticationViewModel
    lateinit var api : ApiInterface
    lateinit var preference : SharedPreference

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityQrcodePageBinding.inflate(layoutInflater)
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
        preference = SharedPreference(this)
        api =  RetrofitClient.apiInterface
        viewModel = ViewModelProvider(this, CommonViewModelFactory(AuthRepository(RetrofitClient.apiInterface)))[AuthenticationViewModel::class.java]

        setDataOnUi()
        setOnClickListner()

    }

    fun setDataOnUi(){
        binding.username.text = CustFirstName.plus(" ").plus(CustLastName)
        binding.customerimage.setImageURI(CustPhotoPath)
        binding.clientcode.text="To be paid now  ₹ ".plus(ToBePaidAmount)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setOnClickListner(){

        binding.back.setOnClickListener {
            finish()
        }


        binding.nextlayout.setOnClickListener {
            if(isInternetAvailable(this@QRCodePage)) {

                /*    val registerreq = RegisterCustomerReq(
                      mode = "INSERT",
                      firstName = CustFirstName,
                      middleName = CustMiddleName,
                      lastName = CustLastName,
                      primaryMobileNumber = CustPrimaryMobileNumber,
                      primaryOTP = CustPrimaryOTP,
                      primaryMobileVerified = CustPrimaryMobileVerified,
                      alternateMobileNumber = CustAlternateMobileNumber,
                      alternateMobileOTP = CustAlternateMobileOTP,
                      pAlternateMobileVerified = CustAlternateMobileVerified,
                      eMailID = CusteMailID,
                      flatNo = CustFlatNo,
                      aearSector = CustAreaSector,
                      pinCode = CustPinCode,
                      currentAddress = CustCurrentAddress,
                      stateName = CustStateName,
                      cityName = CustCityName,
                      custPhoto_path = "",
                      country = CustCountry,
                      aadharNumber = AadharNumber,
                      aadharNumberVerified = "",
                      panNumber = PanNumber,
                      PanNumberVerified = "",
                      brandName = BrandName,
                      modelName = ConstantClass.ModelName,
                      modelVariant = ConstantClass.ModelVarient,
                      color = ConstantClass.ModelColor,
                      sellingPrice = ConstantClass.SellingPrice,
                      downPayment =ConstantClass.DownPayment,
                      tenure = ConstantClass.Tenure,
                      emiAmount = EmiAmount,
                      imeiNumber1 = ImeiNumber1,
                      imeiNumber1_SealPhotoPath = "",
                      imeiNumber2 = ImeiNumber2,
                      imeiNumber2_SealPhotoPath = "",
                      invoive_Path = "",
                      imeiNumber_PhotoPath = "",
                      accountNumber = AccountNumber,
                      bankIFSCCode = BankIFSCCode,
                      bankName = BankName,
                      accountType = AccountType,
                      branchName = BranchName,
                      refName = RefName,
                      refRelationShip = RefRelationShip,
                      refmobileNo = RefmobileNo,
                      refAddress = RefAddress,
                      debitOrCreditCard = "",
                      upiMandate = "",
                      createdBy = ""
                  )

                    Log.d("RegistrationReq", Gson().toJson(registerreq))
                    hitApiForCustomerRegister(registerreq)*/

                hitApiForCustomerRegister()

              }
            else{
                  Toast.makeText(this,"Please check your internet connection!!",Toast.LENGTH_SHORT).show()
              }

        }

    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun hitApiForCustomerRegister() {
        ConstantClass.OpenPopUpForVeryfyOTP(this)
        val custPhotoPart = createMultipartFromUri(this, CustPhotoPath, "CustPhoto_File","CustomerPhoto")
        val imei1SealPart = createMultipartFromUri(this, ImeiNumber1SealPhotoPath, "IMEINumber1_SealPhotoFile","IMEINumber1Image")
        val imei2SealPart = createMultipartFromUri(this, ImeiNumber2SealPhotoPath, "IMEINumber2_SealPhotoFile","IMEINumber2Image")
        val imeiPhotoPart = createMultipartFromUri(this, ImeiNumberPhotoPath, "IMEINumberPhotoFile","IMEINumberImage")
        val invoicePart = createMultipartFromUri(this, Invoive_Path, "InvoiceFile","InvoiceImage")
        val aadharFrontPart = createMultipartFromUri(this, AadharFrontImageUri, "CustAadharPhoto_File","AadharFrontImage")
        val aadharBackPart = createMultipartFromUri(this, AadharBackImageUri, "CustAadharBackPhoto_File","AadharBackImage")
        val PanFrontPart = createMultipartFromUri(this, PanFrontImageUri, "CustPanNumberPhoto_File","PanFrontImage") // pending


        val firstName = preference.getStringValue(ConstantClass.FirstName, "").orEmpty()
        val lastName = preference.getStringValue(ConstantClass.LastName, "").orEmpty()
        val safeLastName = if (!lastName.isNullOrBlank() && lastName != "null") lastName else ""
        var createdBy = firstName.plus(" ").plus(safeLastName)
        var retailercode =  preference.getStringValue(ConstantClass.RetailerCode, "")

        val requestMap = hashMapOf(
            "Mode" to "INSERT".toRequestBody(),
            "FirstName" to CustFirstName.toRequestBody(),
            "MiddleName" to CustMiddleName.toRequestBody(),
            "LastName" to CustLastName.toRequestBody(),
            "PrimaryMobileNumber" to CustPrimaryMobileNumber.toRequestBody(),
            "PrimaryOTP" to CustPrimaryOTP.toRequestBody(),
            "PrimaryMobileVerified" to CustPrimaryMobileVerified.toRequestBody(),
            "AlternateMobileNumber" to CustAlternateMobileNumber.toRequestBody(),
            "AlternateMobileOTP" to  CustAlternateMobileOTP.toRequestBody(),
            "PAlternateMobileVerified" to  CustAlternateMobileVerified.toRequestBody(),
            "EMailID" to CusteMailID.toRequestBody(),
            "FlatNo" to CustFlatNo.toRequestBody(),
            "AearSector" to CustAreaSector.toRequestBody(),
            "PinCode" to CustPinCode.toRequestBody(),
            "CurrentAddress" to CustCurrentAddress.toRequestBody(),
            "StateName" to CustStateName.toRequestBody(),
            "CityName" to CustCityName.toRequestBody(),
            "Country" to CustCountry!!.toRequestBody(),
            "AadharNumber" to AadharNumber.toRequestBody(),//
            "AadharNumberVerified" to ConstantClass.AadharVerified.toRequestBody()/*"".toRequestBody()*/,
            "PANNumber" to PanNumber.toRequestBody(),
            "PANNumberVerified" to PanNumberVerified.toRequestBody() /*"".toRequestBody()*/,
            "BrandName" to BrandName.toRequestBody(),
            "ModelName" to ConstantClass.ModelName.toRequestBody(),
            "ModelVariant" to ConstantClass.ModelVarient.toRequestBody(),
            "Color" to ConstantClass.ModelColor.toRequestBody(),
            "SellingPrice" to ConstantClass.SellingPrice.toRequestBody(),
            "DownPayment" to ConstantClass.DownPayment.toRequestBody(),
            "Tenure" to ConstantClass.Tenure.toRequestBody(),
            "EMIAmount" to EmiAmount.toRequestBody(),
            "IMEINumber1" to ImeiNumber1.toRequestBody(),
            "IMEINumber2" to ImeiNumber2.toRequestBody(),
            "AccountNumber" to AccountNumber.toRequestBody(),
            "BankIFSCCode" to BankIFSCCode.toRequestBody(),
            "BankName" to BankName.toRequestBody(),
            "AccountType" to AccountType.toRequestBody(),
            "BranchName" to BranchName.toRequestBody(),
            "RefName" to RefName.toRequestBody(),
            "RefRelationShip" to RefRelationShip.toRequestBody(),
            "RefmobileNo" to RefmobileNo.toRequestBody(),
            "RefAddress" to RefAddress.toRequestBody(),
            "DebitOrCreditCard" to "".toRequestBody(),
            "UPIMandate" to "yes".toRequestBody()/*"".toRequestBody()*/,
            "CreatedBy" to createdBy.toRequestBody(),
            "RetailerCode" to retailercode.toRequestBody(),
            "IsAggrementVerified" to isAggrementVerified.toRequestBody()
        )
        Log.d("RequestRegis",requestMap.toString())

         if(ConstantClass.CheckOnlineOrOffline.equals(ConstantClass.offline)){
             lifecycleScope.launch {
                 try {
                     val response = api.getRegisterCustomerReq(
                         requestMap["Mode"]!!,
                         requestMap["FirstName"]!!,
                         requestMap["MiddleName"]!!,
                         requestMap["LastName"]!!,
                         requestMap["PrimaryMobileNumber"]!!,
                         requestMap["PrimaryOTP"]!!,
                         requestMap["PrimaryMobileVerified"]!!,
                         requestMap["AlternateMobileNumber"]!!,
                         requestMap["AlternateMobileOTP"]!!,
                         requestMap["PAlternateMobileVerified"]!!,
                         requestMap["EMailID"]!!,
                         requestMap["FlatNo"]!!,
                         requestMap["AearSector"]!!,
                         requestMap["PinCode"]!!,
                         requestMap["CurrentAddress"]!!,
                         requestMap["StateName"]!!,
                         requestMap["CityName"]!!,
                         requestMap["Country"]!!,
                         requestMap["AadharNumber"]!!,
                         requestMap["AadharNumberVerified"]!!,
                         requestMap["PANNumber"]!!,
                         requestMap["PANNumberVerified"]!!,
                         requestMap["BrandName"]!!,
                         requestMap["ModelName"]!!,
                         requestMap["ModelVariant"]!!,
                         requestMap["Color"]!!,
                         requestMap["SellingPrice"]!!,
                         requestMap["DownPayment"]!!,
                         requestMap["Tenure"]!!,
                         requestMap["EMIAmount"]!!,
                         requestMap["IMEINumber1"]!!,
                         requestMap["IMEINumber2"]!!,
                         requestMap["AccountNumber"]!!,
                         requestMap["BankIFSCCode"]!!,
                         requestMap["BankName"]!!,
                         requestMap["AccountType"]!!,
                         requestMap["BranchName"]!!,
                         requestMap["RefName"]!!,
                         requestMap["RefRelationShip"]!!,
                         requestMap["RefmobileNo"]!!,
                         requestMap["RefAddress"]!!,
                         requestMap["DebitOrCreditCard"]!!,
                         requestMap["UPIMandate"]!!,
                         requestMap["CreatedBy"]!!,
                         requestMap["RetailerCode"]!!,
                         requestMap["IsAggrementVerified"]!!,
                         custPhotoPart!!,
                         imei1SealPart!!,
                         imei2SealPart!!,
                         imeiPhotoPart!!,
                         invoicePart!!,
                         aadharFrontPart!!,
                         aadharBackPart!!,
                         PanFrontPart!!
                     )
                     if (response.isSuccessful) {
                         // Handle success
                         val body = response.body()
                         /* ConstantClass.dialog.dismiss()
                          startActivity(Intent(this@QRCodePage,CongratulationPage::class.java))*/
                         var message = body?.message ?: "Success"

                         if(body!!.statuss.equals("FAILED")) {
                             if(ConstantClass.dialog!=null && ConstantClass.dialog.isShowing){
                                 ConstantClass.dialog.dismiss()
                             }
                             Toast.makeText(this@QRCodePage,body.message,Toast.LENGTH_SHORT).show()
                             startActivity(Intent(this@QRCodePage, DashBoard::class.java))
                             finish()
                             clearData()
                         }
                         else{
                             var customerCode = body?.customerCode
                             val safecustomerCode = if (!customerCode.isNullOrBlank() && customerCode != "null") customerCode else ""
                             val startDate = getCurrentStartDate()
                             val endDate = calculateEmiEndDateFromNow(Tenure.toInt())

                             var loancreatedreq = LoanCreatedReq(
                                 modetype = "INSERT",
                                 rid = 0,
                                 customerCode = safecustomerCode,
                                 loanAmount=ConstantClass.LoanAmount.toDouble(),
                                 downPayment= ConstantClass.DownPayment.toDouble(),
                                 emiAmount= EmiAmount.toDouble(),
                                 tenure= Tenure.toInt(),
                                 interestRate= InterestRate.toDouble(),
                                 startDate=startDate,
                                 endDate= endDate,
                                 imeiNumber= ImeiNumber1,
                                 createdBy=createdBy,
                                 brandname = BrandName,
                                 modelname = ModelName,
                                 variantname = ModelVarient,
                                 avlcolor = ModelColor,
                                 retailerCode =retailercode
                             )

                             Log.d("LoanCreateReq", Gson().toJson(loancreatedreq))
                             hitApiForRetailerCreatedLoan(loancreatedreq,message)
                         }

                     }
                     else {
                         // Handle error
                         Log.e("API ERROR", response.errorBody()?.string() ?: "Unknown error")
                     }
                 }
                 catch (e: Exception) {
                     if(ConstantClass.dialog!=null && ConstantClass.dialog.isShowing){
                         ConstantClass.dialog.dismiss()
                     }
                     Log.e("API EXCEPTION", e.toString())
                 }

             }
         }else{
             lifecycleScope.launch {
                 try {
                     val response = api.getRegisterOnlineCustomerReq(
                         requestMap["Mode"]!!,
                         requestMap["FirstName"]!!,
                         requestMap["MiddleName"]!!,
                         requestMap["LastName"]!!,
                         requestMap["PrimaryMobileNumber"]!!,
                         requestMap["PrimaryOTP"]!!,
                         requestMap["PrimaryMobileVerified"]!!,
                         requestMap["AlternateMobileNumber"]!!,
                         requestMap["AlternateMobileOTP"]!!,
                         requestMap["PAlternateMobileVerified"]!!,
                         requestMap["EMailID"]!!,
                         requestMap["FlatNo"]!!,
                         requestMap["AearSector"]!!,
                         requestMap["PinCode"]!!,
                         requestMap["CurrentAddress"]!!,
                         requestMap["StateName"]!!,
                         requestMap["CityName"]!!,
                         requestMap["Country"]!!,
                         requestMap["AadharNumber"]!!,
                         requestMap["AadharNumberVerified"]!!,
                         requestMap["PANNumber"]!!,
                         requestMap["PANNumberVerified"]!!,
                         requestMap["BrandName"]!!,
                         requestMap["ModelName"]!!,
                         requestMap["ModelVariant"]!!,
                         requestMap["Color"]!!,
                         requestMap["SellingPrice"]!!,
                         requestMap["DownPayment"]!!,
                         requestMap["Tenure"]!!,
                         requestMap["EMIAmount"]!!,
                         requestMap["IMEINumber1"]!!,
                         requestMap["IMEINumber2"]!!,
                         requestMap["AccountNumber"]!!,
                         requestMap["BankIFSCCode"]!!,
                         requestMap["BankName"]!!,
                         requestMap["AccountType"]!!,
                         requestMap["BranchName"]!!,
                         requestMap["RefName"]!!,
                         requestMap["RefRelationShip"]!!,
                         requestMap["RefmobileNo"]!!,
                         requestMap["RefAddress"]!!,
                         requestMap["DebitOrCreditCard"]!!,
                         requestMap["UPIMandate"]!!,
                         requestMap["CreatedBy"]!!,
                         requestMap["RetailerCode"]!!,
                         requestMap["IsAggrementVerified"]!!,
                         custPhotoPart!!,
                         imei1SealPart!!,
                         imei2SealPart!!,
                         imeiPhotoPart!!,
                         invoicePart!!
                     )
                     if (response.isSuccessful) {
                         // Handle success
                         val body = response.body()
                         /* ConstantClass.dialog.dismiss()
                          startActivity(Intent(this@QRCodePage,CongratulationPage::class.java))*/
                         var message = body?.message ?: "Success"

                         if(body!!.statuss.equals("FAILED")) {
                             if(ConstantClass.dialog!=null && ConstantClass.dialog.isShowing){
                                 ConstantClass.dialog.dismiss()
                             }
                             Toast.makeText(this@QRCodePage,body.message,Toast.LENGTH_SHORT).show()
                             startActivity(Intent(this@QRCodePage, DashBoard::class.java))
                             finish()
                             clearData()
                         }
                         else{
                             var customerCode = body?.customerCode
                             val safecustomerCode = if (!customerCode.isNullOrBlank() && customerCode != "null") customerCode else ""
                             val startDate = getCurrentStartDate()
                             val endDate = calculateEmiEndDateFromNow(Tenure.toInt())

                             var loancreatedreq = LoanCreatedReq(
                                 modetype = "INSERT",
                                 rid = 0,
                                 customerCode = safecustomerCode,
                                 loanAmount=ConstantClass.LoanAmount.toDouble(),
                                 downPayment= ConstantClass.DownPayment.toDouble(),
                                 emiAmount= EmiAmount.toDouble(),
                                 tenure= Tenure.toInt(),
                                 interestRate= InterestRate.toDouble(),
                                 startDate=startDate,
                                 endDate= endDate,
                                 imeiNumber= ImeiNumber1,
                                 createdBy=createdBy,
                                 brandname = BrandName,
                                 modelname = ModelName,
                                 variantname = ModelVarient,
                                 avlcolor = ModelColor,
                                 retailerCode =retailercode
                             )

                             Log.d("LoanCreateReq", Gson().toJson(loancreatedreq))
                             hitApiForRetailerCreatedLoan(loancreatedreq,message)
                         }

                     }
                     else {
                         // Handle error
                         Log.e("API ERROR", response.errorBody()?.string() ?: "Unknown error")
                     }
                 }
                 catch (e: Exception) {
                     if(ConstantClass.dialog!=null && ConstantClass.dialog.isShowing){
                         ConstantClass.dialog.dismiss()
                     }
                     Log.e("API EXCEPTION", e.toString())
                 }

             }
         }


    }

    fun hitApiForRetailerCreatedLoan(requset: LoanCreatedReq,message:String) {
        Log.d("customerReq", Gson().toJson(requset))
        viewModel.getRetailerLoanCreatedReq(requset).observe(this) { resources ->
            resources.let {
                when (it.apiStatus) {
                    ApiStatus.SUCCESS -> {
                        it.data?.let { users ->
                            users.body()?.let { response ->
                                if(ConstantClass.dialog!=null && ConstantClass.dialog.isShowing){
                                    ConstantClass.dialog.dismiss()
                                }
                                Log.d("customerres", response.message)
                               // Toast.makeText(this, response.message, Toast.LENGTH_SHORT).show()
                                if (response.status.equals("SUCCESS")) {
                                    Toast.makeText(this@QRCodePage,message , Toast.LENGTH_SHORT).show()
                                    startActivity(Intent(this@QRCodePage,CongratulationPage::class.java))
                                    clearData()
                                    finish()
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

                    }

                }
            }
        }
    }

    fun String.toRequestBody(): RequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), this)


    fun clearData() {
        CustFirstName = ""
        CustMiddleName = ""
        CustLastName = ""
        CustPrimaryMobileNumber = ""
        CustPrimaryOTP = ""
        CustPrimaryMobileVerified = ""
        CustAlternateMobileNumber = ""
        CustAlternateMobileOTP = ""
        CustAlternateMobileVerified = ""
        CusteMailID = ""
        CustFlatNo = ""
        CustAreaSector = ""
        CustPinCode = ""
        CustCurrentAddress = ""
        CustStateName = ""
        CustCityName = ""
        CustCountry = ""

        AadharNumber = ""
        PanNumber = ""

        BrandName = ""
        ConstantClass.ModelName = ""
        ConstantClass.ModelVarient = ""
        ConstantClass.ModelColor = ""
        ConstantClass.SellingPrice = ""
        ConstantClass.DownPayment = ""
        ConstantClass.Tenure = ""

        EmiAmount = ""

        ImeiNumber1 = ""
        ImeiNumber2 = ""

        AccountNumber = ""
        BankIFSCCode = ""
        BankName = ""
        AccountType = ""
        BranchName = ""

        RefName = ""
        RefRelationShip = ""
        RefmobileNo = ""
        RefAddress = ""
        iisAggrementVerified = false


    }



}