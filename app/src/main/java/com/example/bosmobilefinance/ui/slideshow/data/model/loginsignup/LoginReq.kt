package com.example.theemiclub.ui.slideshow.data.model.loginsignup

import com.google.gson.annotations.SerializedName

data class LoginReq (
 @SerializedName("mobileOrEmailID")
 var mobileormailid:String,
 @SerializedName("password")
 var password:String,
 @SerializedName("login_Type")
 var logintype:String
)
