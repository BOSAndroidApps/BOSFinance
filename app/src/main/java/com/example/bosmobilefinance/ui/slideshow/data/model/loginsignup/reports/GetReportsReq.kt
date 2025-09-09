package com.example.theemiclub.ui.slideshow.data.model.loginsignup.reports

import com.google.gson.annotations.SerializedName

data class GetReportsReq(
    @SerializedName("retailerCode")
    var retailercode:String,

    @SerializedName("recordStatus")
    var recordStatus:String,

    @SerializedName("customerCode")
    var customercode:String

    )
