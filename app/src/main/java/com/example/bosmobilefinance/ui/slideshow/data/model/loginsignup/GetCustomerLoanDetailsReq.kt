package com.example.theemiclub.ui.slideshow.data.model.loginsignup

import com.google.gson.annotations.SerializedName

data class GetCustomerLoanDetailsReq(
    @SerializedName("loanCode")
    var loancode:String,
    @SerializedName("customerCode")
    var customercode:String
)
