package com.skithub.resultdear.model.response


import com.google.gson.annotations.SerializedName
import com.skithub.resultdear.model.LotterySerialModel

data class LotterySerialCheckResponse(
    @SerializedName("error")
    var error: Boolean? = false,
    @SerializedName("msg")
    var msg: String? = "",
    @SerializedName("number")
    var number: List<LotterySerialModel>? = listOf()
)