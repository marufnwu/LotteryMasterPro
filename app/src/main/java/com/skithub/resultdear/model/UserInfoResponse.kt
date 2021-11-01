package com.skithub.resultdear.model

data class UserInfoResponse(
    val status: String?=null,
    val message: String?=null,
    val data: MutableList<UserInfoModel>?=null
)
