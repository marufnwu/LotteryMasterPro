package com.skithub.resultdear.model

data class LotteryPdfResponse (
    val status: String?=null,
    val message: String?=null,
    val data: MutableList<LotteryPdfModel>?=null
)