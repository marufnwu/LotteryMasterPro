package com.skithub.resultdear.model

import com.google.gson.annotations.SerializedName

data class LotterySerialModel(
    @SerializedName("Id") val id : String?,
    @SerializedName("LotteryNumber") val lotteryNumber : String?,
    @SerializedName("LotterySerialNumber") val lotterySerialNumber : String?,
    @SerializedName("WinType") val winType : String?,
    @SerializedName("WinDate") val winDate : String?,
    @SerializedName("WinTime") val winTime : String?,
    @SerializedName("WinDayName") val winDayName : String?,
    @SerializedName("SlotId") val SlotId : Int?,
    @SerializedName("Name") val name : String?,
    @SerializedName("Select") var select : Boolean= false
)
