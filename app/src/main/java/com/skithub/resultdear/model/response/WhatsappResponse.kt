package com.skithub.resultdear.model.response


import com.google.gson.annotations.SerializedName

data class WhatsappResponse(
    @SerializedName("error")
    var error: Boolean? = null,
    @SerializedName("msg")
    var msg: String? = null,
    @SerializedName("number")
    var number: String? = null
)