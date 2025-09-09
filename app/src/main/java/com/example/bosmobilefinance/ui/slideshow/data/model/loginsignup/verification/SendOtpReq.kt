package com.example.theemiclub.ui.slideshow.data.model.loginsignup.verification

import com.google.gson.annotations.SerializedName

data class SendOtpReq (
    @SerializedName("mobileOrEmailID")
    var mobileoremailId:String,
    @SerializedName("otP_Type")
    var otpType:String
)
