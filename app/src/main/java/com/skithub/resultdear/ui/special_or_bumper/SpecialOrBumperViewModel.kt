package com.skithub.resultdear.ui.special_or_bumper

import androidx.lifecycle.ViewModel
import com.skithub.resultdear.database.PdfRepositories
import com.skithub.resultdear.database.network.MyApi

class SpecialOrBumperViewModel(var myApi: MyApi): ViewModel() {


    suspend fun bumperLotteryResultList(pageNumber: String, itemCount: String) = PdfRepositories().getBumperLotteryResultList(pageNumber,itemCount,myApi)








}