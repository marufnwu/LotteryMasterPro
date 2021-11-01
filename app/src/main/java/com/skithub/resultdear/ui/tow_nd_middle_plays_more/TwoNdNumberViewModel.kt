package com.skithub.resultdear.ui.tow_nd_middle_plays_more

import androidx.lifecycle.ViewModel
import com.skithub.resultdear.database.PdfRepositories
import com.skithub.resultdear.database.network.MyApi
import com.skithub.resultdear.model.response.BannerRes
import retrofit2.Response

class TwoNdNumberViewModel(var myApi: MyApi): ViewModel() {




    suspend fun getMiddlePlaysMoreNumberList(pageNumber: String, itemCount: String) =
        PdfRepositories().gettwoNdMiddlePlaysMoreList(pageNumber,itemCount,myApi)

    suspend fun getoneMiddlePlaysMoreNumberList(pageNumber: String, itemCount: String) =
        PdfRepositories().get1stNdMiddlePlaysMoreList(pageNumber,itemCount,myApi)

    suspend fun getPaidForContact(pageId: String, userId: String) =
        PdfRepositories().getPaidForContact(pageId,userId,myApi)

    suspend fun getBanner(banner: String)=  PdfRepositories().getBanner(banner, myApi)




}