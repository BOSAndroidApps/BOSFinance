package com.example.bosmobilefinance.ui.slideshow.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.bosmobilefinance.ui.slideshow.data.model.loginsignup.cibilscore.CibilScoreReq
import com.example.bosmobilefinance.ui.slideshow.data.repository.AuthRepository
import com.example.bosmobilefinance.ui.slideshow.data.repository.CibilRepository
import com.example.bosmobilefinance.ui.slideshow.utils.ApiResponse
import kotlinx.coroutines.Dispatchers

class CibilViewModel (private val repository: CibilRepository): ViewModel(){

    fun getCibilReq(req: CibilScoreReq) = liveData(Dispatchers.IO) {
        emit(ApiResponse.loading(data = null))
        try {
            emit(ApiResponse.success(data = repository.getReportsReq(req)))
        }
        catch (exception: Exception) {
            emit(ApiResponse.error(data = null, message = exception.message?: "Error Occurred!"))
        }
    }

}