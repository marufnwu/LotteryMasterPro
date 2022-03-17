package com.skithub.resultdear.model.response


import com.google.gson.annotations.SerializedName
import com.skithub.resultdear.model.Audio

data class AudioResponse(
    @SerializedName("audio")
    var audio: Audio? = null,
    @SerializedName("error")
    var error: Boolean = false,
    @SerializedName("msg")
    var msg: String? = null
)