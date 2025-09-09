package com.bos.payment.appName.network

import com.example.bosmobilefinance.ui.slideshow.data.model.loginsignup.CustomerLoanEmiReceiveReq
import com.example.bosmobilefinance.ui.slideshow.data.model.loginsignup.CustomerLoanEmiResp
import com.example.bosmobilefinance.ui.slideshow.data.model.loginsignup.CustomerMakePaymentResp
import com.example.bosmobilefinance.ui.slideshow.data.model.loginsignup.CustomreLoanEmiReceiverResp
import com.example.bosmobilefinance.ui.slideshow.data.model.loginsignup.EmiSplitRes
import com.example.bosmobilefinance.ui.slideshow.data.model.loginsignup.ForgotPasswordReq
import com.example.bosmobilefinance.ui.slideshow.data.model.loginsignup.GetAllMobileDetailsListRes
import com.example.bosmobilefinance.ui.slideshow.data.model.loginsignup.GetCustomerLoanDetailsReq
import com.example.bosmobilefinance.ui.slideshow.data.model.loginsignup.GetEMISplitDetlailsReq
import com.example.bosmobilefinance.ui.slideshow.data.model.loginsignup.reports.GetReportsReq
import com.example.bosmobilefinance.ui.slideshow.data.model.loginsignup.LoanCreatedReq
import com.example.bosmobilefinance.ui.slideshow.data.model.loginsignup.LoanCreatedResp
import com.example.bosmobilefinance.ui.slideshow.data.model.loginsignup.LoginReq
import com.example.bosmobilefinance.ui.slideshow.data.model.loginsignup.RegisterCustomerResp
import com.example.bosmobilefinance.ui.slideshow.data.model.loginsignup.RegistrationReq
import com.example.bosmobilefinance.ui.slideshow.data.model.loginsignup.RegistrationRes
import com.example.bosmobilefinance.ui.slideshow.data.model.loginsignup.verification.SendOtpReq
import com.example.bosmobilefinance.ui.slideshow.data.model.loginsignup.verification.SendOtpRes
import com.example.bosmobilefinance.ui.slideshow.data.model.loginsignup.SmsResponse
import com.example.bosmobilefinance.ui.slideshow.data.model.loginsignup.VerifyOTPReq
import com.example.bosmobilefinance.ui.slideshow.data.model.loginsignup.cibilscore.CibilScoreReq
import com.example.bosmobilefinance.ui.slideshow.data.model.loginsignup.cibilscore.CibilScroeResp
import com.example.bosmobilefinance.ui.slideshow.data.model.loginsignup.reports.ReportsResp
import com.example.bosmobilefinance.ui.slideshow.data.model.loginsignup.verification.AadharVerificationReq
import com.example.bosmobilefinance.ui.slideshow.data.model.loginsignup.verification.AadharVerificationResp
import com.example.bosmobilefinance.ui.slideshow.data.model.loginsignup.verification.PanVerificationReq
import com.example.bosmobilefinance.ui.slideshow.data.model.loginsignup.verification.PanVerificationResp
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query


interface ApiInterface {

    //pan verification

    @POST("api/AOP/V1/Validation/PanBasic")
    suspend fun getPanVarification(@Body req : PanVerificationReq): Response<PanVerificationResp>?


    // Adhar verification
    @POST("api/AOP/V1/Validation/AadhaarValidateUrl")
    suspend fun getAadharVarification(@Body req : AadharVerificationReq): Response<AadharVerificationResp>?


    @POST("api/V1/OQFinance/Registration")
    suspend fun registration(@Body req: RegistrationReq): Response<RegistrationRes>?


    @POST("api/V1/OQFinance/Login")
    suspend fun login(@Body req: LoginReq): Response<RegistrationRes>?


    @POST("api/V1/OQFinance/SendOTP")
    suspend fun sendOTP(@Body req: SendOtpReq): Response<SendOtpRes>?


    @POST("api/V1/OQFinance/VerifyOTP")
    suspend fun verifyOTP(@Body req: VerifyOTPReq): Response<SendOtpRes>?


    @POST("api/V1/OQFinance/ForgotPassword")
    suspend fun forgotPassword(@Body req: ForgotPasswordReq): Response<SendOtpRes>?


    @POST("api/V1/OQFinance/GetAllDeviceDetails")
    suspend fun getAllDeviceDetails(): Response<GetAllMobileDetailsListRes>?


    @POST("api/V1/OQFinance/GetModelWiseLoanDetails")
    suspend fun getEmiSplitDataDetails(@Body req : GetEMISplitDetlailsReq): Response<EmiSplitRes>?


