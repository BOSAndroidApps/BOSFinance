package com.example.theemiclub.ui.slideshow.data.model.loginsignup

import com.google.gson.annotations.SerializedName

data class LoanCreatedResp(@SerializedName("data")
                           val data: Data,
                           @SerializedName("message")
                           val message: String = "",
                           @SerializedName("status")
                           val status: String = "")

data class Data(@SerializedName("interestRate")
                val interestRate:  Double = 0.0,
                @SerializedName("emiAmount")
                val emiAmount:  Double = 0.0,
                @SerializedName("imeiNumber")
                val imeiNumber: String = "",
                @SerializedName("endDate")
                val endDate: String = "",
                @SerializedName("downPayment")
                val downPayment: Double = 0.0,
                @SerializedName("customerCode")
                val customerCode: String = "",
                @SerializedName("loanCode")
                val loanCode: String = "",
                @SerializedName("rid")
                val rid:  Int = 0,
                @SerializedName("loanAmount")
                val loanAmount: Double = 0.0,
                @SerializedName("tenure")
                val tenure:  Double = 0.0,
                @SerializedName("startDate")
                val startDate: String = "")