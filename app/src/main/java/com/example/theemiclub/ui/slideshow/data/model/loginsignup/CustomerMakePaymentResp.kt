package com.example.theemiclub.ui.slideshow.data.model.loginsignup

import com.google.gson.annotations.SerializedName

data class CustomerMakePaymentResp(@SerializedName("message")
                                   val message: String = "",
                                   @SerializedName("statuss")
                                   val statuss: String = "",
                                   @SerializedName("value")
                                   val value: String = "")