    @POST("api/V1/OQFinance/ManageLoan")
    suspend fun getLoanCreatedByRetailer(@Body req : LoanCreatedReq): Response<LoanCreatedResp>?


    @Multipart
    @POST("api/V1/OQFinance/ManageCustomer")
    suspend fun getRegisterCustomerReq(
        @Part("Mode") mode: RequestBody,
        @Part("FirstName") firstName: RequestBody,
        @Part("MiddleName") middleName: RequestBody,
        @Part("LastName") lastName: RequestBody,
        @Part("PrimaryMobileNumber") primaryMobileNumber: RequestBody,
        @Part("PrimaryOTP") primaryOTP: RequestBody,
        @Part("PrimaryMobileVerified") primaryMobileVerified: RequestBody,
        @Part("AlternateMobileNumber") alternateMobileNumber: RequestBody,
        @Part("AlternateMobileOTP") alternateMobileOTP: RequestBody,
        @Part("PAlternateMobileVerified") pAlternateMobileVerified: RequestBody,
        @Part("EMailID") eMailID: RequestBody,
        @Part("FlatNo") flatNo: RequestBody,
        @Part("AearSector") aearSector: RequestBody,
        @Part("PinCode") pinCode: RequestBody,
        @Part("CurrentAddress") currentAddress: RequestBody,
        @Part("StateName") stateName: RequestBody,
        @Part("CityName") cityName: RequestBody,
        @Part("Country") country: RequestBody,
        @Part("AadharNumber") aadharNumber: RequestBody,
        @Part("AadharNumberVerified") aadharNumberVerified: RequestBody,
        @Part("PANNumber") panNumber: RequestBody,
        @Part("PANNumberVerified") panNumberVerified: RequestBody,
        @Part("BrandName") brandName: RequestBody,
        @Part("ModelName") modelName: RequestBody,
        @Part("ModelVariant") modelVariant: RequestBody,
        @Part("Color") color: RequestBody,
        @Part("SellingPrice") sellingPrice: RequestBody,
        @Part("DownPayment") downPayment: RequestBody,
        @Part("Tenure") tenure: RequestBody,
        @Part("EMIAmount") emiAmount: RequestBody,
        @Part("IMEINumber1") imeiNumber1: RequestBody,
        @Part("IMEINumber2") imeiNumber2: RequestBody,
        @Part("AccountNumber") accountNumber: RequestBody,
        @Part("BankIFSCCode") bankIFSCCode: RequestBody,
        @Part("BankName") bankName: RequestBody,
        @Part("AccountType") accountType: RequestBody,
        @Part("BranchName") branchName: RequestBody,
        @Part("RefName") refName: RequestBody,
        @Part("RefRelationShip") refRelationShip: RequestBody,
        @Part("RefmobileNo") refmobileNo: RequestBody,
        @Part("RefAddress") refAddress: RequestBody,
        @Part("DebitOrCreditCard") debitOrCreditCard: RequestBody,
        @Part("UPIMandate") upiMandate: RequestBody,
        @Part("CreatedBy") createdBy: RequestBody,
        @Part("RetailerCode") retailercode: RequestBody,
        @Part("IsAggrementVerified") isAggrementVerified: RequestBody,
        @Part custPhoto_File: MultipartBody.Part?, // File here
        @Part imeiNumber1_SealPhotoPath: MultipartBody.Part?,
        @Part imeiNumber2_SealPhotoPath: MultipartBody.Part?,
        @Part imeiNumber_PhotoPath: MultipartBody.Part?,
        @Part invoive_Path: MultipartBody.Part?,
        @Part aadharFront_Path: MultipartBody.Part?,
        @Part aadharBack_Path: MultipartBody.Part?,
        @Part panFront_Path: MultipartBody.Part?,

    ): Response<RegisterCustomerResp>


