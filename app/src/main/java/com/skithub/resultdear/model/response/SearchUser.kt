package com.skithub.resultdear.model.response


import com.google.gson.annotations.SerializedName

data class SearchUser(
    @SerializedName("Phone")
    var phone: String? = null,
    @SerializedName("userId")
    var userId: String? = null
)