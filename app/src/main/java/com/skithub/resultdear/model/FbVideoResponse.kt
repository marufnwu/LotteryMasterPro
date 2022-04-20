package com.skithub.resultdear.model

data class FbVideoResponse(
    val page: Int=0,
    val totalPage: Int=0,
    val status: String?=null,
    val message: String?=null,
    val data: MutableList<VideoTutorModel>?=null
)