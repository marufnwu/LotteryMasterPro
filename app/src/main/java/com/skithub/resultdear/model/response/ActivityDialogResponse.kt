package com.skithub.resultdear.model.response


import com.google.gson.annotations.SerializedName

data class ActivityDialogResponse(
    @SerializedName("activityDialog")
    var activityDialog: ActivityDialog? = ActivityDialog(),
    @SerializedName("error")
    var error: Boolean? = false,
    @SerializedName("msg")
    var msg: String? = ""
)