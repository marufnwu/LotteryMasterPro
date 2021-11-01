package com.skithub.resultdear.ui.winning_number

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.skithub.resultdear.database.network.MyApi

class WinningNumberActivityViewModelFactory(private val myApi: MyApi): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WinningNumberActivityViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WinningNumberActivityViewModel(myApi) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }



}