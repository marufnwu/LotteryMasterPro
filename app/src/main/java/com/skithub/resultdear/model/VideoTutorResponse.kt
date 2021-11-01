package com.skithub.resultdear.model

data class VideoTutorResponse(
    val status: String?=null,
    val message: String?=null,
    val data: MutableList<VideoTutorModel>?=null
)