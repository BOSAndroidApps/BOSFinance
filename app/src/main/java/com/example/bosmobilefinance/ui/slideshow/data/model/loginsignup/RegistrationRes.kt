package com.example.bosmobilefinance.ui.slideshow.data.model.loginsignup

import com.google.gson.annotations.SerializedName

data class RegistrationRes(@SerializedName("lastName")
                           val lastName: String? = "",
                           @SerializedName("address")
                           val address: String? = "",
                           @SerializedName("mobileNumber")
                           val mobileNumber: String? = "",
                           @SerializedName("customerCode")
                           val customerCode: String? = "",
                           @SerializedName("emailID")
                           val emailID: String? = "",
                           @SerializedName("panNumber")
                           val panNumber: String? = "",
                           @SerializedName("message")
                           val message: String = "",
                           @SerializedName("statuss")
                           val statuss: Boolean = false,
                           @SerializedName("firstName")
                           val firstName: String? = "",
                           @SerializedName("password")
                           val password: String? = "",
                           @SerializedName("aadharNumber")
                           val aadharNumber: String? = "",
                           @SerializedName("activeStatus")
                           val activeStatus: String? = "",
                           @SerializedName("value")
                           val value: String = "",
                           @SerializedName("mobileNo")
                           val mobileno: String? = "",
                           @SerializedName("retailerCode")
                           val retailerCode: String? = "",
)