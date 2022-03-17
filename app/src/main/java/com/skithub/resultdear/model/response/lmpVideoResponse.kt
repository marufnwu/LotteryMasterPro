package com.skithub.resultdear.model.response


import com.google.gson.annotations.SerializedName
import com.skithub.resultdear.model.LmpVideo

data class lmpVideoResponse(
    @SerializedName("currentPage")
    var currentPage: String = "",
    @SerializedName("error")
    var error: Boolean = false,
    @SerializedName("lmpVideos")
    var lmpVideos: List<LmpVideo> = listOf(),
    @SerializedName("msg")
    var msg: String = "",
    @SerializedName("root")
    var root: String = "",
    @SerializedName("totalPages")
    var totalPages: Int = 0
)