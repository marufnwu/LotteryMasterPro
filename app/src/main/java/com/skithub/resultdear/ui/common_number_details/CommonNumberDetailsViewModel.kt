package com.skithub.resultdear.ui.common_number_details

import androidx.lifecycle.ViewModel
import com.skithub.resultdear.database.PdfRepositories
import com.skithub.resultdear.database.network.MyApi

class CommonNumberDetailsViewModel(var myApi: MyApi): ViewModel() {




    suspend fun getLotteryNumberListUsingLotteryNumber(lotteryNumber: String) =
        PdfRepositories().getLotteryNumberListUsingLotteryNumber(lotteryNumber,myApi)









}