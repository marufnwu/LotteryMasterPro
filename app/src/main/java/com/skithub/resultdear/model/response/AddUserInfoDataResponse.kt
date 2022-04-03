package com.skithub.resultdear.model.response


import com.google.gson.annotations.SerializedName

data class AddUserInfoDataResponse(
    @SerializedName("banner")
    var banner: Banner? = null,
    @SerializedName("text")
    var text: String = ""
)