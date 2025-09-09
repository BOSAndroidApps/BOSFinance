package com.example.theemiclub.ui.slideshow.data.model.loginsignup.verification

import com.google.gson.annotations.SerializedName


data class SendOtpRes(@SerializedName("message")
                      val message: String = "",
                      @SerializedName("statuss")
                      val statuss: String = "",
                      @SerializedName("value")
                      val value: String = "")