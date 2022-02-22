package com.skithub.resultdear.model.response


import com.google.gson.annotations.SerializedName

data class ActivityDialog(
    @SerializedName("action")
    var action: Boolean? = false,
    @SerializedName("actionUrl")
    var actionUrl: String? = "",
    @SerializedName("active")
    var active: Boolean? = false,
    @SerializedName("activity")
    var activity: String? = "",
    @SerializedName("id")
    var id: Int? = 0,
    @SerializedName("imageUrl")
    var imageUrl: String? = "",
    @SerializedName("description")
    var description: String? = "",
    @SerializedName("showImage")
    var showImage: Boolean? = false
)