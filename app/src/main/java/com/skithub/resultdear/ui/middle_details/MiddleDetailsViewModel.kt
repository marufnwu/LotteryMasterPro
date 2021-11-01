package com.skithub.resultdear.ui.middle_details

import androidx.lifecycle.ViewModel
import com.skithub.resultdear.database.PdfRepositories
import com.skithub.resultdear.database.network.MyApi

class MiddleDetailsViewModel(var myApi: MyApi): ViewModel() {




    suspend fun getMiddleListUsingLotteryNumber(lotteryNumber: String) =
        PdfRepositories().getMiddleListUsingLotteryNumber(lotteryNumber,myApi)

    suspend fun get2ndMiddleListUsingLotteryNumber(lotteryNumber: String) =
        PdfRepositories().get2ndMiddleListUsingLotteryNumber(lotteryNumber,myApi)

    suspend fun get1stMiddleListUsingLotteryNumber(lotteryNumber: String) =
        PdfRepositories().get1stMiddleListUsingLotteryNumber(lotteryNumber,myApi)





}