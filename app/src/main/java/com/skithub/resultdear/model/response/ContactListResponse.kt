package com.skithub.resultdear.model.response

import com.google.gson.annotations.SerializedName

data class ContactListResponse(
    @SerializedName("error")var error: Boolean? = null,
    @SerializedName("numberList")var numberList: MutableList<CustomerNumber?>? = null,
    @SerializedName("whatsapp") var whatsapp: String? = null,
)