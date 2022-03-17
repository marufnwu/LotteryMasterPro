package com.skithub.resultdear.model


import com.google.gson.annotations.SerializedName

data class Contacts(
    @SerializedName("error")
    var error: Boolean? = null,
    @SerializedName("numberList")
    var numberList: List<Number>? = null,
    @SerializedName("whatsapp")
    var whatsapp: String? = null
)