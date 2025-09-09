package com.example.bosmobilefinance.ui.slideshow.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.bosmobilefinance.ui.slideshow.data.model.loginsignup.CustomerLoanEmiReceiveReq
import com.example.bosmobilefinance.ui.slideshow.data.model.loginsignup.ForgotPasswordReq
import com.example.bosmobilefinance.ui.slideshow.data.model.loginsignup.GetCustomerLoanDetailsReq
import com.example.bosmobilefinance.ui.slideshow.data.model.loginsignup.GetEMISplitDetlailsReq
import com.example.bosmobilefinance.ui.slideshow.data.model.loginsignup.LoanCreatedReq
import com.example.bosmobilefinance.ui.slideshow.data.model.loginsignup.LoginReq
import com.example.bosmobilefinance.ui.slideshow.data.model.loginsignup.RegistrationReq
import com.example.bosmobilefinance.ui.slideshow.data.model.loginsignup.verification.SendOtpReq
import com.example.bosmobilefinance.ui.slideshow.data.model.loginsignup.VerifyOTPReq
import com.example.bosmobilefinance.ui.slideshow.data.model.loginsignup.cibilscore.CibilScoreReq
import com.example.bosmobilefinance.ui.slideshow.data.model.loginsignup.reports.GetReportsReq
import com.example.bosmobilefinance.ui.slideshow.data.model.loginsignup.verification.AadharVerificationReq
import com.example.bosmobilefinance.ui.slideshow.data.model.loginsignup.verification.PanVerificationReq
import com.example.bosmobilefinance.ui.slideshow.data.repository.AuthRepository
import com.example.bosmobilefinance.ui.slideshow.utils.ApiResponse
import kotlinx.coroutines.Dispatchers

class AuthenticationViewModel (private val repository: AuthRepository):ViewModel(){

    fun getRegistration(req: RegistrationReq) = liveData(Dispatchers.IO) {
        emit(ApiResponse.loading(data = null))
        try {
            emit(ApiResponse.success(data = repository.getregistration(req)))
        }catch (exception: Exception) {
            emit(ApiResponse.error(data = null, message = exception.message?: "Error Occurred!"))
        }
    }


    fun getLogin(req: LoginReq) = liveData(Dispatchers.IO) {
        emit(ApiResponse.loading(data = null))
        try {
            emit(ApiResponse.success(data = repository.getlogin(req)))
        }catch (exception: Exception) {
            emit(ApiResponse.error(data = null, message = exception.message?: "Error Occurred!"))
        }
    }


    fun sendOTPReq(req: SendOtpReq) = liveData(Dispatchers.IO) {
        emit(ApiResponse.loading(data = null))
        try {
            emit(ApiResponse.success(data = repository.sendOTP(req)))
        }catch (exception: Exception) {
            emit(ApiResponse.error(data = null, message = exception.message?: "Error Occurred!"))
        }
    }


    fun verifyOTPReq(req: VerifyOTPReq) = liveData(Dispatchers.IO) {
        emit(ApiResponse.loading(data = null))
        try {
            emit(ApiResponse.success(data = repository.verifyOTPReq(req)))
        }catch (exception: Exception) {
            emit(ApiResponse.error(data = null, message = exception.message?: "Error Occurred!"))
        }
    }


    fun forgotPasswordReq(req: ForgotPasswordReq) = liveData(Dispatchers.IO) {
        emit(ApiResponse.loading(data = null))
        try {
            emit(ApiResponse.success(data = repository.forgotPassword(req)))
        }catch (exception: Exception) {
            emit(ApiResponse.error(data = null, message = exception.message?: "Error Occurred!"))
        }
    }


    fun getMobileList() = liveData(Dispatchers.IO) {
        emit(ApiResponse.loading(data = null))
        try {
            emit(ApiResponse.success(data = repository.getMobileList()))
        }
        catch (exception: Exception) {
            emit(ApiResponse.error(data = null, message = exception.message?: "Error Occurred!"))
        }
    }


    fun getSplitEmiDetails(req: GetEMISplitDetlailsReq) = liveData(Dispatchers.IO) {
        emit(ApiResponse.loading(data = null))
        try {
            emit(ApiResponse.success(data = repository.getEmiSplitData(req)))
        }
        catch (exception: Exception) {
            emit(ApiResponse.error(data = null, message = exception.message?: "Error Occurred!"))
        }
    }


    fun getRetailerLoanCreatedReq(req: LoanCreatedReq) = liveData(Dispatchers.IO) {
        emit(ApiResponse.loading(data = null))
        try {
            emit(ApiResponse.success(data = repository.getRetailerLoanCreatedReq(req)))
        }
        catch (exception: Exception) {
            emit(ApiResponse.error(data = null, message = exception.message?: "Error Occurred!"))
        }
    }



    fun getCustomerLoanEmiDetailsReq(req: GetCustomerLoanDetailsReq) = liveData(Dispatchers.IO) {
        emit(ApiResponse.loading(data = null))
        try {
            emit(ApiResponse.success(data = repository.getCustomerLoanEmiDetailsReq(req)))
        }
        catch (exception: Exception) {
            emit(ApiResponse.error(data = null, message = exception.message?: "Error Occurred!"))
        }
    }




    fun getCustomerLoanDetailsReq(req: CustomerLoanEmiReceiveReq) = liveData(Dispatchers.IO) {
        emit(ApiResponse.loading(data = null))
        try {
            emit(ApiResponse.success(data = repository.getCustomerLoanEmiReceiveReq(req)))
        }
        catch (exception: Exception) {
            emit(ApiResponse.error(data = null, message = exception.message?: "Error Occurred!"))
        }
    }


    fun getPanVerificationReq(req: PanVerificationReq) = liveData(Dispatchers.IO) {
        emit(ApiResponse.loading(data = null))
        try {
            emit(ApiResponse.success(data = repository.getPanVerificationReq(req)))
        }
        catch (exception: Exception) {
            emit(ApiResponse.error(data = null, message = exception.message?: "Error Occurred!"))
        }
    }


    fun getAadharVerificationReq(req: AadharVerificationReq) = liveData(Dispatchers.IO) {
        emit(ApiResponse.loading(data = null))
        try {
            emit(ApiResponse.success(data = repository.getAadharVerificationReq(req)))
        }
        catch (exception: Exception) {
            emit(ApiResponse.error(data = null, message = exception.message?: "Error Occurred!"))
        }
    }



    fun getReportsReq(req: GetReportsReq) = liveData(Dispatchers.IO) {
        emit(ApiResponse.loading(data = null))
        try {
            emit(ApiResponse.success(data = repository.getReportsReq(req)))
        }
        catch (exception: Exception) {
            emit(ApiResponse.error(data = null, message = exception.message?: "Error Occurred!"))
        }
    }




}