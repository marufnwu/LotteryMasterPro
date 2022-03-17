package com.skithub.resultdear.ui.lottery_number_check

import androidx.lifecycle.ViewModel
import com.skithub.resultdear.database.PdfRepositories
import com.skithub.resultdear.database.network.MyApi

class LotteryNumberCheckViewModel(var myApi: MyApi): ViewModel() {


    suspend fun findLotteryInfoUsingLotteryNumber(pageNumber: String, itemCount: String,lotteryNumber: String) =
        PdfRepositories().findSimilarLotteryNumberList(pageNumber,itemCount,lotteryNumber,myApi)









}