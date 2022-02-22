package com.skithub.resultdear.model


import com.google.gson.annotations.SerializedName

data class FbShareContent(
    @SerializedName("hashtag")
    var hashtag: String? = "",
    @SerializedName("id")
    var id: String? = "",
    @SerializedName("instraction")
    var instraction: String? = "",
    @SerializedName("qoute")
    var qoute: String? = "",
    @SerializedName("sharelink")
    var sharelink: String? = ""
)