package com.skithub.resultdear.model


import com.google.gson.annotations.SerializedName

data class License(
    @SerializedName("expire_date")
    var expireDate: String? = "",
    @SerializedName("id")
    var id: String? = "",
    @SerializedName("license_number")
    var licenseNumber: String? = "",
    @SerializedName("plan_type")
    var planType: String? = "",
    @SerializedName("started_date")
    var startedDate: String? = "",
    @SerializedName("status")
    var status: String? = "",
    @SerializedName("user_id")
    var userId: String? = ""
)