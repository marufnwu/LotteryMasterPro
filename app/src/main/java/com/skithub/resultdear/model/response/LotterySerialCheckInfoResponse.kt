package com.skithub.resultdear.model.response


import com.google.gson.annotations.SerializedName

data class LotterySerialCheckInfoResponse(
    @SerializedName("banner")
    var banner: Banner? = Banner(),
    @SerializedName("error")
    var error: Boolean = false,
    @SerializedName("isLicensed")
    var isLicensed: Boolean = false,
    @SerializedName("msg")
    var msg: String? = "",
    @SerializedName("audioUrl")
    var audioUrl: String? = ""
)