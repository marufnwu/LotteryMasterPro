package com.skithub.resultdear.model.response

import com.google.gson.annotations.SerializedName

data class CustomerNumber(
    @SerializedName("id"     ) var id     : String? = null,
    @SerializedName("number" ) var number : String? = null,
    @SerializedName("name"   ) var name   : String? = null,
    @SerializedName("active" ) var active : String? = null
)