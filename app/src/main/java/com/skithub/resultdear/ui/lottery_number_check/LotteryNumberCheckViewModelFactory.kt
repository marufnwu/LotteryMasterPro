package com.skithub.resultdear.ui.lottery_number_check

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.skithub.resultdear.database.network.MyApi

class LotteryNumberCheckViewModelFactory(private val myApi: MyApi): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LotteryNumberCheckViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LotteryNumberCheckViewModel(myApi) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }



}