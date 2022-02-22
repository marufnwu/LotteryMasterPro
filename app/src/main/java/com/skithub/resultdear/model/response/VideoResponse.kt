package com.skithub.resultdear.model.response


import com.google.gson.annotations.SerializedName
import com.skithub.resultdear.model.Video


data class VideoResponse(
    @SerializedName("currentPage")
    var currentPage: String? = "",
    @SerializedName("error")
    var error: Boolean? = false,
    @SerializedName("msg")
    var msg: String? = "",
    @SerializedName("totalPages")
    var totalPages: Int? = 0,
    @SerializedName("videos")
    var videos: List<Video>? = listOf()
)