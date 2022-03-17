package com.skithub.resultdear.model


import com.google.gson.annotations.SerializedName

data class LmpVideo(
    @SerializedName("description")
    var description: String = "",
    @SerializedName("gif")
    var gif: String = "",
    @SerializedName("id")
    var id: String = "",
    @SerializedName("short_id")
    var shortId: String = "",
    @SerializedName("thumbnail")
    var thumbnail: String = "",
    @SerializedName("title")
    var title: String = "",
    @SerializedName("video_id")
    var videoId: String = "",
    @SerializedName("videoUrl")
    var videoUrl: String = ""
)