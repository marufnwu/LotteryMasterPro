package com.skithub.resultdear.ui.common_number

import androidx.lifecycle.ViewModel
import com.skithub.resultdear.database.PdfRepositories
import com.skithub.resultdear.database.network.MyApi

class CommonNumberViewModel(var myApi: MyApi): ViewModel() {




    suspend fun getDuplicateLotteryNumberList(pageNumber: String, itemCount: String) =
        PdfRepositories().getDuplicateLotteryNumberList(pageNumber,itemCount,myApi)



    suspend fun getPaidForContact(pageId: String, userId: String) =
        PdfRepositories().getPaidForContact(pageId,userId,myApi)





}