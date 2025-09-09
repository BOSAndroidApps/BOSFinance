package com.example.theemiclub.ui.slideshow.data.repository

import com.bos.payment.appName.network.ApiInterface
import com.example.theemiclub.ui.slideshow.data.model.loginsignup.CustomerLoanEmiReceiveReq
import com.example.theemiclub.ui.slideshow.data.model.loginsignup.ForgotPasswordReq
import com.example.theemiclub.ui.slideshow.data.model.loginsignup.GetCustomerLoanDetailsReq
import com.example.theemiclub.ui.slideshow.data.model.loginsignup.GetEMISplitDetlailsReq
import com.example.theemiclub.ui.slideshow.data.model.loginsignup.LoanCreatedReq
import com.example.theemiclub.ui.slideshow.data.model.loginsignup.LoginReq
import com.example.theemiclub.ui.slideshow.data.model.loginsignup.RegistrationReq
import com.example.theemiclub.ui.slideshow.data.model.loginsignup.verification.SendOtpReq
import com.example.theemiclub.ui.slideshow.data.model.loginsignup.VerifyOTPReq
import com.example.theemiclub.ui.slideshow.data.model.loginsignup.reports.GetReportsReq
import com.example.theemiclub.ui.slideshow.data.model.loginsignup.verification.AadharVerificationReq
import com.example.theemiclub.ui.slideshow.data.model.loginsignup.verification.PanVerificationReq

class AuthRepository(private val apiInterface: ApiInterface) {

  suspend fun getregistration(req: RegistrationReq) = apiInterface.registration(req)

  suspend fun getlogin(req: LoginReq) = apiInterface.login(req)

  suspend fun sendOTP(req: SendOtpReq) = apiInterface.sendOTP(req)

  suspend fun verifyOTPReq(req: VerifyOTPReq) = apiInterface.verifyOTP(req)

  suspend fun forgotPassword(req: ForgotPasswordReq) = apiInterface.forgotPassword(req)

  suspend fun getMobileList() = apiInterface.getAllDeviceDetails()

  suspend fun getEmiSplitData(req: GetEMISplitDetlailsReq) = apiInterface.getEmiSplitDataDetails(req)

  suspend fun getRetailerLoanCreatedReq(req:LoanCreatedReq) = apiInterface.getLoanCreatedByRetailer(req)

  suspend fun getCustomerLoanEmiDetailsReq(req: GetCustomerLoanDetailsReq) = apiInterface.getCustomerLoanDetailsList(req)

  suspend fun getCustomerLoanEmiReceiveReq(req: CustomerLoanEmiReceiveReq) = apiInterface.getcustomerLoanEmiReceive(req)

  suspend fun getPanVerificationReq(req: PanVerificationReq) = apiInterface.getPanVarification(req)

  suspend fun getAadharVerificationReq(req: AadharVerificationReq) = apiInterface.getAadharVarification(req)

  suspend fun getReportsReq(req: GetReportsReq) = apiInterface.getReports(req)


}