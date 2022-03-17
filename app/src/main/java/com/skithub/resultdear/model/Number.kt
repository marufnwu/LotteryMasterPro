package com.skithub.resultdear.model


import com.google.gson.annotations.SerializedName

data class Number(
    @SerializedName("active")
    var active: String? = null,
    @SerializedName("id")
    var id: String? = null,
    @SerializedName("name")
    var name: String? = null,
    @SerializedName("number")
    var number: String? = null
)