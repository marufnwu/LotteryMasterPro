package com.skithub.resultdear.model.response


import com.google.gson.annotations.SerializedName
import com.skithub.resultdear.model.License

data class SerialCheckLicenseResponse(
    @SerializedName("error")
    var error: Boolean = false,
    @SerializedName("license")
    var license: License? = License(),
    @SerializedName("msg")
    var msg: String = ""
)