package com.example.bosmobilefinance.ui.slideshow.data.model.loginsignup.reports

import com.google.gson.annotations.SerializedName

data class ReportsResp(@SerializedName("data")
                       val data: List<ReportsDataItem>?,
                       @SerializedName("message")
                       val message: String = "",
                       @SerializedName("status")
                       val status: String = "")