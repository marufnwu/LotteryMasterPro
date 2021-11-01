package com.skithub.resultdear.model

import com.google.gson.annotations.SerializedName

data class AdsImageModel(
    @SerializedName("Id") val id : String?,
    @SerializedName("ImageUrl") val imageUrl : String?,
    @SerializedName("TargetUrl") val targetUrl : String?,
    @SerializedName("ActiveStatus") val activeStatus : String?
)
