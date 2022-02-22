package com.skithub.resultdear.model.response


import com.google.gson.annotations.SerializedName
import com.skithub.resultdear.model.FbShareContent

data class FbShareContentResponse(
    @SerializedName("error")
    var error: Boolean = false,
    @SerializedName("fbShareContent")
    var fbShareContent: FbShareContent? = FbShareContent(),

    @SerializedName("banner")
    var banner: Banner? = Banner(),
    @SerializedName("msg")
    var msg: String? = ""
)