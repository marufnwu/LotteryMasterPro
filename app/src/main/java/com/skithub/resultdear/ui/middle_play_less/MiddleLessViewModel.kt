package com.skithub.resultdear.ui.middle_play_less

import androidx.lifecycle.ViewModel
import com.skithub.resultdear.database.PdfRepositories
import com.skithub.resultdear.database.network.MyApi

class MiddleLessViewModel(var myApi: MyApi): ViewModel() {




    suspend fun getMiddleLessNumberList(pageNumber: String, itemCount: String) =
        PdfRepositories().getMiddleLessNumberList(pageNumber,itemCount,myApi)



    suspend fun getPaidForContact(pageId: String, userId: String) =
        PdfRepositories().getPaidForContact(pageId,userId,myApi)





}