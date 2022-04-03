package com.skithub.resultdear.model.response

import com.google.gson.annotations.SerializedName

data class GenericResponse(
    @SerializedName("error")
    val error: Boolean = true,
    @SerializedName("msg")
    val msg: String = "",
)
