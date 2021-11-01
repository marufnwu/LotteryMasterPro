package com.skithub.resultdear.model.response

import com.google.gson.annotations.SerializedName

data class BannerRes(
    @SerializedName("error") var error : Boolean = true,
    @SerializedName("msg") var msg : String? = null,
    @SerializedName("id") var id : String? = null,
    @SerializedName("activity") var activity : String? = null,
    @SerializedName("imageUrl") var imageUrl : String? = null,
    @SerializedName("actionUrl") var actionUrl : String? = null,
    @SerializedName("actionType") var actionType : Int = 0,
    @SerializedName("visible") var visible : Boolean = false
    )