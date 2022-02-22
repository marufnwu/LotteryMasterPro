package com.skithub.resultdear.model.response


import com.google.gson.annotations.SerializedName

data class VideoTypeResposne(
    @SerializedName("error")
    var error: Boolean? = false,
    @SerializedName("msg")
    var msg: String? = "",
    @SerializedName("type")
    var type: Int? = 0
)