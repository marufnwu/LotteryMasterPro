package com.skithub.resultdear.ui.main

import androidx.lifecycle.ViewModel
import com.skithub.resultdear.database.PdfRepositories
import com.skithub.resultdear.database.network.MyApi

class MainViewModel(var myApi: MyApi): ViewModel() {




    suspend fun getHomeTutorialInfo() = PdfRepositories().getHomeTutorialInfo(myApi)

    suspend fun uploadUserInfo(token: String, phone: String,registrationDate: String,activeStatus: String, countryCode: String) = PdfRepositories().uploadUserInfo(token,phone,registrationDate,activeStatus,countryCode,myApi)

    suspend fun getUserInfoByToken(userId: String,token: String) = PdfRepositories().getUserInfoByToken(userId,token,myApi)

    suspend fun getLogout(userId: String) = PdfRepositories().getLogout(userId,myApi)

    suspend fun getPaidForContact(pageId: String, userId: String) =
        PdfRepositories().getPaidForContact(pageId,userId,myApi)





}