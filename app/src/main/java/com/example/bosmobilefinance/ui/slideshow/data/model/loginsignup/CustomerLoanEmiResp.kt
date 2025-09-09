package com.example.theemiclub.ui.slideshow.data.model.loginsignup

import com.google.gson.annotations.SerializedName

data class CustomerLoanEmiResp(
    @SerializedName("data")
    val data: MutableList<CustomerDataItem?>?,
    @SerializedName("message")
    val message: String = "",
    @SerializedName("status")
    val status: String = ""
)

data class CustomerDataItem(
    @SerializedName("interestRate")
    val interestRate: Double = 0.0,
    @SerializedName("brandName")
    val brandName: String = "",
    @SerializedName("endDate")
    val endDate: String = "",
    @SerializedName("customerCode")
    val customerCode: String = "",
    @SerializedName("message")
    val message: String = "",
    @SerializedName("statuss")
    val statuss: String = "",
    @SerializedName("loanAmount")
    val loanAmount: Double = 0.0,
    @SerializedName("modelName")
    val modelName: String = "",
    @SerializedName("emiAmount")
    val emiAmount: Double = 0.0,
    @SerializedName("downPayment")
    val downPayment: Double = 0.0,
    @SerializedName("loanCode")
    val loanCode: String = "",
    @SerializedName("variantName")
    val variantName: String = "",
    @SerializedName("value")
    val value: String = "",
    @SerializedName("tenure")
    val tenure: Int = 0,
    @SerializedName("startDate")
    val startDate: String = "",
    @SerializedName("avlbColors")
    val avlbColors: String = "",
    @SerializedName("paidEMI")
    val paidEmi: String = "",
    @SerializedName("duesEMI")
    val duesEmi: String = ""
)

