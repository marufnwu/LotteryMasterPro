package com.skithub.resultdear.model


import com.google.gson.annotations.SerializedName

data class ResultSlot(
    @SerializedName("active")
    var active: Boolean? = null,
    @SerializedName("id")
    var id: Int? = null,
    @SerializedName("time")
    var time: String? = null,
    @SerializedName("name")
    var name: String? = null
)