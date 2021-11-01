package com.skithub.resultdear.model


import com.google.gson.annotations.SerializedName

data class Audio(
    @SerializedName("active")
    var active: String? = null,
    @SerializedName("audioUrl")
    var audioUrl: String? = null,
    @SerializedName("id")
    var id: String? = null,
    @SerializedName("name")
    var name: String? = null
)