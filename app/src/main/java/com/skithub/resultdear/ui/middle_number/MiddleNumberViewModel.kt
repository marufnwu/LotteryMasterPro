package com.skithub.resultdear.ui.middle_number

import androidx.lifecycle.ViewModel
import com.skithub.resultdear.database.PdfRepositories
import com.skithub.resultdear.database.network.MyApi

class MiddleNumberViewModel(var myApi: MyApi): ViewModel() {




    suspend fun getMiddlePlaysMoreNumberList(pageNumber: String, itemCount: String) =
        PdfRepositories().getMiddlePlaysMoreNumberList(pageNumber,itemCount,myApi)


    suspend fun getPaidForContact(pageId: String, userId: String) =
        PdfRepositories().getPaidForContact(pageId,userId,myApi)

    suspend fun getSpecialNumber(userId: String) =
        PdfRepositories().getSpeciallotteryNumber(userId,myApi)

    suspend fun getVideo(userId: String) =
        PdfRepositories().getTutorialVideo(userId,myApi)


}