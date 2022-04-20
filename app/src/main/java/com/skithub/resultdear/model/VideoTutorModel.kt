package com.skithub.resultdear.model

import com.google.gson.annotations.SerializedName

data class VideoTutorModel(
    @SerializedName("id") val id : String?,
    @SerializedName("video_title") val video_title :String,
    @SerializedName("thumbail") val thumbail : String?,
    @SerializedName("video_link") val video_link : String?,
)
