package com.skithub.resultdear.model

data class LotteryResultRecyclerModel(
    val winType: String?=null,
    val data: MutableList<LotteryNumberModel>?=null
)
