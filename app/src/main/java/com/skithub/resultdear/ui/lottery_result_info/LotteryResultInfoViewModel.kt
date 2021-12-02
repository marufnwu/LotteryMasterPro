package com.skithub.resultdear.ui.lottery_result_info

import androidx.lifecycle.ViewModel
import com.skithub.resultdear.database.PdfRepositories
import com.skithub.resultdear.database.network.MyApi

class LotteryResultInfoViewModel(var myApi: MyApi): ViewModel() {




    suspend fun getLotteryNumberListByDateTime(date: String, time: String, userId: String) =PdfRepositories().getLotteryNumberListByDateTime(date,time,userId,myApi)
    suspend fun getLotteryNumberListByDateSlot(date: String, slot: Int, userId: String) =PdfRepositories().getLotteryNumberListByDateSlot(date,slot,userId,myApi)
    suspend fun getLotteryNumberListByDateSlotSecondServer(date: String, slot: Int, userId: String) =PdfRepositories().getLotteryNumberListByDateSlot(date,slot,userId,myApi)

    suspend fun getAdsImageInfo() =PdfRepositories().getAdsImageInfo(myApi)









}