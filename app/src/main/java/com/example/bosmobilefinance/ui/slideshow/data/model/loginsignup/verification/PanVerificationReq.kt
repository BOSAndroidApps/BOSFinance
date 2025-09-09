package com.example.bosmobilefinance.ui.slideshow.data.model.loginsignup.verification

import com.google.gson.annotations.SerializedName

data class PanVerificationReq(
    @SerializedName("PanNumber")
    var panNumber:String ,
    @SerializedName("FirstName")
    var firstName:String,
    @SerializedName("RegistrationID")
    var registrationId:String
)