    @Multipart
    @POST("api/V1/OQFinance/ManageCustomer")
    suspend fun getRegisterOnlineCustomerReq(
        @Part("Mode") mode: RequestBody,
        @Part("FirstName") firstName: RequestBody,
        @Part("MiddleName") middleName: RequestBody,
        @Part("LastName") lastName: RequestBody,
        @Part("PrimaryMobileNumber") primaryMobileNumber: RequestBody,
        @Part("PrimaryOTP") primaryOTP: RequestBody,
        @Part("PrimaryMobileVerified") primaryMobileVerified: RequestBody,
        @Part("AlternateMobileNumber") alternateMobileNumber: RequestBody,
        @Part("AlternateMobileOTP") alternateMobileOTP: RequestBody,
        @Part("PAlternateMobileVerified") pAlternateMobileVerified: RequestBody,
        @Part("EMailID") eMailID: RequestBody,
        @Part("FlatNo") flatNo: RequestBody,
        @Part("AearSector") aearSector: RequestBody,
        @Part("PinCode") pinCode: RequestBody,
        @Part("CurrentAddress") currentAddress: RequestBody,
        @Part("StateName") stateName: RequestBody,
        @Part("CityName") cityName: RequestBody,
        @Part("Country") country: RequestBody,
        @Part("AadharNumber") aadharNumber: RequestBody,
        @Part("AadharNumberVerified") aadharNumberVerified: RequestBody,
        @Part("PANNumber") panNumber: RequestBody,
        @Part("PANNumberVerified") panNumberVerified: RequestBody,
        @Part("BrandName") brandName: RequestBody,
        @Part("ModelName") modelName: RequestBody,
        @Part("ModelVariant") modelVariant: RequestBody,
        @Part("Color") color: RequestBody,
        @Part("SellingPrice") sellingPrice: RequestBody,
        @Part("DownPayment") downPayment: RequestBody,
        @Part("Tenure") tenure: RequestBody,
        @Part("EMIAmount") emiAmount: RequestBody,
        @Part("IMEINumber1") imeiNumber1: RequestBody,
        @Part("IMEINumber2") imeiNumber2: RequestBody,
        @Part("AccountNumber") accountNumber: RequestBody,
        @Part("BankIFSCCode") bankIFSCCode: RequestBody,
        @Part("BankName") bankName: RequestBody,
        @Part("AccountType") accountType: RequestBody,
        @Part("BranchName") branchName: RequestBody,
        @Part("RefName") refName: RequestBody,
        @Part("RefRelationShip") refRelationShip: RequestBody,
        @Part("RefmobileNo") refmobileNo: RequestBody,
        @Part("RefAddress") refAddress: RequestBody,
        @Part("DebitOrCreditCard") debitOrCreditCard: RequestBody,
        @Part("UPIMandate") upiMandate: RequestBody,
        @Part("CreatedBy") createdBy: RequestBody,
        @Part("RetailerCode") retailercode: RequestBody,
        @Part("IsAggrementVerified") isAggrementVerified: RequestBody,
        @Part custPhoto_File: MultipartBody.Part?, // File here
        @Part imeiNumber1_SealPhotoPath: MultipartBody.Part?,
        @Part imeiNumber2_SealPhotoPath: MultipartBody.Part?,
        @Part imeiNumber_PhotoPath: MultipartBody.Part?,
        @Part invoive_Path: MultipartBody.Part?
        ): Response<RegisterCustomerResp>


    // for customer.....................................................

    @POST("api/V1/OQFinance/GetLoanDetailsCustomerWise")
    suspend fun getCustomerLoanDetailsList(@Body req : GetCustomerLoanDetailsReq): Response<CustomerLoanEmiResp>?


    @POST("api/V1/OQFinance/LoanEMIReceiving")
    suspend fun getcustomerLoanEmiReceive(@Body req : CustomerLoanEmiReceiveReq): Response<CustomreLoanEmiReceiverResp>?


    @Multipart
    @POST("api/V1/OQFinance/MakePayment")
    suspend fun getCustomerReceiptUpload(
        @Part("CustomerCode") customercode: RequestBody,
        @Part("LoanCode") loancode: RequestBody,
        @Part("PaidAmount") paidamount: RequestBody,
        @Part("ReceiptImage_Path") path: RequestBody,
        @Part("CreatedBy") createBy: RequestBody,
        @Part("CreatedAt") createat: RequestBody,
        @Part("ActiveStatus") activestatus: RequestBody,
        @Part("RecordStatus") recordstatus: RequestBody,
        @Part("PaidEMINo") paidemino: RequestBody,
        @Part receiptImage: MultipartBody.Part
        ): Response<CustomerMakePaymentResp>


    // sms api implemented ...............................................


    @POST("vb/apikey.php")
    suspend fun sendSMSForVerifyMob(@Query("apikey") apikey : String,
                                    @Query("senderid") senderid : String,
                                    @Query("templateid") templateid : String,
                                    @Query("number") mobnumber : String,
                                    @Query("message") message : String): Response<SmsResponse>?


    // for customer and retailer both showing reports
    @POST("api/V1/OQFinance/GetLoanDetailsRetailerWise")
    suspend fun getReports(@Body req : GetReportsReq): Response<ReportsResp>?



   // cibil api for getting cibil score...............
    @POST("api/AOP/CreditAnalytics/Report")
    suspend fun getcibilscore(@Body req : CibilScoreReq): Response<CibilScroeResp>?


}