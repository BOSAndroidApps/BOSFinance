package com.example.bosmobilefinance.ui.slideshow.data.repository

import com.bos.payment.appName.network.ApiInterface
import com.example.bosmobilefinance.ui.slideshow.data.model.loginsignup.cibilscore.CibilScoreReq

class CibilRepository(private val apiInterface: ApiInterface) {

    suspend fun getReportsReq(req: CibilScoreReq) = apiInterface.getcibilscore(req)

}