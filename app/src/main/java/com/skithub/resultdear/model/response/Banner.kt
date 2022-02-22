package com.skithub.resultdear.model.response


import com.google.gson.annotations.SerializedName

data class Banner(
    @SerializedName("actionType")
    var actionType: Int? = null,
    @SerializedName("actionUrl")
    var actionUrl: String? = null,
    @SerializedName("activity")
    var activity: String? = null,
    @SerializedName("error")
    var error: Boolean = true,
    @SerializedName("id")
    var id: String? = null,
    @SerializedName("imageUrl")
    var imageUrl: String? = null,
    @SerializedName("msg")
    var msg: String? = null,
    @SerializedName("visible")
    var visible: Boolean? = null
)