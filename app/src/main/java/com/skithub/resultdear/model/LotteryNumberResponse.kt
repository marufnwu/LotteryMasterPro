package com.skithub.resultdear.model

data class LotteryNumberResponse(
    val status: String?=null,
    val message: String?=null,
    val data: MutableList<LotteryNumberModel>?=null
)
