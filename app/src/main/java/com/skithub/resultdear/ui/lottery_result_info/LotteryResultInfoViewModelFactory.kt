package com.skithub.resultdear.ui.lottery_result_info

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.skithub.resultdear.database.network.MyApi

class LotteryResultInfoViewModelFactory(private val myApi: MyApi): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LotteryResultInfoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LotteryResultInfoViewModel(myApi) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }



}