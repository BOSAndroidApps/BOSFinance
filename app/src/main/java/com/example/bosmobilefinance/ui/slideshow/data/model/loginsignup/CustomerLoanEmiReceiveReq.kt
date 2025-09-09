package com.example.theemiclub.ui.slideshow.data.model.loginsignup

import com.google.gson.annotations.SerializedName

data class CustomerLoanEmiReceiveReq(
    @SerializedName("mode")
    var mode:String,

    @SerializedName("rid")
    var rid:Int,

    @SerializedName("loanCode")
    var loanCode:String,

    @SerializedName("emiAmount")
    var emiAmount:String,

    @SerializedName("paymentDate")
    var paymentDate:String,

    @SerializedName("paidAmount")
    var paidAmount:String,

    @SerializedName("paymentMode")
    var paymentMode:String,

    @SerializedName("utrNumber")
    var utrNumber:String,

    @SerializedName("remarks")
    var remarks:String,

    @SerializedName("createdBy")
    var creatdeBy:String,

    @SerializedName("receiptNo")
    var receiptNo:String,

    @SerializedName("customercode")
    var customerCode:String,

    @SerializedName("retailerCode")
    var retailercode:String,




)
