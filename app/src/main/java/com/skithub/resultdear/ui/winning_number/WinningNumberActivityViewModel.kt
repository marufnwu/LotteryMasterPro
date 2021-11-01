package com.skithub.resultdear.ui.winning_number

import androidx.lifecycle.ViewModel
import com.skithub.resultdear.database.PdfRepositories
import com.skithub.resultdear.database.network.MyApi

class WinningNumberActivityViewModel(var myApi: MyApi): ViewModel() {




    suspend fun findLotteryNumberListWithWinType(pageNumber: String, itemCount: String, winType: String) =
        PdfRepositories().getLotteryNumberListByWinType(pageNumber,itemCount,winType,myApi)









}