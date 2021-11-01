package com.skithub.resultdear.model

import com.google.gson.annotations.SerializedName

data class UserInfoModel(
    @SerializedName("Id") val id : String?,
    @SerializedName("Token") val token : String?,
    @SerializedName("Phone") val phone : String?,
    @SerializedName("paid_license") val paid_license : String?,
    @SerializedName("ac_position") val ac_position : String?,
    @SerializedName("RegistrationDate") val registrationDate : String?,
    @SerializedName("ActiveStatus") val activeStatus : String?
)
