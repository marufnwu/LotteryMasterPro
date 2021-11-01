package com.skithub.resultdear.model.response


import com.google.gson.annotations.SerializedName
import com.skithub.resultdear.model.ResultSlot

data class LotterySlotResponse(
    @SerializedName("error")
    var error: Boolean? = null,
    @SerializedName("msg")
    var msg: String? = null,
    @SerializedName("resultSlots")
    var resultSlots: MutableList<ResultSlot>? = null
)