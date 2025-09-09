package com.example.theemiclub.ui.slideshow.data.model.loginsignup

import com.google.gson.annotations.SerializedName

data class RegistrationReq(
    @SerializedName("firstName")
    var firstName:String,
    @SerializedName("lastName")
    var lastName:String,
    @SerializedName("mobileNumber")
    var mobileNumber:String,
    @SerializedName("emailID")
    var emailId:String,
    @SerializedName("password")
    var password:String,
    @SerializedName("confirmPassword")
    var confrmpassword:String,
    @SerializedName("address")
    var address:String,
    @SerializedName("aadharNumber")
    var aadharnumber:String,
    @SerializedName("panNumber")
    var panNumber:String,
)
